package prosense.sassa.sapo.verification.boundary;

import com.fasterxml.jackson.databind.node.ObjectNode;

import com.google.common.collect.ImmutableSet;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.slf4j.Logger;

import prosense.sassa.sapo.verification.entity.Verification;
import prosense.sassa.sapo.verification.control.VerificationAgent;
import prosense.sassa.sapo.verification.control.VerificationStore;

import prosense.sassa.sapo.api.control.App;
import prosense.sassa.sapo.api.entity.AppException;

@Stateless
@Path("verifications")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VerificationsResource {
    @Inject
    @App
    Logger logger;

    @Inject
    VerificationAgent verificationAgent;

    @Inject
    VerificationStore verificationStore;

    @POST
    public Response createVerification(ObjectNode objectNode, @Context UriInfo uriInfo, @HeaderParam("User") String loginUser) {
        if (loginUser == null || loginUser == "") {
            throw AppException.builder()
                              .badRequest400()
                              .message("user mandatory")
                              .build();
        }
        verificationAgent.validate(objectNode);
        Verification verification = verificationAgent.forCreate(objectNode);
        verificationAgent.validateCreate(verification);
        verification = verificationStore.create(verification, loginUser);
        return Response.created(uriInfo.getAbsolutePathBuilder()
                                       .path(verification.getIdNumber())
                                       .build())
                       .entity(verificationAgent.toFilteredMap(verification, ImmutableSet.of(VerificationAgent.idNumber, VerificationAgent.fullname, VerificationAgent.surname, VerificationAgent.transaction, VerificationAgent.status, VerificationAgent.code, VerificationAgent.description)))
                       .build();
    }
}
