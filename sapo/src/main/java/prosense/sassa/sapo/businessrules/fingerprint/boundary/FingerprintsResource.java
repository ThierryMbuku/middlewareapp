package prosense.sassa.sapo.businessrules.fingerprint.boundary;

import com.fasterxml.jackson.databind.node.ObjectNode;

import com.google.common.base.Splitter;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.slf4j.Logger;

import prosense.sassa.sapo.beneficiaryapi.entity.Fingerprint;
import prosense.sassa.sapo.businessrules.fingerprint.control.FingerprintAgent;
import prosense.sassa.sapo.businessrules.fingerprint.control.FingerprintStore;
import prosense.sassa.sapo.api.control.App;

@Stateless
@Path("businessrules/fingerprints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FingerprintsResource {
    @Inject
    @App
    Logger logger;

    @Inject
    FingerprintAgent fingerprintAgent;

    @Inject
    FingerprintStore fingerprintStore;

    @GET
    public Response readFingerprints(@Context UriInfo uriInfo) {
        final MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
//         final Optional<Iterable<String>> filterOptional = Optional.ofNullable(queryParameters.getFirst("_fields")).map(m -> Splitter.on(',').omitEmptyStrings().trimResults().split(m));
        final Optional<Iterable<String>> filterOptional = Optional.ofNullable("image,hand,finger").map(m -> Splitter.on(',').omitEmptyStrings().trimResults().split(m));
        return Response.ok(fingerprintStore.search(queryParameters)
                                                .stream()
                                                .map(fingerprint -> filterOptional.isPresent() ? fingerprintAgent.toFilteredMap(fingerprint, filterOptional.get()) : fingerprintAgent.toMap(fingerprint))
                                                .collect(Collectors.toCollection(LinkedHashSet::new))).build();
    }
}
