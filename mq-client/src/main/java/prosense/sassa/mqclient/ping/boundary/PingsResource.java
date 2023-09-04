package prosense.sassa.mqclient.ping.boundary;

import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.ejb.Stateless;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.slf4j.Logger;

import prosense.sassa.mqclient.queue.qmstats.control.QMStatsBroker;
import prosense.sassa.mqclient.ping.entity.Ping;
import prosense.sassa.mqclient.ping.control.PingAgent;
import prosense.sassa.mqclient.api.control.App;

@Stateless
@Path("pings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PingsResource {
    @Inject
    @App
    Logger logger;

    @Inject
    PingAgent pingAgent;

    @Inject
    QMStatsBroker qmStatsBroker;

    @POST
    public Response createPing(ObjectNode objectNode, @Context UriInfo uriInfo) {
        pingAgent.validate(objectNode);
        Ping ping = pingAgent.forCreate(objectNode);
        pingAgent.validateCreate(ping);
		qmStatsBroker.sendQMStatsMessage(ping);
        return Response.created(uriInfo.getAbsolutePathBuilder()
                                       .path(String.valueOf(ping.getIamPUT()))
                                       .build())
                       .entity(pingAgent.toMap(ping))
                       .build();
    }
}
