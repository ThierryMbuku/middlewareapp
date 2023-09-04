package prosense.sassa.mqclient.beneficiary.jobs;

import java.util.Set;

import org.slf4j.Logger;

import javax.ws.rs.core.*;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import prosense.sassa.mqclient.api.control.App;
import prosense.sassa.mqclient.beneficiary.entity.OutcomeError;
import prosense.sassa.mqclient.beneficiary.boundary.OutcomeErrorsResource;
import prosense.sassa.mqclient.beneficiary.control.BeneficiaryStore;

public class ExpiredOutcome implements Job {
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        Logger logger = (Logger)dataMap.get("logger");
        OutcomeErrorsResource outcomeErrorsResource = (OutcomeErrorsResource)dataMap.get("outcomeErrorsResource");
        BeneficiaryStore beneficiaryStore = (BeneficiaryStore)dataMap.get("beneficiaryStore");
        
        Set<OutcomeError> outcomeErrors = outcomeErrorsResource.getPendingOutcomes();
        outcomeErrors.forEach(outcomeError -> {
        	outcomeError.setMessageStatus("expired");
        	outcomeError.setUpdator("iamadmin");
			outcomeErrorsResource.updateOutcomeError(outcomeError);
        });
	}
}
