package prosense.sassa.mqclient.queue.sabenefstats.control;

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
import prosense.sassa.mqclient.beneficiary.control.OutcomeAgent;
import prosense.sassa.mqclient.beneficiary.entity.Outcome;
import prosense.sassa.mqclient.mqevent.boundary.MQEventsResource;
import prosense.sassa.mqclient.mqevent.entity.MQEvent;

@Dependent
public class SABenefStatsBroker {
    private static JMSProducer producer;
    private static Logger logger;
    private static ObjectMapper objectMapper;
    private static BeneficiaryStore beneficiaryStore;
    private static OutcomeAgent outcomeAgent;
    private static MQEventsResource mqeventsResource;
    private static final String QUEUE_NAME = "SABENEF_STATS";

    public SABenefStatsBroker(JMSContext jmsContext, Logger logger, ObjectMapper objectMapper, BeneficiaryStore beneficiaryStore, OutcomeAgent outcomeAgent, MQEventsResource mqeventsResource) {
        try {
        	this.logger = logger;
        	this.objectMapper = objectMapper;
        	this.beneficiaryStore = beneficiaryStore;
        	this.outcomeAgent = outcomeAgent;
        	this.mqeventsResource = mqeventsResource;

            Destination destination = jmsContext.createQueue("queue:///" + QUEUE_NAME);
            JMSConsumer consumer = jmsContext.createConsumer(destination);
            consumer.setMessageListener(new SABenefStatsListener());

            logger.info(QUEUE_NAME + " Listener started");
        } catch (Exception e) {
            logger.error("Exception :: ", e);
        }
    }

    private class SABenefStatsListener implements MessageListener {
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
                String rawMessage = msgText;
                msgText = msgText.replace("\"applicantID\":", "\"idNumber\":");
                msgText = msgText.replace("\"time\":", "\"socpenTime\":");
                msgText = msgText.replace("\"paymentMethod\":", "\"method\":");
                msgText = msgText.replace("\"socpenAppRef\":", "\"ref\":");
                msgText = msgText.replace("\"grantType1\":", "\"type1\":");
                msgText = msgText.replace("\"grantType2\":", "\"type2\":");
                msgText = msgText.replace("\"grantType3\":", "\"type3\":");
                msgText = msgText.replace("\"grantType4\":", "\"type4\":");
                msgText = msgText.replace("\"grantStatus1\":", "\"status1\":");
                msgText = msgText.replace("\"grantStatus2\":", "\"status2\":");
                msgText = msgText.replace("\"grantStatus3\":", "\"status3\":");
                msgText = msgText.replace("\"grantStatus4\":", "\"status4\":");
                msgText = msgText.replace("\"reasonCode1\":", "\"reason1\":");
                msgText = msgText.replace("\"reasonCode2\":", "\"reason2\":");
                msgText = msgText.replace("\"reasonCode3\":", "\"reason3\":");
                msgText = msgText.replace("\"reasonCode4\":", "\"reason4\":");
				logger.info(QUEUE_NAME + " request data -- " + msgText);

		        Outcome outcome = null;
		        try {
			        outcome = outcomeAgent.forCreate((ObjectNode)objectMapper.readTree(msgText));
			    } catch (Exception e) {
			    	outcome = Outcome.builder().messageId(msgId).rawMessage(rawMessage).build();
			    	beneficiaryStore.createOutcomeError(outcome, null, "invalid json message", null, null);
			    	throw e;
			    }
		        outcome.setMessageId(msgId);
		        outcome.setRawMessage(rawMessage);
		
				mqeventsResource.createMQEvent(MQEvent.builder().sequence(5L).messageId(msgId).type("GET")
				.effectedBy("MQCLIENT").effectedOn("SABENEF_STATS").occurred(ZonedDateTime.now()).context("BENEFICIARYSOCPEN").idDob(outcome.getIdNumber()).creator("iamadmin").created(ZonedDateTime.now()).build());

                outcome = beneficiaryStore.createOutcome(outcome, false);

				mqeventsResource.createMQEvent(MQEvent.builder().sequence(6L).type("POST")
				.effectedBy("MQCLIENT").effectedOn("beneficiaries/api/outcomes").occurred(ZonedDateTime.now()).context("BENEFICIARYSOCPEN").idDob(outcome.getIdNumber()).creator("iamadmin").created(ZonedDateTime.now()).build());
            } catch (Exception e) {
                logger.error("Exception:: ", e);
            }
        }
    }
}
