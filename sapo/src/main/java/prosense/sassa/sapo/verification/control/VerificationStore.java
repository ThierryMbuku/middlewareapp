package prosense.sassa.sapo.verification.control;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import java.util.Collections;
import java.util.HashSet;
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
import prosense.sassa.sapo.beneficiaryapi.entity.Application;
import prosense.sassa.sapo.beneficiaryapi.entity.Verification;
import prosense.sassa.sapo.beneficiaryapi.entity.Approval;
import prosense.sassa.sapo.beneficiaryapi.entity.Piva;
import prosense.sassa.sapo.beneficiaryapi.entity.Setting;
import prosense.sassa.sapo.businessrules.fingerprint.control.FingerprintAgent;


@Dependent
public class VerificationStore {
    @Inject
    Logger logger;

    @Inject
    FingerprintAgent fingerprintAgent;

    private static final String SERVER = "http://localhost:7003";
    private static final String API = "/beneficiary/api";
    private static final String PARTIES_RESOURCE = "parties";
    private static final String APPLICATIONS_RESOURCE = "applications";
    private static final String FINGERPRINTS_RESOURCE = "fingerprints";
    private static final String VERIFICATIONS_RESOURCE = "verifications";
    private static final String PIVA_RESOURCE = "/shared/api/piva";
    private static final String SETTINGS_RESOURCE = "/settings";

