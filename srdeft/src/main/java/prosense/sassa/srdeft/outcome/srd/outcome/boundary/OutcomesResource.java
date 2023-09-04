package prosense.sassa.srdeft.outcome.srd.boundary;

import prosense.sassa.srdeft.outcome.srd.entity.Outcome;
import prosense.sassa.srdeft.outcome.srd.control.OutcomeStore;
import prosense.sassa.srdeft.covjob.entity.CovJob;
import prosense.sassa.srdeft.file.control.FileAgent;

import javax.ejb.Stateless;

import javax.inject.Inject;

import java.util.Set;

import java.time.ZonedDateTime;


@Stateless
public class OutcomesResource {
    @Inject
    OutcomeStore outcomeStore;

    @Inject
    FileAgent fileAgent;

    public Outcome updateProcessed(long id, boolean processed) {
    	Outcome outcome = outcomeStore.read(id);
    	if (processed)
	        outcome.setProcessed(ZonedDateTime.now());
	    else
	    	outcome.setProcessed(null);
        return outcomeStore.update(outcome);
    }
    
    public Set<Outcome> searchApprovedByCovJob(CovJob covJob, long paySeq) {
        String actionDate = fileAgent.getActionDate("ddMMyyyy", String.valueOf(covJob.getPayDay()) + covJob.getPeriod());
        try {
	        return outcomeStore.search(Outcome.builder().period(covJob.getVerificationPeriod()).outcome("approved").payDay(Long.valueOf(actionDate.substring(0,2))).payMonth(Long.valueOf(actionDate.substring(2,4))).payYear(Long.valueOf(actionDate.substring(4,8))).paySeq(paySeq).build());
	    } catch(Exception e) {
	        return outcomeStore.search(Outcome.builder().period(covJob.getVerificationPeriod()).outcome("approved").payDay(Long.valueOf(actionDate.substring(0,2))).payMonth(Long.valueOf(actionDate.substring(2,4))).payYear(Long.valueOf(actionDate.substring(4,8))).paySeq(paySeq).build());
	    }
    }
}
