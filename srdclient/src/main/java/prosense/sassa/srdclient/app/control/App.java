package prosense.sassa.srdclient.app.control;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import javax.inject.Inject;

import javax.jms.JMSContext;
import javax.jms.JMSException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

import java.io.IOException;

import prosense.sassa.srdclient.api.control.Context;
import prosense.sassa.srdclient.api.control.Property;

@ApplicationScoped
public class App {
    private Properties environment;

    @Inject
    @Property
    private String mq_host;

    @Inject
    @Property
    private String mq_port;

    @Inject
    @Property
    private String mq_channel;

    @Inject
    @Property
    private String mq_qmgr;

    @Inject
    @Property
    private String mq_app_user;

    @Inject
    @Property
    private String mq_app_password;

    public App() {
		environment = new Properties();
		try {
			environment.load(RestApp.class.getResourceAsStream("environment.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @Produces
    public Logger logger(InjectionPoint injectionPoint) {
        return LoggerFactory.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
    }

    @Produces
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Produces
    @PersistenceContext(unitName = "srdclientUserPu")
    private EntityManager em;

    @Produces
    @Property
    public String property(final InjectionPoint ip) {
        final Property annotation = ip.getAnnotated().getAnnotation(Property.class);
        if (annotation.value().isEmpty()) {
            return environment.getProperty(ip.getMember().getName());
        }
        return environment.getProperty(annotation.value());
    }

    @Produces
	@Context
    public JMSContext jmsContext() throws JMSException{
        JmsFactoryFactory ff = JmsFactoryFactory.getInstance(WMQConstants.WMQ_PROVIDER);
        JmsConnectionFactory cf = ff.createConnectionFactory();
        cf.setStringProperty(WMQConstants.WMQ_HOST_NAME, mq_host);
        cf.setIntProperty(WMQConstants.WMQ_PORT, Integer.parseInt(mq_port));
        cf.setStringProperty(WMQConstants.WMQ_CHANNEL, mq_channel);
        cf.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_CLIENT);
        cf.setStringProperty(WMQConstants.WMQ_QUEUE_MANAGER, mq_qmgr);
        cf.setBooleanProperty(WMQConstants.USER_AUTHENTICATION_MQCSP, false);
        cf.setStringProperty(WMQConstants.USERID, mq_app_user);
        cf.setStringProperty(WMQConstants.PASSWORD, mq_app_password);
        return cf.createContext();
    }
}
