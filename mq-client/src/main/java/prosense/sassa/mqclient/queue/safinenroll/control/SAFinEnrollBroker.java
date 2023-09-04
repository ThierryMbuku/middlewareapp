package prosense.sassa.mqclient.queue.safinenroll.control;

import java.time.ZonedDateTime;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;

import org.slf4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import prosense.sassa.mqclient.api.control.Context;
import prosense.sassa.mqclient.beneficiary.entity.BeneficiaryEnrol;
import prosense.sassa.mqclient.beneficiary.control.BeneficiaryEnrolAgent;
import prosense.sassa.mqclient.mqevent.boundary.MQEventsResource;
import prosense.sassa.mqclient.mqevent.entity.MQEvent;


@Dependent
public class SAFinEnrollBroker {
    @Inject
    Logger logger;

    @Inject
    ObjectMapper objectMapper;

	@Inject
	@Context
    JMSContext jmsContext;

    @Inject
    BeneficiaryEnrolAgent beneficiaryEnrolAgent;

    @Inject
    MQEventsResource mqeventsResource;

    public void sendMessage(BeneficiaryEnrol beneficiaryEnrol) {
        try {
			mqeventsResource.createMQEvent(MQEvent.builder().sequence(3L).transaction(Long.valueOf(beneficiaryEnrol.getApplication())).type("POST")
			.effectedBy("BENEFICIARIES").effectedOn("mqclient/api/beneficiary/id/enrol").occurred(ZonedDateTime.now()).context("BENEFICIARYSOCPEN").idDob(beneficiaryEnrol.getApplicantId()).creator("iamadmin").created(ZonedDateTime.now()).build());

    		String QUEUE_NAME = "SAFIN_ENROLL";
            Destination destination_resp = jmsContext.createQueue("queue:///" + QUEUE_NAME);
            JMSProducer producer = jmsContext.createProducer();

        	logger.info("in sendMessage SAFIN_ENROLL");
        	String response = objectMapper.writeValueAsString(beneficiaryEnrolAgent.toMap(beneficiaryEnrol));
            logger.info("response data -- " + response);
            producer.send(destination_resp, response);

			mqeventsResource.createMQEvent(MQEvent.builder().sequence(4L).transaction(Long.valueOf(beneficiaryEnrol.getApplication())).type("PUT")
			.effectedBy("MQCLIENT").effectedOn("SAFIN_ENROLL").occurred(ZonedDateTime.now()).context("BENEFICIARYSOCPEN").idDob(beneficiaryEnrol.getApplicantId()).creator("iamadmin").created(ZonedDateTime.now()).build());
        } catch (Exception e) {
            logger.error("Exception", e);
        }
    }
}
