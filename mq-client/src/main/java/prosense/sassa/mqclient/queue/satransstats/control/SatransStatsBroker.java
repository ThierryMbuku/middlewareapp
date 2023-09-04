package prosense.sassa.mqclient.queue.satransstats.control;

import java.util.Map;
import java.time.ZonedDateTime;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import javax.jms.JMSConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;

import org.slf4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import prosense.sassa.mqclient.api.control.Context;
import prosense.sassa.mqclient.transaction.entity.Transaction;
import prosense.sassa.mqclient.transaction.control.TransactionAgent;
import prosense.sassa.mqclient.transaction.control.TransactionStore;
import prosense.sassa.mqclient.mqevent.boundary.MQEventsResource;
import prosense.sassa.mqclient.mqevent.entity.MQEvent;


@Dependent
public class SatransStatsBroker {
    @Inject
    Logger logger;

    @Inject
    ObjectMapper objectMapper;

	@Inject
	@Context
    JMSContext jmsContext;

    @Inject
    TransactionAgent transactionAgent;

    @Inject
    TransactionStore transactionStore;

    @Inject
    MQEventsResource mqeventsResource;

    public void sendSatransStatsMessage(Transaction transaction) {
        try {
			mqeventsResource.createMQEvent(MQEvent.builder().transaction(transaction.getId()).sequence(5L).correlation(transaction.getCorrelation()).type("POST")
			.effectedBy("NONREPUDIATION").effectedOn("mqclinet/api/transactions").occurred(ZonedDateTime.now()).context("SOCPENHIGHRISK").creator("iamadmin").created(ZonedDateTime.now()).build());

    		String RESP_QUEUE_NAME = "SATRANS_STATS_RESP";
            Destination destination_resp = jmsContext.createQueue("queue:///" + RESP_QUEUE_NAME);
            JMSProducer producer = jmsContext.createProducer();

        	logger.info("in sendSatransStatsMessage");
        	String response = objectMapper.writeValueAsString(transactionAgent.toMap(transaction));
            logger.info("response data -- " + response);
			logger.info("SATRANS_STATS_RESP correlation id -- " + transaction.getCorrelation());
            producer.setJMSCorrelationID(transaction.getCorrelation());
            producer.send(destination_resp, response);

			transactionStore.createOaamTransaction(transaction);

			mqeventsResource.createMQEvent(MQEvent.builder().transaction(transaction.getId()).sequence(6L).messageId(transaction.getCorrelation()).correlation(transaction.getCorrelation()).type("PUT")
			.effectedBy("MQCLIENT").effectedOn("SATRANS_STATS_RESP").occurred(ZonedDateTime.now()).context("SOCPENHIGHRISK").creator("iamadmin").created(ZonedDateTime.now()).build());
        } catch (Exception e) {
            logger.error("Exception", e);
        }
    }
}
