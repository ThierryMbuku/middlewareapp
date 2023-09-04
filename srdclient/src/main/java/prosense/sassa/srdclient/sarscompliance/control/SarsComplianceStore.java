package prosense.sassa.srdclient.sarscompliance.control;

import javax.enterprise.context.Dependent;

import javax.inject.Inject;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import prosense.sassa.srdclient.api.control.Property;

@Dependent
public class SarsComplianceStore {
    private static final String resource = "irp5";

    @Inject
    @Property
    private String perm_srd_api;
    
    public String srdCallback(String outcomeId, String irp5FoundInd) throws Exception {
		Client client = ClientBuilder.newClient();
		Response response = client.target(perm_srd_api).path(outcomeId).path(resource).path(irp5FoundInd)
									.request(MediaType.APPLICATION_JSON)
									.header("User", "srdclient")
									.method("PATCH", Entity.json(""));
		if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
			throw new RuntimeException(response.readEntity(String.class));
		}
		return response.readEntity(String.class);
    }
}
