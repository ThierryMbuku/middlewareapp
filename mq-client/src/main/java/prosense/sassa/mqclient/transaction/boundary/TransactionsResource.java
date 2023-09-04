package prosense.sassa.mqclient.transaction.boundary;

import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.ejb.Stateless;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.slf4j.Logger;

import prosense.sassa.mqclient.queue.satransstats.control.SatransStatsBroker;
import prosense.sassa.mqclient.transaction.entity.Transaction;
import prosense.sassa.mqclient.transaction.control.TransactionAgent;
import prosense.sassa.mqclient.api.control.App;

@Stateless
@Path("transactions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class TransactionsResource {
    @Inject
    @App
    Logger logger;

    @Inject
    TransactionAgent transactionAgent;

    @Inject
    SatransStatsBroker satransStatsBroker;

    @POST
    public Response createTransaction(ObjectNode objectNode, @Context UriInfo uriInfo) {
        transactionAgent.validate(objectNode);
        Transaction transaction = transactionAgent.forCreate(objectNode);
        transactionAgent.validateCreate(transaction);
		satransStatsBroker.sendSatransStatsMessage(transaction);
        return Response.created(uriInfo.getAbsolutePathBuilder()
                                       .path(String.valueOf(transaction.getId()))
                                       .build())
                       .entity(transactionAgent.toMap(transaction))
                       .build();
    }
}
