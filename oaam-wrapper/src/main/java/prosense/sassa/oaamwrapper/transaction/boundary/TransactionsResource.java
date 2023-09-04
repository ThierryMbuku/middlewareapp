package prosense.sassa.oaamwrapper.transaction.boundary;

import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.ejb.Stateless;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.slf4j.Logger;

import prosense.sassa.oaamwrapper.transaction.control.TransactionJMSProducer;
import prosense.sassa.oaamwrapper.transaction.entity.Transaction;
import prosense.sassa.oaamwrapper.transaction.control.TransactionAgent;
import prosense.sassa.oaamwrapper.api.control.App;

@Stateless
@Path("transactions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TransactionsResource {
    @Inject
    @App
    Logger logger;

    @Inject
    TransactionAgent transactionAgent;

    @Inject
    TransactionJMSProducer transactionJMSProducer;

    @POST
    public Response createTransaction(ObjectNode objectNode, @Context UriInfo uriInfo) {
        transactionAgent.validate(objectNode);
        Transaction transaction = transactionAgent.forCreate(objectNode);
        transactionAgent.validateCreate(transaction);
		transactionJMSProducer.send(transaction);
        return Response.created(uriInfo.getAbsolutePathBuilder()
                                       .path(String.valueOf(transaction.getId()))
                                       .build())
                       .entity(transactionAgent.toMap(transaction))
                       .build();
    }
}
