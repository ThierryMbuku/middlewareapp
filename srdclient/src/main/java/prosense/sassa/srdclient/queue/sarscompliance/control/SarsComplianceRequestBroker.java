package prosense.sassa.srdclient.queue.sarscompliance.control;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.TextMessage;

import com.ibm.msg.client.wmq.WMQConstants;

import org.slf4j.Logger;

import java.io.StringWriter;

import java.util.UUID;
 
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
 
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import prosense.sassa.srdclient.api.control.Context;
import prosense.sassa.srdclient.sarscompliance.entity.SarsCompliance;
import prosense.sassa.srdclient.mqevent.boundary.MQEventsResource;
import prosense.sassa.srdclient.mqevent.entity.MQEvent;


@Dependent
public class SarsComplianceRequestBroker {
    @Inject
    Logger logger;

	@Inject
	@Context
    JMSContext jmsContext;
    
    @Inject
    MQEventsResource mqeventsResource;
    
    private String QUEUE_NAME = "SASSA.ENQUIRECOMPLIANCEDATA.REQ";

    public void sendSarsComplianceRequestMessage(SarsCompliance sarsCompliance) {
        try {
        	String msgId = "ID:" + UUID.randomUUID().toString().replace("-", "");
			mqeventsResource.createMQEvent(MQEvent.builder().outcomeId(sarsCompliance.getOutcomeId()).sequence(1L).messageId(msgId).correlation(msgId).type("POST")
			.effectedBy("NONREPUDIATION").effectedOn("srdclinet/api/sarsCompliances").occurred(ZonedDateTime.now()).context("SOCPENHIGHRISK").creator("srdclient").created(ZonedDateTime.now()).build());

        	logger.info("in sendSarsComplianceRequestMessage");
        	String request = toXml(sarsCompliance);
        	logger.info("data -- " + request);

            Destination destination = jmsContext.createQueue("queue:///" + QUEUE_NAME);
            JMSProducer producer = jmsContext.createProducer();

            TextMessage message = jmsContext.createTextMessage();
            message.setText(request);
            message.setIntProperty(WMQConstants.JMS_IBM_MSGTYPE, 8);
            message.setStringProperty(WMQConstants.JMS_IBM_FORMAT, "MQSTR   ");
            message.setIntProperty(WMQConstants.JMS_IBM_CHARACTER_SET, 819);
            message.setIntProperty(WMQConstants.JMS_IBM_ENCODING, 273);
            producer.send(destination, message);

			mqeventsResource.createMQEvent(MQEvent.builder().outcomeId(sarsCompliance.getOutcomeId()).sequence(2L).messageId(msgId).correlation(msgId).type("POST")
			.effectedBy("SRDCLIENT").effectedOn(QUEUE_NAME).occurred(ZonedDateTime.now()).context("SOCPENHIGHRISK").creator("srdclient").created(ZonedDateTime.now()).build());
        } catch (Exception e) {
            logger.error("Exception", e);
        }
    }
    
    private String toXml (SarsCompliance sarsCompliance) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        String xml = "";
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            
            Element request = doc.createElement("soap:Envelope");
            request.setAttribute("xmlns:soap", "http://www.w3.org/2003/05/soap-envelope");
            request.setAttribute("xmlns:ns", "http://www.egovernment.gov.za/GMD/SARSExternalHeader/xml/schemas/version/1.0");
            request.setAttribute("xmlns:ns1", "http://www.sars.gov.za/GMD/EnquireOGACompliance/xml/schemas/version/1.5");
            doc.appendChild(request);
            
            Element header = doc.createElement("soap:Header");
            request.appendChild(header);
            Element messageIdentification = doc.createElement("ns:MessageIdentification");
            header.appendChild(messageIdentification);
            messageIdentification.appendChild(getElement(doc, "ns:channelID", "SASSA"));
            messageIdentification.appendChild(getElement(doc, "ns:applicationID", "ENQUIREDATACOMPLIANCE"));
            messageIdentification.appendChild(getElement(doc, "ns:messageSeqNo", String.valueOf(sarsCompliance.getOutcomeId())));
            messageIdentification.appendChild(getElement(doc, "ns:messageTimeStamp", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss").format(ZonedDateTime.now())));
            messageIdentification.appendChild(getElement(doc, "ns:externalReferenceID", sarsCompliance.getIdentificationNumber()));
            messageIdentification.appendChild(getElement(doc, "ns:universalUniqueID", String.valueOf(sarsCompliance.getOutcomeId())));
            messageIdentification.appendChild(getElement(doc, "ns:versionNo", "1.5"));
 
            Element body = doc.createElement("soap:Body");
            request.appendChild(body);
            Element enquireOGAComplianceRequest = doc.createElement("ns1:EnquireOGAComplianceRequest");
            body.appendChild(enquireOGAComplianceRequest);
            Element submittingEntityInfo = doc.createElement("ns1:SubmittingEntityInfo");
            enquireOGAComplianceRequest.appendChild(submittingEntityInfo);
            submittingEntityInfo.appendChild(getElement(doc, "ns1:NatureOfEntity", "GOVERNMENT_ENTITY"));
            submittingEntityInfo.appendChild(getElement(doc, "ns1:RegisteredName", "South African Social Security Agency"));
            submittingEntityInfo.appendChild(getElement(doc, "ns1:TradingName", "South African Social Security Agency"));
            submittingEntityInfo.appendChild(getElement(doc, "ns1:TaxRefNo", "7820757831"));
            submittingEntityInfo.appendChild(getElement(doc, "ns1:SystemUserID", "Social Relief Grant"));
            Element ipr5ConfirmationDetails = doc.createElement("ns1:IRP5ConfirmationDetails");
            enquireOGAComplianceRequest.appendChild(ipr5ConfirmationDetails);
            ipr5ConfirmationDetails.appendChild(getElement(doc, "ns1:NatureOfEntity", "INDIVIDUAL"));
            if(SarsCompliance.IdentificationType.south_african_id_number.name().equals(sarsCompliance.getIdentificationType()))
	            ipr5ConfirmationDetails.appendChild(getElement(doc, "ns1:IdentificationType", "001"));
            else if(SarsCompliance.IdentificationType.foreign_passport_number.name().equals(sarsCompliance.getIdentificationType()))
	            ipr5ConfirmationDetails.appendChild(getElement(doc, "ns1:IdentificationType", "003"));
            else if(SarsCompliance.IdentificationType.asylum_permit_number.name().equals(sarsCompliance.getIdentificationType()))
	            ipr5ConfirmationDetails.appendChild(getElement(doc, "ns1:IdentificationType", "010"));
            ipr5ConfirmationDetails.appendChild(getElement(doc, "ns1:IdentificationNo", sarsCompliance.getIdentificationNumber()));
            if(SarsCompliance.IdentificationType.foreign_passport_number.name().equals(sarsCompliance.getIdentificationType()))
            	ipr5ConfirmationDetails.appendChild(getElement(doc, "ns1:CountryOfIssue", sarsCompliance.getCountryOfIssue()));
            ipr5ConfirmationDetails.appendChild(getElement(doc, "ns1:ConsentReceivedInd", "Y"));

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));

            xml = writer.toString();
        } catch (Exception e) {
	        logger.error("Exception", e);
        }
        return xml;
    }

    private Node getElement(Document doc, String name, String value) {
        Element node = doc.createElement(name);
        node.appendChild(doc.createTextNode(value));
        return node;
    }
}