    public prosense.sassa.sapo.verification.entity.Verification create(prosense.sassa.sapo.verification.entity.Verification verification, String loginUser) {
		logger.info("sapo verification api call for idNumber: " + verification.getIdNumber() + " : start" );
		Fingerprint primary = null, secondary = null;
		Set<Fingerprint> otherFingerprints = new HashSet<Fingerprint>();
		for (Fingerprint fingerprint : verification.getFingerprints()) {
			if (fingerprint.getFinger() != null) {
				if (fingerprint.getFinger().equalsIgnoreCase("thumb")) {
					if (primary == null) {
						primary = fingerprint;
						primary.setGrade("primary");
					} else if (secondary == null){
						secondary = fingerprint;
						secondary.setGrade("secondary");
					}
				}
			}
		}
		for (Fingerprint fingerprint : verification.getFingerprints()) {
			if (fingerprint.getFinger() != null) {
				if (!fingerprint.getFinger().equalsIgnoreCase("thumb")) {
					if (primary == null) {
						primary = fingerprint;
						primary.setGrade("primary");
					} else if (secondary == null){
						secondary = fingerprint;
						secondary.setGrade("secondary");
					} else {
						otherFingerprints.add(fingerprint);
					}
				}
			}
		}
// 		Piva piva = null;
// 		try {
// 			logger.info("sapo piva api call for idNumber: " + verification.getIdNumber() + " : start" );
// 			Setting setting = findSettingByName("ijs", loginUser);
// 			if (setting != null && setting.getValue())
// 				piva = createPiva(Piva.builder().id(verification.getIdNumber()).primary(Fingerprint.builder().hand(primary.getHand()).finger(primary.getFinger()).image(primary.getImage()).build()).secondary(Fingerprint.builder().hand(secondary.getHand()).finger(secondary.getFinger()).image(secondary.getImage()).build()).build(), loginUser);
// 			logger.info("sapo piva api call for idNumber: " + verification.getIdNumber() + " : end" );
// 		} catch (AppException ae) {
// 			logger.error(ae.toString());
// 			logger.error("sapo piva api call for idNumber: " + verification.getIdNumber() + " : exception" );
// 		}
// 		if (piva.getStatus() != null && !piva.getStatus().equalsIgnoreCase("green")) {
// 			logger.info("sapo verification api call for idNumber: " + verification.getIdNumber() + " : end" );
// 	        return prosense.sassa.sapo.verification.entity.Verification.builder().idNumber(verification.getIdNumber()).status(piva.getStatus()).transaction(piva.getTransaction()).build();
// 		}
		
// 		String fullname = "", surname = "";
// 		if (piva != null) {
// 			if (piva.getFullname() != null)
// 				fullname = piva.getFullname();
// 			if (piva.getSurname() != null)
// 				surname = piva.getSurname();
// 		}
		Party party = null;
// 		Set<Party> parties = findPartiesByIdNumberFullnameSurname(verification.getIdNumber(), fullname, surname, loginUser);
		Set<Party> parties = findPartiesByIdNumber(verification.getIdNumber(), loginUser);
		if (parties != null && parties.size() > 0){
			party = parties.iterator().next();
			logger.info("party id :: " + party.getId());
		} else {
			party = createParty(Party.builder().idNumber(verification.getIdNumber()).fullname("unknown").surname("unknown").fingerprinted(false).build(), loginUser);
		}
		Application application = createApplication(Application.builder().party(party.getId()).branch("SAPO").mac("000000").build(), loginUser);
		if (verification.getFingerprints() != null) {
			Set<Fingerprint> fingerprints = findFingerprintsByPartyGrade(party.getId(), "primary", loginUser);
			for (Fingerprint fingerprint : fingerprints) {
				updateFingerprintGradeNull(fingerprint.getId(), loginUser);
			}
			fingerprints = findFingerprintsByPartyGrade(party.getId(), "secondary", loginUser);
			for (Fingerprint fingerprint : fingerprints) {
				updateFingerprintGradeNull(fingerprint.getId(), loginUser);
			}

			fingerprints = findFingerprintsByPartyHandFinger(party.getId(), primary.getHand(), primary.getFinger(), loginUser);
			if (fingerprints != null && fingerprints.size() > 0){
				updateFingerprint(fingerprints.iterator().next().getId(), Fingerprint.builder().grade(primary.getGrade()).template(primary.getTemplate()).image(primary.getImage()).imageType(primary.getImageType()).build(), loginUser);
			} else {
				createFingerprint(Fingerprint.builder().party(party.getId()).hand(primary.getHand()).finger(primary.getFinger()).grade(primary.getGrade()).template(primary.getTemplate()).image(primary.getImage()).imageType(primary.getImageType()).build(), loginUser);
			}
			fingerprints = findFingerprintsByPartyHandFinger(party.getId(), secondary.getHand(), secondary.getFinger(), loginUser);
			if (fingerprints != null && fingerprints.size() > 0){
				updateFingerprint(fingerprints.iterator().next().getId(), Fingerprint.builder().grade(secondary.getGrade()).template(secondary.getTemplate()).image(secondary.getImage()).imageType(secondary.getImageType()).build(), loginUser);
			} else {
				createFingerprint(Fingerprint.builder().party(party.getId()).hand(secondary.getHand()).finger(secondary.getFinger()).grade(secondary.getGrade()).template(secondary.getTemplate()).image(secondary.getImage()).imageType(secondary.getImageType()).build(), loginUser);
			}
			for (Fingerprint fingerprint : otherFingerprints) {
				fingerprints = findFingerprintsByPartyHandFinger(party.getId(), fingerprint.getHand(), fingerprint.getFinger(), loginUser);
				if (fingerprints != null && fingerprints.size() > 0){
					updateFingerprint(fingerprints.iterator().next().getId(), Fingerprint.builder().template(fingerprint.getTemplate()).image(fingerprint.getImage()).imageType(fingerprint.getImageType()).build(), loginUser);
				} else {
					createFingerprint(Fingerprint.builder().party(party.getId()).hand(fingerprint.getHand()).finger(fingerprint.getFinger()).template(fingerprint.getTemplate()).image(fingerprint.getImage()).imageType(fingerprint.getImageType()).build(), loginUser);
				}
			}
		}
		party = updateParty(party.getId(), Party.builder().fingerprinted(true).build(), loginUser);
		Verification beneficiaryVerification = createVerification(Verification.builder().application(application.getId()).build(), loginUser);
		if (beneficiaryVerification.getFullname() != null || beneficiaryVerification.getSurname() != null)
			party = updateParty(party.getId(), Party.builder().fullname(beneficiaryVerification.getFullname()).surname(beneficiaryVerification.getSurname()).build(), loginUser);
		String verificationStatus = beneficiaryVerification.getStatus();
		if ("blue".equalsIgnoreCase(verificationStatus))
			verificationStatus = "green";
		logger.info("sapo verification api call for idNumber: " + verification.getIdNumber() + " : end" );
		return prosense.sassa.sapo.verification.entity.Verification.builder().idNumber(party.getIdNumber()).fullname(party.getFullname()).surname(party.getSurname()).status(verificationStatus).transaction(beneficiaryVerification.getTransaction()).code(beneficiaryVerification.getCode()).description(beneficiaryVerification.getDescription()).build();
    }

