package prosense.sassa.mqclient.queue.qmstats.control;

import java.util.Map;
import java.time.ZonedDateTime;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import javax.jms.JMSConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;

import org.slf4j.Logger;

import org.apache.commons.lang3.RandomStringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import prosense.sassa.mqclient.api.control.Context;
import prosense.sassa.mqclient.ping.entity.Ping;
import prosense.sassa.mqclient.ping.control.PingAgent;

import prosense.sassa.mqclient.mqevent.boundary.MQEventsResource;
import prosense.sassa.mqclient.mqevent.entity.MQEvent;

@Dependent
public class QMStatsBroker {
    @Inject
    Logger logger;

    @Inject
    ObjectMapper objectMapper;

	@Inject
	@Context
    JMSContext jmsContext;

    @Inject
    PingAgent pingAgent;

    @Inject
    MQEventsResource mqeventsResource;

    public void sendQMStatsMessage(Ping ping) {
        try {
			String unique = RandomStringUtils.randomNumeric(44) + String.valueOf(System.currentTimeMillis());
			ping.setCorrelation("ID:" + String.format("%048x", new java.math.BigInteger(unique)));

			mqeventsResource.createMQEvent(MQEvent.builder().sequence(1L).correlation(ping.getCorrelation()).type("POST")
			.effectedBy("NONREPUDIATION").effectedOn("mqclinet/api/pings").occurred(ZonedDateTime.now()).context("SOCPENPING").creator("iamadmin").created(ZonedDateTime.now()).build());

    		String RESP_QUEUE_NAME = "QM_STATS";
            Destination destination = jmsContext.createQueue("queue:///" + RESP_QUEUE_NAME);
            JMSProducer producer = jmsContext.createProducer();

        	String request = objectMapper.writeValueAsString(pingAgent.toMap(ping));
			logger.info("ping request msgId -- " + ping.getCorrelation());
            logger.info("ping request -- " + request);
            producer.setJMSCorrelationID(ping.getCorrelation());
            producer.send(destination, request);

			mqeventsResource.createMQEvent(MQEvent.builder().sequence(2L).messageId(ping.getCorrelation()).correlation(ping.getCorrelation()).type("PUT")
			.effectedBy("MQCLIENT").effectedOn("QM_STATS").occurred(ZonedDateTime.now()).context("SOCPENPING").creator("iamadmin").created(ZonedDateTime.now()).build());
        } catch (Exception e) {
            logger.error("Exception", e);
        }
    }
}
