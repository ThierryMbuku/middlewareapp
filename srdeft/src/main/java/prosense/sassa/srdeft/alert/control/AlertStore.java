package prosense.sassa.srdeft.alert.control;

import org.slf4j.Logger;

import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.Dependent;

import javax.inject.Inject;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.client.Entity;
import javax.ws.rs.WebApplicationException;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import prosense.sassa.srdeft.api.control.App;
import prosense.sassa.srdeft.api.control.Property;


@Dependent
public class AlertStore {
    @Inject
    @App
    Logger logger;

    @Inject
    @Property
    private String alert_group;

    @Inject
    @Property
    private String alert_from;

    @Inject
    @Property
    private String smtp_host;

    @Inject
    @Property
    private String smtp_port;

    @Inject
    @Property
    private String sms_server_url;

    @Inject
    @Property
    private String sms_server_token;
    
    @Inject
    Map<String, Object> mbeanServerDetails;
    
    private String toEmails;
    private String toNumbers;

    public void sendAlert(final String subject, final String body) {
    	logger.info(subject);
    	logger.info(body);
		new Thread(() -> send(subject, body)).start();
	}

    private void send(String subject, String body) {
    	loadContacts();
    	sendEmail(subject, body);
    	sendSms(subject);
    }

    private void loadContacts() {
    	List<String> toEmails = new ArrayList<String>();
    	List<String> toNumbers = new ArrayList<String>();
        try {
			MBeanServerConnection connection = (MBeanServerConnection)mbeanServerDetails.get("connection");
			ObjectName authenticator = (ObjectName)mbeanServerDetails.get("authenticator");
			String[] userNames = (String[])connection.invoke(authenticator, "listAllUsersInGroup",
													  new Object[] { alert_group, "*", new java.lang.Integer(0) },
													  new String [] { "java.lang.String", "java.lang.String", "java.lang.Integer" });
			logger.info("userNames :: " + String.join(",", userNames));
			Arrays.asList(userNames).forEach(userName -> {
				try {
					String mail = (String)connection.invoke(authenticator, "getUserAttributeValue", new Object[] { userName, "mail" }, new String[] { "java.lang.String", "java.lang.String" });
					if(!StringUtils.isEmpty(mail))
						toEmails.add(mail);
					String mobile = (String)connection.invoke(authenticator, "getUserAttributeValue", new Object[] { userName, "mobile" }, new String[] { "java.lang.String", "java.lang.String" });
					if(!StringUtils.isEmpty(mobile))
						toNumbers.add("\"" + mobile + "\"");
				} catch(Exception e){
					logger.error("exception", e);
					return;
				}
			});
			this.toEmails = String.join(",", toEmails);
			this.toNumbers = String.join(",", toNumbers);
	    } catch(Exception e){
            logger.error("exception", e);
        }
    }

    private void sendEmail(String subject, String body) {
        try {
        	logger.info(toEmails);
        	if(StringUtils.isEmpty(toEmails))
        		return;
        	Properties prop = new Properties();
        	prop.put("mail.smtp.host", smtp_host);
        	prop.put("mail.smtp.port", smtp_port);
        	Session session = Session.getInstance(prop, null);
        	
			MimeMessage msg = new MimeMessage(session);
			msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			msg.addHeader("format", "flowed");
			msg.addHeader("Content-Transfer-Encoding", "8bit");
			msg.setFrom(new InternetAddress(alert_from));
			msg.setReplyTo(InternetAddress.parse("no-reply@sassa.gov.za", false));
			msg.setSubject(subject, "UTF-8");
			msg.setText(body, "UTF-8");
			msg.setSentDate(new Date());
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmails, false));
			
			Transport.send(msg);  
			logger.info("email with subject " + subject + " sent to " + toEmails);
	    } catch(Exception e){
            logger.error("exception", e);
        }
    }

    private void sendSms(String content) {
        try {
        	logger.info(toNumbers);
        	if(StringUtils.isEmpty(toNumbers))
        		return;
        	String json = "{\"to\": [" + toNumbers + "], \"content\": \"" + content + "\"}";
			logger.info(json);
			Client client = ClientBuilder.newClient();
			Response response = client.target(sms_server_url)
									  .request(MediaType.APPLICATION_JSON)
									  .accept(MediaType.APPLICATION_JSON)
									  .header(HttpHeaders.AUTHORIZATION, sms_server_token)
									  .post(Entity.json(json));
			if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL)
				throw new WebApplicationException(response);
			logger.info("sms with content " + content + " sent to " + toNumbers);
	    } catch(Exception e){
            logger.error("exception", e);
        }
    }
}
