package prosense.sassa.mqclient.queue;

import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;

import javax.jms.JMSContext;

import javax.inject.Inject;
import javax.enterprise.inject.Produces;
import javax.annotation.PostConstruct;

import javax.ejb.Singleton;
import javax.ejb.Startup;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import prosense.sassa.mqclient.queue.satransref.control.SatransRefBroker;
import prosense.sassa.mqclient.queue.sabenefstats.control.SABenefStatsBroker;
import prosense.sassa.mqclient.queue.sabenefgenid.control.SABenefGenIdBroker;
import prosense.sassa.mqclient.queue.qmstatsresp.control.QMStatsRespBroker;

import prosense.sassa.mqclient.api.control.App;
import prosense.sassa.mqclient.api.control.Context;
import prosense.sassa.mqclient.api.control.Property;
import prosense.sassa.mqclient.transaction.control.TransactionStore;
import prosense.sassa.mqclient.beneficiary.control.BeneficiaryStore;
import prosense.sassa.mqclient.mqevent.boundary.MQEventsResource;
import prosense.sassa.mqclient.beneficiary.control.OutcomeAgent;
import prosense.sassa.mqclient.beneficiary.control.QuadAgent;
import prosense.sassa.mqclient.beneficiary.jobs.PendingOutcome;
import prosense.sassa.mqclient.beneficiary.jobs.ExpiredOutcome;
import prosense.sassa.mqclient.beneficiary.boundary.OutcomeErrorsResource;

@Singleton
@Startup
public class Setup {
    @Inject
    Logger logger;

    @Inject
    ObjectMapper objectMapper;

	@Inject
	@Context
    JMSContext jmsContext;

    @Inject
    TransactionStore transactionStore;

    @Inject
    BeneficiaryStore beneficiaryStore;

    @Inject
    MQEventsResource mqeventsResource;

    @Inject
    OutcomeAgent outcomeAgent;
    
    @Inject
    QuadAgent quadAgent;
    
    @Inject
    OutcomeErrorsResource outcomeErrorsResource;
    
    @Inject
    @Property
    private String pending_outcome_freq;

    @Inject
    @Property
    private String expired_outcome_time;

    @PostConstruct
    public void init(){
        try {
			SatransRefBroker satransRefBroker = new SatransRefBroker(jmsContext.createContext(JMSContext.AUTO_ACKNOWLEDGE), logger, objectMapper, transactionStore, mqeventsResource);
			SABenefStatsBroker saBenefStatsBroker = new SABenefStatsBroker(jmsContext.createContext(JMSContext.AUTO_ACKNOWLEDGE), logger, objectMapper, beneficiaryStore, outcomeAgent, mqeventsResource);
			SABenefGenIdBroker saBenefGenIdBroker = new SABenefGenIdBroker(jmsContext.createContext(JMSContext.AUTO_ACKNOWLEDGE), logger, objectMapper, beneficiaryStore, quadAgent, mqeventsResource);
			QMStatsRespBroker qmStatsRespBroker = new QMStatsRespBroker(jmsContext.createContext(JMSContext.AUTO_ACKNOWLEDGE), logger, mqeventsResource);
			scheduler();
        } catch (Exception e) {
            logger.error("Exception", e);
        }
    }
    
    private void scheduler() throws SchedulerException {
		Scheduler scheduler = new StdSchedulerFactory().getScheduler();
		JobDetail job = JobBuilder.newJob(PendingOutcome.class).withIdentity("PendingOutcomeJob", "group1").build();
		job.getJobDataMap().put("logger", logger);
		job.getJobDataMap().put("outcomeErrorsResource", outcomeErrorsResource);
		job.getJobDataMap().put("beneficiaryStore", beneficiaryStore);
		job.getJobDataMap().put("mqeventsResource", mqeventsResource);
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity("PendingOutcomeJobTrigger", "group1")
										.withSchedule(CronScheduleBuilder.cronSchedule(pending_outcome_freq)).build();
		scheduler.scheduleJob(job, trigger);

		job = JobBuilder.newJob(ExpiredOutcome.class).withIdentity("ExpiredOutcomeJob", "group1").build();
		job.getJobDataMap().put("logger", logger);
		job.getJobDataMap().put("outcomeErrorsResource", outcomeErrorsResource);
		job.getJobDataMap().put("beneficiaryStore", beneficiaryStore);
		trigger = TriggerBuilder.newTrigger().withIdentity("ExpiredOutcomeJobTrigger", "group1")
										.withSchedule(CronScheduleBuilder.cronSchedule(expired_outcome_time)).build();
		scheduler.scheduleJob(job, trigger);

		scheduler.start();
     }
}
