package prosense.sassa.dataloader;

import java.io.FileReader;
import java.io.FileNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.GenericType;

import org.glassfish.jersey.client.HttpUrlConnectorProvider;

public class Loader {
	// dev
    private static final String SERVER = "http://10.124.156.145:7003";

    private static final String ENROLMENT_API = "/enrolment/api";
    private static final String PARTIES_RESOURCE = "parties";
    private static final String LOGIN_USER = "Test";

    public static void main(String[] args) {
        Map < String, String > mapping = new HashMap < String, String > ();
        mapping.put("USR_UDF_NATIONAL_IDENTIFIER", "nationalIdentifier");
        mapping.put("USR_FIRST_NAME", "firstName");
        mapping.put("USR_LAST_NAME", "lastName");
        mapping.put("USR_EMAIL", "email");
        mapping.put("MOBILE_NUMBER", "mobile");
        mapping.put("USR_UDF_REGION", "region");
        mapping.put("USR_EMP_TYPE", "empType");
        mapping.put("USR_LOGIN", "login");
        mapping.put("USR_UDF_POSITION", "udfPosition");

        HeaderColumnNameTranslateMappingStrategy < HRParty > strategy = new HeaderColumnNameTranslateMappingStrategy < HRParty > ();
        strategy.setType(HRParty.class);
        strategy.setColumnMapping(mapping);

        CSVReader csvReader = null;
        try {
            csvReader = new CSVReader(new FileReader(args[0]));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        CsvToBean csvToBean = new CsvToBean();
        List < HRParty > list = csvToBean.parse(strategy, csvReader);
        for (HRParty hrParty: list) {
        	try {
				Set<EnrolmentParty> parties = findPartiesByIdNumber(hrParty.getNationalIdentifier());
				EnrolmentParty party = null;
				if (parties != null && parties.size() > 0){
					party = parties.iterator().next();
					boolean domainUserMismatch = false;
					if(!party.getDomainUser().equalsIgnoreCase(hrParty.getLogin())) {
						domainUserMismatch = true;
					}
					party = updateParty(party.getId(), transformForUpdate(hrParty, party));
					if(domainUserMismatch)
						System.out.println(hrParty.getNationalIdentifier() + " updated successfully -- domain user mismatch");
					else
						System.out.println(hrParty.getNationalIdentifier() + " updated successfully");
				} else {
					party = createParty(transformForCreate(hrParty));
					System.out.println(hrParty.getNationalIdentifier() + " created successfully");
				}
			} catch (AppException ex) {
				System.out.println(hrParty.getNationalIdentifier() + " error " + ex.toString());
			}
        }
    }
    
    private static Set<EnrolmentParty> findPartiesByIdNumber(String idNumber) {
        Client client = ClientBuilder.newClient();
        Response response = client.target(SERVER)
                                  .path(ENROLMENT_API)
                                  .path(PARTIES_RESOURCE)
                                  .queryParam("idNumber", idNumber)
                                  .request(MediaType.APPLICATION_JSON)
                                  .header("User", LOGIN_USER)
                                  .get();
        if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
            throw AppException.builder().status(response.getStatusInfo()).messages(response.readEntity(AppError.class).messages).build();
        }
        Set<EnrolmentParty> parties = response.readEntity(new GenericType<Set<EnrolmentParty>>() {});
        return parties;
    }

