package prosense.sassa.mqclient.queue.saidcheckpoa.control;

import java.time.ZonedDateTime;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;

import org.slf4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import prosense.sassa.mqclient.api.control.Context;
import prosense.sassa.mqclient.beneficiary.entity.ProcuratorVerify;
import prosense.sassa.mqclient.beneficiary.control.ProcuratorVerifyAgent;
import prosense.sassa.mqclient.mqevent.boundary.MQEventsResource;
import prosense.sassa.mqclient.mqevent.entity.MQEvent;


@Dependent
public class SAIDCheckPOABroker {
    @Inject
    Logger logger;

    @Inject
    ObjectMapper objectMapper;

	@Inject
	@Context
    JMSContext jmsContext;

    @Inject
    ProcuratorVerifyAgent procuratorVerifyAgent;

    @Inject
    MQEventsResource mqeventsResource;

    public void sendMessage(ProcuratorVerify procuratorVerify) {
        try {
			if ("green".equalsIgnoreCase(procuratorVerify.getStatus()))
				procuratorVerify.setStatus("Y");
			else if ("amber".equalsIgnoreCase(procuratorVerify.getStatus()) || "red".equalsIgnoreCase(procuratorVerify.getStatus()) || procuratorVerify.getErrorCode() != null)
				procuratorVerify.setStatus("N");
			else if ("blue".equalsIgnoreCase(procuratorVerify.getStatus()))
				procuratorVerify.setStatus("O");
        
			mqeventsResource.createMQEvent(MQEvent.builder().sequence(1L).transaction(Long.valueOf(procuratorVerify.getApplication())).type("POST")
			.effectedBy("BENEFICIARIES").effectedOn("mqclient/api/beneficiary/proc/verify").occurred(ZonedDateTime.now()).context("BENEFICIARYSOCPEN").idDob(procuratorVerify.getApplicantId()).creator("iamadmin").created(ZonedDateTime.now()).build());

    		String QUEUE_NAME = "SAID_CHECK_POA";
            Destination destination_resp = jmsContext.createQueue("queue:///" + QUEUE_NAME);
            JMSProducer producer = jmsContext.createProducer();

        	logger.info("in sendMessage SAID_CHECK_POA");
        	String response = objectMapper.writeValueAsString(procuratorVerifyAgent.toMap(procuratorVerify));
            logger.info("response data -- " + response);
            producer.send(destination_resp, response);

			mqeventsResource.createMQEvent(MQEvent.builder().sequence(2L).transaction(Long.valueOf(procuratorVerify.getApplication())).type("PUT")
			.effectedBy("MQCLIENT").effectedOn("SAID_CHECK_POA").occurred(ZonedDateTime.now()).context("BENEFICIARYSOCPEN").idDob(procuratorVerify.getApplicantId()).creator("iamadmin").created(ZonedDateTime.now()).build());
        } catch (Exception e) {
            logger.error("Exception", e);
        }
    }
}
