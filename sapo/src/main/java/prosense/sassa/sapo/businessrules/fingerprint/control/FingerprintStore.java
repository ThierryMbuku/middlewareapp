package prosense.sassa.sapo.businessrules.fingerprint.control;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MultivaluedMap;

import org.slf4j.Logger;

import prosense.sassa.sapo.api.entity.AppError;
import prosense.sassa.sapo.api.entity.AppException;
import prosense.sassa.sapo.beneficiaryapi.entity.Fingerprint;
import prosense.sassa.sapo.beneficiaryapi.entity.Party;
import prosense.sassa.sapo.beneficiaryapi.entity.Outcome;

@Dependent
public class FingerprintStore {
    @Inject
    Logger logger;

//     private static final String SERVER = "http://10.117.123.20:7001";
    private static final String SERVER = "http://localhost:7003";
    private static final String API = "/beneficiary/api";
    private static final String FINGERPRINTS_RESOURCE = "fingerprints";
    private static final String PARTIES_RESOURCE = "parties";
    private static final String OUTCOMES_RESOURCE = "outcomes";

    public Set<Fingerprint> search(final MultivaluedMap<String, String> queryParameters) {
        final Optional<String> idNumberOptional = Optional.ofNullable(queryParameters.getFirst("idNumber"));
        Set<Fingerprint> randomFingerprints = null;
        if (idNumberOptional.isPresent()) {
        	Set<Party> parties = findPartiesByIdNumber(Long.valueOf(idNumberOptional.get()));
        	if (parties != null && parties.size() > 0){
		        Party party = parties.iterator().next();
        		logger.info("party id :: " + party.getId());

				Set<Outcome> outcomes = findOutcomesByIdNumber(Long.valueOf(idNumberOptional.get()));
				if (outcomes != null && outcomes.size() > 0){
					boolean method = false;
					for (Outcome outcome : outcomes) {
						logger.info("outcome.getMethod() :: " + outcome.getMethod());
						if (outcome.getMethod() != null && (outcome.getMethod().equals("ATM/POS") || outcome.getMethod().equals("PIN"))) {
							method = true;
							break;
						}
					}
					if (!method) {
						throw AppException.builder().notFound404().message("Incorrect payment method").build();
					}
				} else {
					throw AppException.builder().notFound404().message("Enrolment incomplete").build();
				}

	        	Set<Fingerprint> fingerprints = findFingerprintsByPartyId(party.getId());
	        	if (fingerprints != null && fingerprints.size() > 0) {
					randomFingerprints = fingerprints.stream()
										.filter(n -> n.getGrade() != null && (n.getGrade().equals("primary") || n.getGrade().equals("secondary")))
										.collect(Collectors.toCollection(LinkedHashSet::new));
					List<Fingerprint> fingerprintList = new ArrayList<>(fingerprints);
					Collections.shuffle(fingerprintList);
					fingerprintList.stream()
					.filter(n -> n.getGrade() == null)
					.limit(4 - randomFingerprints.size())
					.forEach(randomFingerprints::add);
	        	} else {
			    	throw AppException.builder().notFound404().message("Enrolment complete – no fingerprints").build();
	        	}
		    } else {
		    	throw AppException.builder().notFound404().message("ID number not found").build();
		    }
        } else {
        	throw AppException.builder().unprocessableEntity422().message("ID number mandatory").build();
        }
		
		if (randomFingerprints != null && randomFingerprints.size() > 0) {
	        return randomFingerprints;
        } else {
        	throw AppException.builder().notFound404().message("no fingerprints available").build();
        }
    }

    public Set<Party> findPartiesByIdNumber(Long idNumber) {
        Client client = ClientBuilder.newClient();
        Response response = client.target(SERVER)
                                  .path(API)
                                  .path(PARTIES_RESOURCE)
                                  .queryParam("idNumber", String.valueOf(idNumber))
                                  .queryParam("_exacts", "idNumber")
                                  .request(MediaType.APPLICATION_JSON)
                                  .header("User", "admin")
                                  .get();
        if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
            throw AppException.builder().status(response.getStatusInfo()).messages(response.readEntity(AppError.class).messages).build();
        }
        Set<Party> parties = response.readEntity(new GenericType<Set<Party>>() {});
        return parties;
    }

    public Set<Outcome> findOutcomesByIdNumber(Long idNumber) {
        Client client = ClientBuilder.newClient();
        Response response = client.target(SERVER)
                                  .path(API)
                                  .path(OUTCOMES_RESOURCE)
                                  .queryParam("idNumber", String.valueOf(idNumber))
                                  .queryParam("createdFrom", ZonedDateTime.now().minusDays(30).format(DateTimeFormatter.ISO_INSTANT))
                                  .queryParam("createdTo", ZonedDateTime.now().plusDays(1).format(DateTimeFormatter.ISO_INSTANT))
                                  .queryParam("_exacts", "idNumber")
                                  .request(MediaType.APPLICATION_JSON)
                                  .header("User", "admin")
                                  .get();
        if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
            throw AppException.builder().status(response.getStatusInfo()).messages(response.readEntity(AppError.class).messages).build();
        }
        Set<Outcome> outcomes = response.readEntity(new GenericType<Set<Outcome>>() {});
        return outcomes;
    }

    public Set<Fingerprint> findFingerprintsByPartyId(Long partyId) {
        Client client = ClientBuilder.newClient();
        Response response = client.target(SERVER)
                                  .path(API)
                                  .path(FINGERPRINTS_RESOURCE)
                                  .queryParam("party", String.valueOf(partyId))
                                  .request(MediaType.APPLICATION_JSON)
                                  .header("User", "admin")
                                  .get();
        if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
            throw AppException.builder().status(response.getStatusInfo()).messages(response.readEntity(AppError.class).messages).build();
        }
        Set<Fingerprint> fingerprints = response.readEntity(new GenericType<Set<Fingerprint>>() {});
        return fingerprints;
    }
}