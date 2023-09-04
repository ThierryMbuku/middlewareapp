package prosense.sassa.mqclient.queue.saidcheck.control;

import java.time.ZonedDateTime;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;

import org.slf4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import prosense.sassa.mqclient.api.control.Context;
import prosense.sassa.mqclient.beneficiary.entity.BeneficiaryVerify;
import prosense.sassa.mqclient.beneficiary.control.BeneficiaryVerifyAgent;
import prosense.sassa.mqclient.mqevent.boundary.MQEventsResource;
import prosense.sassa.mqclient.mqevent.entity.MQEvent;


@Dependent
public class SAIDCheckBroker {
    @Inject
    Logger logger;

    @Inject
    ObjectMapper objectMapper;

	@Inject
	@Context
    JMSContext jmsContext;

    @Inject
    BeneficiaryVerifyAgent beneficiaryVerifyAgent;

    @Inject
    MQEventsResource mqeventsResource;

    public void sendMessage(BeneficiaryVerify beneficiaryVerify) {
        try {
			if ("green".equalsIgnoreCase(beneficiaryVerify.getStatus()))
				beneficiaryVerify.setStatus("Y");
			else if ("amber".equalsIgnoreCase(beneficiaryVerify.getStatus()) || "red".equalsIgnoreCase(beneficiaryVerify.getStatus()) || beneficiaryVerify.getErrorCode() != null)
				beneficiaryVerify.setStatus("N");
			else if ("blue".equalsIgnoreCase(beneficiaryVerify.getStatus()))
				beneficiaryVerify.setStatus("O");

			mqeventsResource.createMQEvent(MQEvent.builder().sequence(1L).transaction(Long.valueOf(beneficiaryVerify.getApplication())).type("POST")
			.effectedBy("BENEFICIARIES").effectedOn("mqclient/api/beneficiary/ben/verify").occurred(ZonedDateTime.now()).context("BENEFICIARYSOCPEN").idDob(beneficiaryVerify.getApplicantId()).creator("iamadmin").created(ZonedDateTime.now()).build());

    		String QUEUE_NAME = "SAID_CHECK";
            Destination destination_resp = jmsContext.createQueue("queue:///" + QUEUE_NAME);
            JMSProducer producer = jmsContext.createProducer();

        	logger.info("in sendMessage SAID_CHECK");
        	String response = objectMapper.writeValueAsString(beneficiaryVerifyAgent.toMap(beneficiaryVerify));
            logger.info("response data -- " + response);
            producer.send(destination_resp, response);

			mqeventsResource.createMQEvent(MQEvent.builder().sequence(2L).transaction(Long.valueOf(beneficiaryVerify.getApplication())).type("PUT")
			.effectedBy("MQCLIENT").effectedOn("SAID_CHECK").occurred(ZonedDateTime.now()).context("BENEFICIARYSOCPEN").idDob(beneficiaryVerify.getApplicantId()).creator("iamadmin").created(ZonedDateTime.now()).build());
        } catch (Exception e) {
            logger.error("Exception", e);
        }
    }
}
