package prosense.sassa.srdclient.sarscompliance.boundary;

import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.ejb.Stateless;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import javax.inject.Inject;

import org.slf4j.Logger;

import prosense.sassa.srdclient.queue.sarscompliance.control.SarsComplianceRequestBroker;
import prosense.sassa.srdclient.sarscompliance.entity.SarsCompliance;
import prosense.sassa.srdclient.sarscompliance.control.SarsComplianceAgent;
import prosense.sassa.srdclient.api.control.App;

@Stateless
@Path("sarscompliances")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class SarsCompliancesResource {
    @Inject
    @App
    Logger logger;

    @Inject
    SarsComplianceAgent sarsComplianceAgent;

    @Inject
    SarsComplianceRequestBroker sarsComplianceRequestBroker;

    @POST
    public Response enquireSarsCompliance(ObjectNode objectNode, @Context UriInfo uriInfo) {
        sarsComplianceAgent.validate(objectNode);
        SarsCompliance sarsCompliance = sarsComplianceAgent.forEnquire(objectNode);
        sarsComplianceAgent.validateEnquire(sarsCompliance);
		sarsComplianceRequestBroker.sendSarsComplianceRequestMessage(sarsCompliance);
        return Response.created(uriInfo.getAbsolutePathBuilder()
                                       .path(String.valueOf(sarsCompliance.getOutcomeId()))
                                       .build())
                       .entity(sarsComplianceAgent.toMap(sarsCompliance))
                       .build();
    }
}
