package prosense.sassa.mqclient.queue.qmstatsresp.control;

import java.time.ZonedDateTime;

import javax.enterprise.context.Dependent;

import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.slf4j.Logger;

import prosense.sassa.mqclient.mqevent.boundary.MQEventsResource;
import prosense.sassa.mqclient.mqevent.entity.MQEvent;

@Dependent
public class QMStatsRespBroker {
    private static Logger logger;
    private static MQEventsResource mqeventsResource;
    private static final String QUEUE_NAME = "QM_STATS_RESP";

    public QMStatsRespBroker(JMSContext jmsContext, Logger logger, MQEventsResource mqeventsResource) {
        try {
        	this.logger = logger;
        	this.mqeventsResource = mqeventsResource;

            Destination destination = jmsContext.createQueue("queue:///" + QUEUE_NAME);
            JMSConsumer consumer = jmsContext.createConsumer(destination);
            consumer.setMessageListener(new QMStatsRespListener());

            logger.info(QUEUE_NAME + " Listener started");
        } catch (Exception e) {
            logger.error("Exception :: ", e);
        }
    }

    private class QMStatsRespListener implements MessageListener {
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
				logger.info(QUEUE_NAME + " request data -- " + msgText);
				mqeventsResource.createMQEvent(MQEvent.builder().sequence(3L).messageId(msgId).correlation(msgId).type("GET")
				.effectedBy("MQCLIENT").effectedOn("QM_STATS_RESP").occurred(ZonedDateTime.now()).context("SOCPENPING").creator("iamadmin").created(ZonedDateTime.now()).build());
				logger.info("ping response msgId -- " + msgId);
				logger.info("ping response -- " + msgText);
            } catch (Exception e) {
                logger.error("Exception:: ", e);
            }
        }
    }
}
