package prosense.sassa.oaamwrapper;

import javax.inject.Inject;
import javax.enterprise.inject.Produces;
import javax.annotation.PostConstruct;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Schedule;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;

import javax.naming.InitialContext;

import prosense.sassa.oaamwrapper.app.control.App;
import prosense.sassa.oaamwrapper.api.control.Context;
import prosense.sassa.oaamwrapper.transaction.control.TransactionJMSConsumer;
import prosense.sassa.oaamwrapper.transaction.control.OaamTransactionStore;

@Singleton
@Startup
public class Setup {
    @Inject
    Logger logger;

    @Inject
    ObjectMapper objectMapper;

	@Inject
	@Context
    InitialContext initialContext;

    @Inject
    OaamTransactionStore oaamTransactionStore;

    @PostConstruct
    private void init(){
        try {
			TransactionJMSConsumer transactionJMSConsumer = new TransactionJMSConsumer(initialContext, logger, objectMapper, oaamTransactionStore);
        } catch (Exception e) {
            logger.error("Exception", e);
        }
    }

	@Schedule(hour = "0", persistent = false)
    private void refreshOaamSession(){
        try {
			App.oaamSession();
        } catch (Exception e) {
            logger.error("Exception", e);
        }
    }
}
