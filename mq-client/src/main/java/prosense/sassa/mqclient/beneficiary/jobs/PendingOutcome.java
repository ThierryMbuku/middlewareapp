package prosense.sassa.mqclient.beneficiary.jobs;

import java.time.ZonedDateTime;

import java.util.Set;

import org.slf4j.Logger;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import prosense.sassa.mqclient.api.control.App;
import prosense.sassa.mqclient.beneficiary.entity.Outcome;
import prosense.sassa.mqclient.beneficiary.entity.OutcomeError;
import prosense.sassa.mqclient.beneficiary.boundary.OutcomeErrorsResource;
import prosense.sassa.mqclient.beneficiary.control.BeneficiaryStore;
import prosense.sassa.mqclient.mqevent.boundary.MQEventsResource;
import prosense.sassa.mqclient.mqevent.entity.MQEvent;

public class PendingOutcome implements Job {
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        Logger logger = (Logger)dataMap.get("logger");
        OutcomeErrorsResource outcomeErrorsResource = (OutcomeErrorsResource)dataMap.get("outcomeErrorsResource");
        BeneficiaryStore beneficiaryStore = (BeneficiaryStore)dataMap.get("beneficiaryStore");
        MQEventsResource mqeventsResource = (MQEventsResource)dataMap.get("mqeventsResource");
        
        Set<OutcomeError> outcomeErrors = outcomeErrorsResource.getPendingOutcomes();
        outcomeErrors.forEach(outcomeError -> {
			try {
				Outcome outcome = Outcome.builder().application(outcomeError.getApplication()).idNumber(outcomeError.getIdNumber()).socpenUser(outcomeError.getSocpenUser())
									.office(outcomeError.getOffice()).socpenTime(outcomeError.getSocpenTime()).method(outcomeError.getMethod()).ref(outcomeError.getRef())
									.type1(outcomeError.getType1()).type2(outcomeError.getType2()).type3(outcomeError.getType3()).type4(outcomeError.getType4())
									.status1(outcomeError.getStatus1()).status2(outcomeError.getStatus2()).status3(outcomeError.getStatus3()).status4(outcomeError.getStatus4())
									.reason1(outcomeError.getReason1()).reason2(outcomeError.getReason2()).reason3(outcomeError.getReason3()).reason4(outcomeError.getReason4())
									.messageId(outcomeError.getMessageId()).rawMessage(outcomeError.getRawMessage())
									.build();

				outcome = beneficiaryStore.createOutcome(outcome, true);

				mqeventsResource.createMQEvent(MQEvent.builder().sequence(6L).type("POST")
				.effectedBy("MQCLIENT").effectedOn("beneficiaries/api/outcomes").occurred(ZonedDateTime.now()).context("BENEFICIARYSOCPEN").idDob(outcome.getIdNumber()).creator("iamadmin").created(ZonedDateTime.now()).build());
			
				outcomeErrorsResource.deleteOutcomeError(outcomeError.getId());
			} catch (IllegalStateException e) {
				if (!e.getMessage().equals(BeneficiaryStore.service_not_available))
					outcomeErrorsResource.deleteOutcomeError(outcomeError.getId());
			} catch (Exception e) {
				logger.error("Exception", e);
			}
        });
	}
}
