package prosense.sassa.srdeft.payday.boundary;

import com.fasterxml.jackson.databind.node.ObjectNode;

import org.apache.commons.lang3.StringUtils;

import prosense.sassa.srdeft.api.entity.AppException;
import prosense.sassa.srdeft.payday.entity.PayDay;
import prosense.sassa.srdeft.payday.control.PayDayAgent;

import javax.ejb.Stateless;

import javax.inject.Inject;

import javax.ws.rs.*;
import javax.ws.rs.core.*;


@Stateless
@Path("paydays")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PayDaysResource {
    @Inject
    PayDayAgent payDayAgent;

    @POST
    public Response evaluatePayDay(ObjectNode objectNode, @Context UriInfo uriInfo, @HeaderParam("User") String user) {
        if (StringUtils.isEmpty(user))
            throw AppException.builder().badRequest400().message("user mandatory").build();
		payDayAgent.validate(objectNode);
        PayDay payDay = payDayAgent.forEvaluate(objectNode, user);
        payDayAgent.validateEvaluate(payDay);
        payDay = payDayAgent.evaluate(payDay);
        return Response.ok(payDayAgent.toMap(payDay)).build();
    }
}
