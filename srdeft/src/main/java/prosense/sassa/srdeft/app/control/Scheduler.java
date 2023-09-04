package prosense.sassa.srdeft.app.control;

import org.slf4j.Logger;

import javax.inject.Inject;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.quartz.JobDetail;
import org.quartz.JobBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.CronScheduleBuilder;
import org.quartz.impl.StdSchedulerFactory;

import prosense.sassa.srdeft.api.control.App;
import prosense.sassa.srdeft.api.control.Property;
import prosense.sassa.srdeft.batchcovjob.boundary.BatchCovJobsResource;
import prosense.sassa.srdeft.batchcovjob.control.BatchCovJobScheduler;


@Singleton
@Startup
public class Scheduler {
    @Inject
    @App
    Logger logger;

    @Inject
    @Property
    private String batchcovjob_time;

    @Inject
    BatchCovJobsResource batchCovJobsResource;
    
    private org.quartz.Scheduler scheduler;

    @PostConstruct
    public void scheduleJobs(){
        try {
			scheduler = new StdSchedulerFactory().getScheduler();
			JobDetail job = JobBuilder.newJob(BatchCovJobScheduler.class).withIdentity("BatchCovJobScheduler", "group1").build();
			job.getJobDataMap().put("logger", logger);
			job.getJobDataMap().put("batchCovJobsResource", batchCovJobsResource);
			Trigger trigger = TriggerBuilder.newTrigger().withIdentity("BatchCovJobSchedulerTrigger", "group1").withSchedule(CronScheduleBuilder.cronSchedule(batchcovjob_time)).build();
			scheduler.scheduleJob(job, trigger);
			scheduler.start();
        } catch (Exception e) {
            logger.error("Exception", e);
        }
    }
    
    @PreDestroy
    public void stopJobs() {
        if (scheduler != null) {
            try {
                scheduler.shutdown(false);
            } catch (Exception e) {
                logger.error("Exception", e);
            }
        }
    }
}
