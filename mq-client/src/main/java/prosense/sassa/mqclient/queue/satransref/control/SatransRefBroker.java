package prosense.sassa.mqclient.queue.satransref.control;

import java.time.ZonedDateTime;
import java.time.Duration;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import javax.jms.JMSConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.slf4j.Logger;

import prosense.sassa.mqclient.transaction.control.TransactionStore;
import prosense.sassa.mqclient.transaction.entity.Transaction;
import prosense.sassa.mqclient.mqevent.boundary.MQEventsResource;
import prosense.sassa.mqclient.mqevent.entity.MQEvent;


@Dependent
public class SatransRefBroker {
    private static Destination destination_resp;
    private static JMSProducer producer;
    private static Logger logger;
    private static ObjectMapper objectMapper;
    private static TransactionStore transactionStore;
    private static MQEventsResource mqeventsResource;
    private static final String QUEUE_NAME = "SATRANS_REF";
    private static final String RESP_QUEUE_NAME = "SATRANS_REF_RESP";

    public SatransRefBroker(JMSContext jmsContext, Logger logger, ObjectMapper objectMapper, TransactionStore transactionStore, MQEventsResource mqeventsResource) {
        try {
        	this.logger = logger;
        	this.objectMapper = objectMapper;
        	this.transactionStore = transactionStore;
        	this.mqeventsResource = mqeventsResource;

            destination_resp = jmsContext.createQueue("queue:///" + RESP_QUEUE_NAME);
            producer = jmsContext.createProducer();

            Destination destination = jmsContext.createQueue("queue:///" + QUEUE_NAME);
            JMSConsumer consumer = jmsContext.createConsumer(destination);
            consumer.setMessageListener(new SatransRefListener());

            logger.info(QUEUE_NAME + " Listener started");
        } catch (Exception e) {
            logger.error("Exception :: ", e);
        }
    }

    private class SatransRefListener implements MessageListener {
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
                msgText = msgText.replace("\"username\":", "\"socpenUser\":");
				logger.info(QUEUE_NAME + " request data -- " + msgText);
				logger.info(QUEUE_NAME + " message id -- " + msgId);
				if(transactionStore == null){
					logger.info("transactionStore is null");
				}
				mqeventsResource.createMQEvent(MQEvent.builder().sequence(2L).messageId(msgId).correlation(msgId).type("GET")
				.effectedBy("MQCLIENT").effectedOn("SATRANS_REF").occurred(ZonedDateTime.now()).context("SOCPENHIGHRISK").creator("iamadmin").created(ZonedDateTime.now()).build());
                
                ZonedDateTime start = ZonedDateTime.now();
                Transaction transaction = transactionStore.createTransaction((ObjectNode)objectMapper.readTree(msgText), msgId);
                long duration = Duration.between(start, ZonedDateTime.now()).toMillis();
				
				logger.info("mqclient to nonrepudiation/api/transactions sync call ==>> domainUser- " + transaction.getDomainUser() + " :: socpenUser- " + transaction.getSocpenUser() + " :: timestamp- " + ZonedDateTime.now() 
				+ " :: challenge- " + transaction.getChallenge() + " :: correlation- " + transaction.getCorrelation() + " :: duration- " + duration + "ms");
				
				mqeventsResource.createMQEvent(MQEvent.builder().sequence(3L).messageId(msgId).correlation(msgId).type("POST")
				.effectedBy("MQCLIENT").effectedOn("nonrepudiation/api/transactions").occurred(ZonedDateTime.now()).context("SOCPENHIGHRISK").creator("iamadmin").created(ZonedDateTime.now()).build());
				
				objectMapper.setSerializationInclusion(Include.NON_EMPTY);
                String response = objectMapper.writeValueAsString(transaction);
				logger.info(RESP_QUEUE_NAME + " response data -- " + response);
				logger.info(RESP_QUEUE_NAME + " correlation id -- " + msgId);
                producer.setJMSCorrelationID(msgId);
                producer.send(destination_resp, response);
				
				mqeventsResource.createMQEvent(MQEvent.builder().sequence(4L).transaction(transaction.getId()).messageId(msgId).correlation(msgId).type("PUT")
				.effectedBy("MQCLIENT").effectedOn("SATRANS_REF_RESP").occurred(ZonedDateTime.now()).context("SOCPENHIGHRISK").creator("iamadmin").created(ZonedDateTime.now()).build());
            } catch (Exception e) {
                logger.error("Exception:: ", e);
            }
        }
    }
}