    public Party createParty(Party party, String loginUser) {
        Client client = ClientBuilder.newClient();
        Response response = client.target(SERVER)
                                  .path(API)
                                  .path(PARTIES_RESOURCE)
                                  .request(MediaType.APPLICATION_JSON)
                                  .header("User", loginUser)
                                  .post(Entity.entity(party, MediaType.APPLICATION_JSON_TYPE));
        if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
            throw AppException.builder().status(response.getStatusInfo()).messages(response.readEntity(AppError.class).messages).build();
        }
        party = response.readEntity(Party.class);
        return party;
    }

    public Set<Party> findPartiesByIdNumber(String idNumber, String loginUser) {
        Client client = ClientBuilder.newClient();
        Response response = client.target(SERVER)
                                  .path(API)
                                  .path(PARTIES_RESOURCE)
                                  .queryParam(VerificationAgent.idNumber, idNumber)
                                  .queryParam("_exacts", VerificationAgent.idNumber)
                                  .queryParam("_sort", "created")
                                  .request(MediaType.APPLICATION_JSON)
                                  .header("User", loginUser)
                                  .get();
        if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
            throw AppException.builder().status(response.getStatusInfo()).messages(response.readEntity(AppError.class).messages).build();
        }
        Set<Party> parties = response.readEntity(new GenericType<Set<Party>>() {});
        return parties;
    }

    public Set<Party> findPartiesByIdNumberFullnameSurname(String idNumber, String fullname, String surname, String loginUser) {
        Client client = ClientBuilder.newClient();
        Response response = client.target(SERVER)
                                  .path(API)
                                  .path(PARTIES_RESOURCE)
                                  .queryParam(VerificationAgent.idNumber, idNumber)
                                  .queryParam(VerificationAgent.fullname, fullname)
                                  .queryParam(VerificationAgent.surname, surname)
                                  .queryParam("_exacts", VerificationAgent.idNumber)
                                  .request(MediaType.APPLICATION_JSON)
                                  .header("User", loginUser)
                                  .get();
        if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
            throw AppException.builder().status(response.getStatusInfo()).messages(response.readEntity(AppError.class).messages).build();
        }
        Set<Party> parties = response.readEntity(new GenericType<Set<Party>>() {});
        return parties;
    }

    public Party updateParty(Long partyId, Party party, String loginUser) {
        Client client = ClientBuilder.newClient();
        Response response = client.target(SERVER)
                                  .path(API)
                                  .path(PARTIES_RESOURCE + "/" + partyId)
                                  .request(MediaType.APPLICATION_JSON)
                                  .header("User", loginUser)
                                  .method("PATCH", Entity.entity(party, MediaType.APPLICATION_JSON_TYPE));
        if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
            throw AppException.builder().status(response.getStatusInfo()).messages(response.readEntity(AppError.class).messages).build();
        }
        party = response.readEntity(Party.class);
        return party;
    }

    public Application createApplication(Application application, String loginUser) {
        Client client = ClientBuilder.newClient();
        Response response = client.target(SERVER)
                                  .path(API)
                                  .path(APPLICATIONS_RESOURCE)
                                  .request(MediaType.APPLICATION_JSON)
                                  .header("User", loginUser)
                                  .post(Entity.entity(application, MediaType.APPLICATION_JSON_TYPE));
        if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
            throw AppException.builder().status(response.getStatusInfo()).messages(response.readEntity(AppError.class).messages).build();
        }
        application = response.readEntity(Application.class);
        return application;
    }

    public Fingerprint createFingerprint(Fingerprint fingerprint, String loginUser) {
        Client client = ClientBuilder.newClient();
        Response response = client.target(SERVER)
                                  .path(API)
                                  .path(FINGERPRINTS_RESOURCE)
                                  .request(MediaType.APPLICATION_JSON)
                                  .header("User", loginUser)
                                  .post(Entity.entity(fingerprint, MediaType.APPLICATION_JSON_TYPE));
        if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
        	throw AppException.builder().status(response.getStatusInfo()).messages(response.readEntity(AppError.class).messages).build();
        }
        fingerprint = response.readEntity(Fingerprint.class);
        return fingerprint;
    }

    public Set<Fingerprint> findFingerprintsByPartyGrade(Long party, String grade, String loginUser) {
        Client client = ClientBuilder.newClient();
        Response response = client.target(SERVER)
                                  .path(API)
                                  .path(FINGERPRINTS_RESOURCE)
                                  .queryParam(FingerprintAgent.party, party)
                                  .queryParam(FingerprintAgent.grade, grade)
                                  .request(MediaType.APPLICATION_JSON)
                                  .header("User", loginUser)
                                  .get();
        if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
            throw AppException.builder().status(response.getStatusInfo()).messages(response.readEntity(AppError.class).messages).build();
        }
        Set<Fingerprint> fingerprints = response.readEntity(new GenericType<Set<Fingerprint>>() {});
        return fingerprints;
    }

    public Set<Fingerprint> findFingerprintsByPartyHandFinger(Long party, String hand, String finger, String loginUser) {
        Client client = ClientBuilder.newClient();
        Response response = client.target(SERVER)
                                  .path(API)
                                  .path(FINGERPRINTS_RESOURCE)
                                  .queryParam(FingerprintAgent.party, party)
                                  .queryParam(FingerprintAgent.hand, hand)
                                  .queryParam(FingerprintAgent.finger, finger)
                                  .request(MediaType.APPLICATION_JSON)
                                  .header("User", loginUser)
                                  .get();
        if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
            throw AppException.builder().status(response.getStatusInfo()).messages(response.readEntity(AppError.class).messages).build();
        }
        Set<Fingerprint> fingerprints = response.readEntity(new GenericType<Set<Fingerprint>>() {});
        return fingerprints;
    }

    public Fingerprint updateFingerprint(Long fingerprintId, Fingerprint fingerprint, String loginUser) {
        Client client = ClientBuilder.newClient();
        Response response = client.target(SERVER)
                                  .path(API)
                                  .path(FINGERPRINTS_RESOURCE + "/" + fingerprintId)
                                  .request(MediaType.APPLICATION_JSON)
                                  .header("User", loginUser)
                                  .method("PATCH", Entity.entity(fingerprint, MediaType.APPLICATION_JSON_TYPE));
        if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
            throw AppException.builder().status(response.getStatusInfo()).messages(response.readEntity(AppError.class).messages).build();
        }
        fingerprint = response.readEntity(Fingerprint.class);
        return fingerprint;
    }

    public Fingerprint updateFingerprintGradeNull(Long fingerprintId, String loginUser) {
        Client client = ClientBuilder.newClient();
        Response response = client.target(SERVER)
                                  .path(API)
                                  .path(FINGERPRINTS_RESOURCE + "/" + fingerprintId)
                                  .request(MediaType.APPLICATION_JSON)
                                  .header("User", loginUser)
                                  .method("PATCH", Entity.json("{\"grade\": null}"));
        if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
            throw AppException.builder().status(response.getStatusInfo()).messages(response.readEntity(AppError.class).messages).build();
        }
        Fingerprint fingerprint = response.readEntity(Fingerprint.class);
        return fingerprint;
    }

    public Verification createVerification(Verification verification, String loginUser) {
        Client client = ClientBuilder.newClient();
        Response response = client.target(SERVER)
                                  .path(API)
                                  .path(VERIFICATIONS_RESOURCE)
                                  .request(MediaType.APPLICATION_JSON)
                                  .header("User", loginUser)
                                  .post(Entity.entity(verification, MediaType.APPLICATION_JSON_TYPE));
        if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
            throw AppException.builder().status(response.getStatusInfo()).messages(response.readEntity(AppError.class).messages).build();
        }
        verification = response.readEntity(Verification.class);
        return verification;
    }

    public Piva createPiva(Piva piva, String loginUser) {
        Client client = ClientBuilder.newClient();
        Response response = client.target(SERVER)
                                  .path(PIVA_RESOURCE)
                                  .request(MediaType.APPLICATION_JSON)
                                  .header("User", loginUser)
                                  .post(Entity.entity(piva, MediaType.APPLICATION_JSON_TYPE));
        if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
            throw AppException.builder().status(response.getStatusInfo()).messages(response.readEntity(AppError.class).messages).build();
        }
        piva = response.readEntity(Piva.class);
        return piva;
    }

    public Setting findSettingByName(String name, String loginUser) {
        Client client = ClientBuilder.newClient();
        Response response = client.target(SERVER)
                                  .path(API)
                                  .path(SETTINGS_RESOURCE + "/" + name)
                                  .request(MediaType.APPLICATION_JSON)
                                  .header("User", loginUser)
                                  .get();
        if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
            throw AppException.builder().status(response.getStatusInfo()).messages(response.readEntity(AppError.class).messages).build();
        }
        Setting setting = response.readEntity(Setting.class);
        return setting;
    }
}
