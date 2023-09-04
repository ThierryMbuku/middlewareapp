package prosense.sassa.srdeft.batchcovjob.control;

import org.slf4j.Logger;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import prosense.sassa.srdeft.batchcovjob.boundary.BatchCovJobsResource;


public class BatchCovJobScheduler implements Job {
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        Logger logger = (Logger)dataMap.get("logger");
        BatchCovJobsResource batchCovJobsResource = (BatchCovJobsResource)dataMap.get("batchCovJobsResource");
    	logger.info("begin");
        batchCovJobsResource.startScheduledJob();
    	logger.info("end");
	}
}
