package prosense.sassa.srdclient.queue.sarscompliance.control;

import java.time.ZonedDateTime;

import javax.enterprise.context.Dependent;

import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.slf4j.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.StringReader;

import prosense.sassa.srdclient.mqevent.boundary.MQEventsResource;
import prosense.sassa.srdclient.mqevent.entity.MQEvent;
import prosense.sassa.srdclient.sarscompliance.control.SarsComplianceStore;

@Dependent
public class SarsComplianceResponseBroker {
    private static Logger logger;
    private static MQEventsResource mqeventsResource;
    private static SarsComplianceStore sarsComplianceStore;
    private static final String QUEUE_NAME = "SASSA.ENQUIRECOMPLIANCEDATA.RES";

    public SarsComplianceResponseBroker(JMSContext jmsContext, Logger logger, MQEventsResource mqeventsResource, SarsComplianceStore sarsComplianceStore) {
        try {
        	this.logger = logger;
        	this.mqeventsResource = mqeventsResource;
        	this.sarsComplianceStore = sarsComplianceStore;

            Destination destination = jmsContext.createQueue("queue:///" + QUEUE_NAME);
            JMSConsumer consumer = jmsContext.createConsumer(destination);
            consumer.setMessageListener(new SarsComplianceResponseListener());

            logger.info(QUEUE_NAME + " Listener started");
        } catch (Exception e) {
            logger.error("Exception :: ", e);
        }
    }

    private class SarsComplianceResponseListener implements MessageListener {
        @Override
        public void onMessage(Message message) {
            try {
            	String msgId = message.getJMSMessageID();
                String msgText = "nothing";
                if (message instanceof TextMessage) {
                    msgText = ((TextMessage) message).getText();
                } else {
                    msgText = message.toString();
                }
				logger.info(QUEUE_NAME + " data -- " + msgText);
				logger.info(QUEUE_NAME + " message id -- " + msgId);

				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document doc = builder.parse(new InputSource(new StringReader(msgText)));
				XPath xpath = XPathFactory.newInstance().newXPath();
				String outcomeId = (String) xpath.evaluate("//*[local-name()='universalUniqueID']", doc, XPathConstants.STRING);
				String irp5FoundInd = (String) xpath.evaluate("//*[local-name()='IRP5FoundInd']", doc, XPathConstants.STRING);
				
				String response = sarsComplianceStore.srdCallback(outcomeId, irp5FoundInd);
				logger.info(" srdCallback response -- " + response);

				mqeventsResource.createMQEvent(MQEvent.builder().outcomeId(Long.valueOf(outcomeId)).sequence(3L).messageId(msgId).correlation(msgId).type("GET")
				.effectedBy("SRDCLIENT").effectedOn(QUEUE_NAME).occurred(ZonedDateTime.now()).context("SOCPENHIGHRISK").creator("srdclient").created(ZonedDateTime.now()).build());
            } catch (Exception e) {
                logger.error("Exception:: ", e);
            }
        }
    }
}
