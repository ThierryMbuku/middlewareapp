package prosense.sassa.mqclient.beneficiary.control;

import java.time.ZonedDateTime;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import java.util.Arrays;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;

import prosense.sassa.mqclient.beneficiary.entity.Outcome;
import prosense.sassa.mqclient.beneficiary.entity.Quad;
import prosense.sassa.mqclient.api.control.Property;
import prosense.sassa.mqclient.api.entity.AppException;
import prosense.sassa.mqclient.api.entity.AppError;
import prosense.sassa.mqclient.beneficiary.entity.OutcomeError;
import prosense.sassa.mqclient.beneficiary.boundary.OutcomeErrorsResource;

@Dependent
public class BeneficiaryStore {
    private static final String outcomes_resource = "outcomes";
    private static final String quads_resource = "quads";
    private static final String matchOutcome_resource = "matchOutcome";
    public static final String service_not_available = "service not available";

    @Inject
    Logger logger;

    @Inject
    @Property
    private String nrp_server;

    @Inject
    @Property
    private String nrp_user;

    @Inject
    @Property
    private String beneficiary_api;

    @Inject
    OutcomeErrorsResource outcomeErrorsResource;

    public Outcome createOutcome(Outcome outcome, boolean retry) throws Exception {
        Client client = null;
        
        if (outcome.getApplication() == null) {
        	client = ClientBuilder.newClient();
			Response response = client.target(nrp_server)
									  .path(beneficiary_api)
									  .path("applications")
									  .path(outcome.getIdNumber())
									  .path(matchOutcome_resource)
									  .request(MediaType.APPLICATION_JSON)
									  .header("User", nrp_user)
									  .get();
			if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
				try {
					createOutcomeError(outcome, Integer.toString(response.getStatusInfo().getStatusCode()), String.join(",",response.readEntity(AppError.class).messages), matchOutcome_resource, null);
				} catch (Exception e) {
					if (!retry)
						createOutcomeError(outcome, null, service_not_available, matchOutcome_resource, "pending");
					throw AppException.builder().status(response.getStatusInfo()).messages(Arrays.asList(service_not_available)).build();
				}
				throw AppException.builder().status(response.getStatusInfo()).messages(response.readEntity(AppError.class).messages).build();
			}
			outcome.setApplication(response.readEntity(Outcome.class).getApplication());
        }
        
        client = ClientBuilder.newClient();
        Response response = client.target(nrp_server)
                                  .path(beneficiary_api)
                                  .path(outcomes_resource)
                                  .request(MediaType.APPLICATION_JSON)
                                  .header("User", nrp_user)
                                  .post(Entity.entity(outcome, MediaType.APPLICATION_JSON_TYPE));
		if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
			try {
				createOutcomeError(outcome, Integer.toString(response.getStatusInfo().getStatusCode()), String.join(",",response.readEntity(AppError.class).messages), outcomes_resource, null);
			} catch (Exception e) {
				if (!retry)
					createOutcomeError(outcome, null, service_not_available, outcomes_resource, "pending");
				throw AppException.builder().status(response.getStatusInfo()).messages(Arrays.asList(service_not_available)).build();
			}
			throw AppException.builder().status(response.getStatusInfo()).messages(response.readEntity(AppError.class).messages).build();
		}
        outcome = response.readEntity(Outcome.class);
        return outcome;
    }

    public Quad createQuad(Quad quad) throws Exception {
        Client client = ClientBuilder.newClient();
        Response response = client.target(nrp_server)
                                  .path(beneficiary_api)
                                  .path(quads_resource)
                                  .request(MediaType.APPLICATION_JSON)
                                  .header("User", nrp_user)
                                  .post(Entity.entity(quad, MediaType.APPLICATION_JSON_TYPE));
        quad = response.readEntity(Quad.class);
        return quad;
    }

    public void createOutcomeError(Outcome outcome, String errorCode, String errorMessage, String api, String messageStatus) throws Exception {
		outcomeErrorsResource.createOutcomeError(OutcomeError.builder().application(outcome.getApplication()).idNumber(outcome.getIdNumber()).socpenUser(outcome.getSocpenUser())
								.office(outcome.getOffice()).socpenTime(outcome.getSocpenTime()).method(outcome.getMethod()).ref(outcome.getRef())
								.type1(outcome.getType1()).type2(outcome.getType2()).type3(outcome.getType3()).type4(outcome.getType4())
								.status1(outcome.getStatus1()).status2(outcome.getStatus2()).status3(outcome.getStatus3()).status4(outcome.getStatus4())
								.reason1(outcome.getReason1()).reason2(outcome.getReason2()).reason3(outcome.getReason3()).reason4(outcome.getReason4())
								.messageId(outcome.getMessageId()).errorCode(errorCode).errorMessage(errorMessage).rawMessage(outcome.getRawMessage())
								.api(api).messageStatus(messageStatus).creator(nrp_user).created(ZonedDateTime.now()).build());
    }
}
