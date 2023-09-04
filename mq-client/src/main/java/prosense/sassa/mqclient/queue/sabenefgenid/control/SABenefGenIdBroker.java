package prosense.sassa.mqclient.queue.sabenefgenid.control;

import java.time.ZonedDateTime;

import javax.enterprise.context.Dependent;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSProducer;
import javax.jms.JMSContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.slf4j.Logger;

import prosense.sassa.mqclient.beneficiary.control.BeneficiaryStore;
import prosense.sassa.mqclient.beneficiary.control.QuadAgent;
import prosense.sassa.mqclient.beneficiary.entity.Quad;
import prosense.sassa.mqclient.mqevent.boundary.MQEventsResource;
import prosense.sassa.mqclient.mqevent.entity.MQEvent;

@Dependent
public class SABenefGenIdBroker {
    private static JMSProducer producer;
    private static Logger logger;
    private static ObjectMapper objectMapper;
    private static BeneficiaryStore beneficiaryStore;
    private static QuadAgent quadAgent;
    private static MQEventsResource mqeventsResource;
    private static final String QUEUE_NAME = "SABENEF_GENID";

    public SABenefGenIdBroker(JMSContext jmsContext, Logger logger, ObjectMapper objectMapper, BeneficiaryStore beneficiaryStore, QuadAgent quadAgent, MQEventsResource mqeventsResource) {
        try {
        	this.logger = logger;
        	this.objectMapper = objectMapper;
        	this.beneficiaryStore = beneficiaryStore;
        	this.quadAgent = quadAgent;
        	this.mqeventsResource = mqeventsResource;

            Destination destination = jmsContext.createQueue("queue:///" + QUEUE_NAME);
            JMSConsumer consumer = jmsContext.createConsumer(destination);
            consumer.setMessageListener(new SABenefGenIdListener());

            logger.info(QUEUE_NAME + " Listener started");
        } catch (Exception e) {
            logger.error("Exception :: ", e);
        }
    }

    private class SABenefGenIdListener implements MessageListener {
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
                msgText = msgText.replace("\"alternateID\":", "\"quadNumber\":");
                msgText = msgText.replace("\"fullName\":", "\"fullname\":");
				logger.info(QUEUE_NAME + " request data -- " + msgText);

		        Quad quad = quadAgent.forCreate((ObjectNode)objectMapper.readTree(msgText));
		
				mqeventsResource.createMQEvent(MQEvent.builder().sequence(7L).messageId(msgId).type("GET")
				.effectedBy("MQCLIENT").effectedOn("SABENEF_GENID").occurred(ZonedDateTime.now()).context("BENEFICIARYSOCPEN").idDob(quad.getQuadNumber()).creator("iamadmin").created(ZonedDateTime.now()).build());

                quad = beneficiaryStore.createQuad(quad);

				mqeventsResource.createMQEvent(MQEvent.builder().sequence(8L).type("POST")
				.effectedBy("MQCLIENT").effectedOn("beneficiaries/api/quads").occurred(ZonedDateTime.now()).context("BENEFICIARYSOCPEN").idDob(quad.getQuadNumber()).creator("iamadmin").created(ZonedDateTime.now()).build());
            } catch (Exception e) {
                logger.error("Exception:: ", e);
            }
        }
    }
}
