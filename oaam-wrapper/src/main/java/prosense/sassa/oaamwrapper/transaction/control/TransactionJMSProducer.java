package prosense.sassa.oaamwrapper.transaction.control;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import javax.naming.InitialContext;

import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import prosense.sassa.oaamwrapper.api.control.Context;
import prosense.sassa.oaamwrapper.transaction.entity.Transaction;
import prosense.sassa.oaamwrapper.transaction.control.TransactionAgent;


@Dependent
public class TransactionJMSProducer {
    @Inject
    Logger logger;

    @Inject
    ObjectMapper objectMapper;

	@Inject
	@Context
    InitialContext initialContext;

    @Inject
    TransactionAgent transactionAgent;

	private static final String CONNECTION_FACTORY = "jms/oaamwrapper/JMSConnFactory";
    private static final String QUEUE = "jms/oaamwrapper/TransactionQueue";

    public void send(Transaction transaction) {
        try {
    		Queue queue = (Queue)initialContext.lookup(QUEUE);
    		QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory)initialContext.lookup(CONNECTION_FACTORY);
    		QueueConnection conn = queueConnectionFactory.createQueueConnection();
			QueueSession session = conn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			
        	String json = objectMapper.writeValueAsString(transactionAgent.toMap(transaction));
            logger.info("json  -- " + json);
			TextMessage message =  session.createTextMessage(json);

			conn.start();
			session.createSender(queue).send(message);
			conn.close();
        } catch (Exception e) {
            logger.error("Exception", e);
        }
    }
}
