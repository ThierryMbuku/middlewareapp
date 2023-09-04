package prosense.sassa.srdclient.queue;

import javax.jms.JMSContext;

import javax.inject.Inject;
import javax.annotation.PostConstruct;

import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.slf4j.Logger;

import prosense.sassa.srdclient.queue.sarscompliance.control.SarsComplianceResponseBroker;

import prosense.sassa.srdclient.api.control.Context;
import prosense.sassa.srdclient.mqevent.boundary.MQEventsResource;
import prosense.sassa.srdclient.sarscompliance.control.SarsComplianceStore;

@Singleton
@Startup
public class Setup {
    @Inject
    Logger logger;

	@Inject
	@Context
    JMSContext jmsContext;

    @Inject
    MQEventsResource mqeventsResource;

    @Inject
    SarsComplianceStore sarsComplianceStore;

    @PostConstruct
    public void init(){
        try {
			new SarsComplianceResponseBroker(jmsContext.createContext(JMSContext.AUTO_ACKNOWLEDGE), logger, mqeventsResource, sarsComplianceStore);
        } catch (Exception e) {
            logger.error("Exception", e);
        }
    }
}
