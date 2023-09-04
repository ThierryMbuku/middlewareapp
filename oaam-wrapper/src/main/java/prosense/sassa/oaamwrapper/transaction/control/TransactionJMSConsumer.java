package prosense.sassa.oaamwrapper.transaction.control;

import javax.enterprise.context.Dependent;

import javax.naming.InitialContext;

import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import prosense.sassa.oaamwrapper.transaction.entity.Transaction;
import prosense.sassa.oaamwrapper.transaction.control.OaamTransactionStore;


@Dependent
public class TransactionJMSConsumer {
    private static Logger logger;
    private static ObjectMapper objectMapper;
    private static OaamTransactionStore oaamTransactionStore;

	private static final String CONNECTION_FACTORY = "jms/oaamwrapper/JMSConnFactory";
    private static final String QUEUE = "jms/oaamwrapper/TransactionQueue";

    public TransactionJMSConsumer(InitialContext initialContext, Logger logger, ObjectMapper objectMapper, OaamTransactionStore oaamTransactionStore){
		try{
        	this.logger = logger;
        	this.objectMapper = objectMapper;
        	this.oaamTransactionStore = oaamTransactionStore;
    		Queue queue = (Queue)initialContext.lookup(QUEUE);
    		QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory)initialContext.lookup(CONNECTION_FACTORY);
    		QueueConnection conn = queueConnectionFactory.createQueueConnection();
			QueueSession session = conn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			
			TransactionJMSListener recieve = new TransactionJMSListener();
			conn.start();
			MessageConsumer consumer = session.createConsumer(queue);
			consumer.setMessageListener(recieve);
			logger.info("listening queue  -- " + QUEUE);
        } catch (Exception e) {
            logger.error("Exception", e);
        }
	}

    private class TransactionJMSListener implements MessageListener {
		@Override
		public void onMessage(Message message) {
			try {
				String msgText = "nothing";
				if (message instanceof TextMessage) {
					msgText = ((TextMessage) message).getText();
				} else {
					msgText = message.toString();
				}
				logger.info("msgText  -- " + msgText);
				oaamTransactionStore.createTransaction((ObjectNode)objectMapper.readTree(msgText));
		   } catch (Exception e) {
			   logger.error("Exception", e);
		   }
		}
	}
}