    private static EnrolmentParty createParty(EnrolmentParty party) {
        Client client = ClientBuilder.newClient();
        Response response = client.target(SERVER)
                                  .path(ENROLMENT_API)
                                  .path(PARTIES_RESOURCE)
                                  .request(MediaType.APPLICATION_JSON)
                                  .header("User", LOGIN_USER)
                                  .post(Entity.entity(party, MediaType.APPLICATION_JSON_TYPE));
        if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
            throw AppException.builder().status(response.getStatusInfo()).messages(response.readEntity(AppError.class).messages).build();
        }
        party = response.readEntity(EnrolmentParty.class);
        return party;
    }

    private static EnrolmentParty updateParty(Long partyId, EnrolmentParty party) {
        Client client = ClientBuilder.newClient();
        Response response = client.target(SERVER)
                                  .path(ENROLMENT_API)
                                  .path(PARTIES_RESOURCE + "/" + partyId)
                                  .request(MediaType.APPLICATION_JSON)
                                  .header("User", LOGIN_USER)
                                  .property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true)
                                  .method("PATCH", Entity.entity(party, MediaType.APPLICATION_JSON_TYPE));
        if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
            throw AppException.builder().status(response.getStatusInfo()).messages(response.readEntity(AppError.class).messages).build();
        }
        party = response.readEntity(EnrolmentParty.class);
        return party;
    }

    private static EnrolmentParty transformForCreate(HRParty hrParty) {
    	EnrolmentParty.EnrolmentPartyBuilder builder = EnrolmentParty.builder();
    	builder.idType("sa_id").idNumber(hrParty.getNationalIdentifier()).firstname(hrParty.getFirstName()).surname(hrParty.getLastName()).email(hrParty.getEmail()).mobile(hrParty.getMobile()).domainUser(hrParty.getLogin());
		if(hrParty.getRegion() != null && !hrParty.getRegion().equals("")) {
			StringBuffer region = new StringBuffer();
			String[] strArr = hrParty.getRegion().toLowerCase().split("\\ ");
			for (String str : strArr) {
				region.append(Character.toUpperCase(str.charAt(0))).append(str.substring(1)).append(" ");
			}
			builder.region(region.toString().trim());
	    }
    	if("Employee".equalsIgnoreCase(hrParty.getEmpType())) {
	    	builder.membership("staff");
	    }
		String[] udfPosition = hrParty.getUdfPosition().split("\\|");
		if(udfPosition != null && udfPosition.length == 4) {
	    	builder.position(udfPosition[1]);
    		builder.base(udfPosition[3]);
    	}
		if(hrParty.getUdfPosition().contains("SUPERVISOR: GRANT ADMINISTRATION")) {
			builder.supervisor(true);
		}
		if(hrParty.getUdfPosition().contains("LOCAL OFFICE MANAGER")) {
			builder.enrolment(true);
		}
		if(hrParty.getUdfPosition().contains("GRANT ADMINISTRATION") || hrParty.getUdfPosition().contains("GRANTS ADMINISTRATION") || hrParty.getUdfPosition().contains("GRANT ADMINISTRATOR")) {
			builder.beneficiary(true);
		}
		if(hrParty.getUdfPosition().contains("LOCAL OFFICE MANAGER") || hrParty.getUdfPosition().contains("GRANT ADMINISTRATION") || hrParty.getUdfPosition().contains("GRANTS ADMINISTRATION") || hrParty.getUdfPosition().contains("GRANT ADMINISTRATOR")) {
			builder.nonrepudiation(true);
		}
    	builder.status("active").enrolmentAdmin(false).beneficiaryAdmin(false).cardswop(false).support(false).supportAdmin(false);
    	return builder.build();
    }

    private static EnrolmentParty transformForUpdate(HRParty hrParty, EnrolmentParty enrolmentParty) {
    	EnrolmentParty.EnrolmentPartyBuilder builder = EnrolmentParty.builder();
		if(hrParty.getFirstName() != null && !hrParty.getFirstName().equals("")) {
	    	builder.firstname(hrParty.getFirstName());
	    }
		if(hrParty.getLastName() != null && !hrParty.getLastName().equals("")) {
			builder.surname(hrParty.getLastName());
	    }
		if(hrParty.getEmail() != null && !hrParty.getEmail().equals("")) {
			builder.email(hrParty.getEmail());
	    }
		if(hrParty.getMobile() != null && !hrParty.getMobile().equals("")) {
			builder.mobile(hrParty.getMobile());
	    }
		if(hrParty.getRegion() != null && !hrParty.getRegion().equals("")) {
			StringBuffer region = new StringBuffer();
			String[] strArr = hrParty.getRegion().toLowerCase().split("\\ ");
			for (String str : strArr) {
				region.append(Character.toUpperCase(str.charAt(0))).append(str.substring(1)).append(" ");
			}
			builder.region(region.toString().trim());
	    }
    	if(hrParty.getEmpType() != null && "Employee".equalsIgnoreCase(hrParty.getEmpType())) {
	    	builder.membership("staff");
	    }
		String[] udfPosition = hrParty.getUdfPosition().split("\\|");
		if(udfPosition != null && udfPosition.length == 4) {
	    	builder.position(udfPosition[1]);
    		builder.base(udfPosition[3]);
    	}
		if(hrParty.getUdfPosition().contains("SUPERVISOR: GRANT ADMINISTRATION")) {
			builder.supervisor(true);
		}
		if(hrParty.getUdfPosition().contains("LOCAL OFFICE MANAGER")) {
			builder.enrolment(true);
		}
		if(hrParty.getUdfPosition().contains("GRANT ADMINISTRATION") || hrParty.getUdfPosition().contains("GRANTS ADMINISTRATION") || hrParty.getUdfPosition().contains("GRANT ADMINISTRATOR")) {
			builder.beneficiary(true);
		}
		if(enrolmentParty.getEnrolment() || enrolmentParty.getBeneficiary() || hrParty.getUdfPosition().contains("LOCAL OFFICE MANAGER") || hrParty.getUdfPosition().contains("GRANT ADMINISTRATION") || hrParty.getUdfPosition().contains("GRANTS ADMINISTRATION") || hrParty.getUdfPosition().contains("GRANT ADMINISTRATOR")) {
			builder.nonrepudiation(true);
		}
    	builder.status("active").enrolmentAdmin(false).beneficiaryAdmin(false).cardswop(false).support(false).supportAdmin(false);
    	return builder.build();
    }
}