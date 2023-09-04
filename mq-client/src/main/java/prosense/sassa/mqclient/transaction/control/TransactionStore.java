package prosense.sassa.mqclient.transaction.control;

import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.enterprise.context.Dependent;

import javax.inject.Inject;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.MultivaluedHashMap;

import org.slf4j.Logger;

import prosense.sassa.mqclient.transaction.entity.Transaction;
import prosense.sassa.mqclient.api.control.Property;

@Dependent
public class TransactionStore {
    private static final String resource = "transactions";

    @Inject
    Logger logger;

    @Inject
    TransactionAgent transactionAgent;

    @Inject
    @Property
    private String nrp_server;

    @Inject
    @Property
    private String nrp_api;

    @Inject
    @Property
    private String nrp_user;

    @Inject
    @Property
    private String nrp_api_key;
    
    @Inject
    @Property
    private String oaam_wrapper_api;
    
    public Transaction createTransaction(ObjectNode objectNode, String correlation) throws Exception {
		Transaction transaction = transactionAgent.forCreate(objectNode);
        
        transaction.setCorrelation(correlation);
        Client client = ClientBuilder.newClient();

		MultivaluedMap<String, Object> httpHeaders = new MultivaluedHashMap<>();
       	httpHeaders.add("User", nrp_user);
       	httpHeaders.add("x-api-key", nrp_api_key);
       	Response response = client.target(nrp_server)
                                 .path(nrp_api)
                                 .path(resource)
                                 .request(MediaType.APPLICATION_JSON)
                                 .headers(httpHeaders)
                                 .post(Entity.entity(transaction, MediaType.APPLICATION_JSON_TYPE));
       	Transaction createdTransaction = response.readEntity(Transaction.class);
       	return createdTransaction;
    }

    public Transaction createOaamTransaction(Transaction transaction) throws Exception {
        Client client = ClientBuilder.newClient();
        Response response = client.target(nrp_server)
                                  .path(oaam_wrapper_api)
                                  .path(resource)
                                  .request(MediaType.APPLICATION_JSON)
                                  .post(Entity.entity(transaction, MediaType.APPLICATION_JSON_TYPE));
        Transaction createdTransaction = response.readEntity(Transaction.class);
        return createdTransaction;
    }
}
