package prosense.sassa.mqclient.beneficiary.boundary;

import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.ejb.Stateless;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.slf4j.Logger;

import prosense.sassa.mqclient.queue.saidcheck.control.SAIDCheckBroker;
import prosense.sassa.mqclient.queue.saidcheckpoa.control.SAIDCheckPOABroker;
import prosense.sassa.mqclient.queue.safinenroll.control.SAFinEnrollBroker;

import prosense.sassa.mqclient.beneficiary.entity.BeneficiaryVerify;
import prosense.sassa.mqclient.beneficiary.control.BeneficiaryVerifyAgent;
import prosense.sassa.mqclient.beneficiary.entity.ProcuratorVerify;
import prosense.sassa.mqclient.beneficiary.control.ProcuratorVerifyAgent;
import prosense.sassa.mqclient.beneficiary.entity.BeneficiaryEnrol;
import prosense.sassa.mqclient.beneficiary.control.BeneficiaryEnrolAgent;
import prosense.sassa.mqclient.api.control.App;

@Stateless
@Path("beneficiary")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BeneficiariesResource {
    @Inject
    @App
    Logger logger;

    @Inject
    BeneficiaryVerifyAgent beneficiaryVerifyAgent;

    @Inject
    ProcuratorVerifyAgent procuratorVerifyAgent;

    @Inject
    BeneficiaryEnrolAgent beneficiaryEnrolAgent;

    @Inject
    SAIDCheckBroker saIDCheckBroker;

    @Inject
    SAIDCheckPOABroker saIDCheckPOABroker;

    @Inject
    SAFinEnrollBroker saFinEnrollBroker;

    @POST
    @Path("ben/verify")
    public Response createBeneficiaryVerify(ObjectNode objectNode, @Context UriInfo uriInfo) {
        beneficiaryVerifyAgent.validate(objectNode);
        BeneficiaryVerify beneficiaryVerify = beneficiaryVerifyAgent.forCreate(objectNode);
        beneficiaryVerifyAgent.validateCreate(beneficiaryVerify);
		saIDCheckBroker.sendMessage(beneficiaryVerify);
        return Response.created(uriInfo.getAbsolutePathBuilder()
                                       .path(String.valueOf(beneficiaryVerify.getApplication()))
                                       .build())
                       .entity(beneficiaryVerifyAgent.toMap(beneficiaryVerify))
                       .build();
    }

    @POST
    @Path("proc/verify")
    public Response createProcuratorVerify(ObjectNode objectNode, @Context UriInfo uriInfo) {
        procuratorVerifyAgent.validate(objectNode);
        ProcuratorVerify procuratorVerify = procuratorVerifyAgent.forCreate(objectNode);
        procuratorVerifyAgent.validateCreate(procuratorVerify);
		saIDCheckPOABroker.sendMessage(procuratorVerify);
        return Response.created(uriInfo.getAbsolutePathBuilder()
                                       .path(String.valueOf(procuratorVerify.getApplication()))
                                       .build())
                       .entity(procuratorVerifyAgent.toMap(procuratorVerify))
                       .build();
    }

    @POST
    @Path("id/enrol")
    public Response createBeneficiaryEnrol(ObjectNode objectNode, @Context UriInfo uriInfo) {
        beneficiaryEnrolAgent.validate(objectNode);
        BeneficiaryEnrol beneficiaryEnrol = beneficiaryEnrolAgent.forCreate(objectNode);
        beneficiaryEnrolAgent.validateCreate(beneficiaryEnrol);
		saFinEnrollBroker.sendMessage(beneficiaryEnrol);
        return Response.created(uriInfo.getAbsolutePathBuilder()
                                       .path(String.valueOf(beneficiaryEnrol.getApplication()))
                                       .build())
                       .entity(beneficiaryEnrolAgent.toMap(beneficiaryEnrol))
                       .build();
    }
}
