package prosense.sassa.oaamwrapper.transaction.control;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.bharosa.vcrypt.common.util.VCryptResponse;
import com.bharosa.vcrypt.common.util.VCryptObjectResponse;
import com.bharosa.vcrypt.common.util.TransactionResponse;
import com.bharosa.vcrypt.tracker.util.VCryptTrackerUtil;
import com.bharosa.vcrypt.tracker.data.TransactionCreateRequestData;
import com.bharosa.vcrypt.tracker.intf.VCryptRulesResult;

import org.slf4j.Logger;

import com.fasterxml.jackson.databind.node.ObjectNode;

import prosense.sassa.oaamwrapper.app.control.App;
import prosense.sassa.oaamwrapper.transaction.entity.Transaction;


@Dependent
public class OaamTransactionStore {
    @Inject
    Logger logger;

    @Inject
    TransactionAgent transactionAgent;
    
    private static final String TRANSACTION_DEF_KEY = "nrpT";
    private static final String EXTERNAL_TRANSACTION_ID = null;
    private static final Integer TRANSACTION_STATUS = new Integer(1);
    private static List<Integer> POST_TRANSACTION_RUNTIME_LIST = Collections.singletonList(new Integer(80));
    
    public void createTransaction(ObjectNode objectNode) throws Exception {
        Transaction transaction = transactionAgent.forCreate(objectNode);
        
        Map transactionParameterMap = new HashMap();        
        transactionParameterMap.put("Transaction.ID", transaction.getId());
        transactionParameterMap.put("Transaction.DomainUser", transaction.getDomainUser());
        transactionParameterMap.put("Transaction.SocpenUser", transaction.getSocpenUser());
        transactionParameterMap.put("Transaction.Content", transaction.getContent());
        transactionParameterMap.put("Transaction.Detail", transaction.getDetail());
        transactionParameterMap.put("Transaction.Type", transaction.getType());
        transactionParameterMap.put("Transaction.Challenge", transaction.getChallenge());
        transactionParameterMap.put("Transaction.State", transaction.getState());
        transactionParameterMap.put("Transaction.Policy", transaction.getPolicy());
        transactionParameterMap.put("Transaction.Status", transaction.getStatus());
        transactionParameterMap.put("Transaction.Creator", transaction.getCreator());
        transactionParameterMap.put("Transaction.Updator", transaction.getUpdator());

        logger.info("session id -- " + App.sessionId);
        logger.info("transactionParameterMap -- " + transactionParameterMap);

        TransactionCreateRequestData request = new TransactionCreateRequestData(App.sessionId, new Date(),
																				   TRANSACTION_DEF_KEY, EXTERNAL_TRANSACTION_ID,
																				   TRANSACTION_STATUS, transactionParameterMap);
        VCryptResponse response = VCryptTrackerUtil.getVCryptTrackerInstance().createTransaction(request);
        logger.info("transaction response -- " + response);
        
        TransactionResponse transResponse = response.getTransactionResponse();
	    Long transId = null;
    	if (transResponse != null) {
			transId = transResponse.getTransactionId();
	    }
        VCryptRulesResult ruleResult = VCryptTrackerUtil.getVCryptRulesEngineInstance().processRules(App.sessionId, transId, EXTERNAL_TRANSACTION_ID, null, 
        																								POST_TRANSACTION_RUNTIME_LIST, transactionParameterMap);
        logger.info("rule result -- " + ruleResult);
    }
}
