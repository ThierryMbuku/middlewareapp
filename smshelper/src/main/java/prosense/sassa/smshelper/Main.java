package prosense.sassa.smshelper;

import java.util.List;
import java.util.ArrayList;

import java.io.Reader;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.opencsv.bean.CsvToBeanBuilder;

import org.json.JSONArray;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.client.Entity;
import javax.ws.rs.WebApplicationException;


public class Main {
    private static final String PERIOD = "DEC2020";
    private static final String NEW_DIR = "files/new/";
    private static final String PROCESSED_DIR = "files/processed/";

// PROD
    private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521/XEPDB1";
// DEV
//     private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521/XEPDB1";
    private static final String DB_USER = "smshelper_user";
    private static final String DB_PASSWORD = "smshelper_user";

    private static final int SMS_MAX_PER_DAY = 200000;
    private static final String SMS_SERVER_URL = "https://restapi.gsm.co.za/send/sms";
    private static final String SMS_SERVER_TOKEN = "U0FTU0FVU1NEOiFxdWNoQGE=";
    private static final int SMS_SERVER_RETRIES = 3;
    
    private static int totalSms;

	private static Connection connection;
	
    public static void main(String[] args) throws Exception {
		connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
		loadOutcomes();
		notifyOutcomes();
		connection.close();
    }

    private static void loadOutcomes() throws Exception {
		Files.list(Paths.get(NEW_DIR)).filter(path -> path.toString().endsWith(".txt")).forEach(path -> {
			try {
				System.out.println(path.getFileName());
				Reader reader = Files.newBufferedReader(path);
				List<Outcome> outcomes = new CsvToBeanBuilder(reader).withType(Outcome.class).withSeparator('~').withIgnoreLeadingWhiteSpace(true).build().parse();
				insertOutcomes(outcomes, path.getFileName().toString());
				Files.move(path, Paths.get(PROCESSED_DIR + path.getFileName()), StandardCopyOption.REPLACE_EXISTING);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
    }

    private static void notifyOutcomes() throws Exception {
		totalSms = 0;
		for (Outcome outcome : findOutcomes()) {
			try {
				sendSms(outcome);
				insertSentSms(outcome);
				updateOutcome(outcome);
				totalSms++;
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
		System.out.println("SMS sent in this run :: " + totalSms);
    }

    private static void insertOutcomes(List<Outcome> outcomes, String fileName) throws Exception {
		PreparedStatement preparedStmt = connection.prepareStatement("insert into OUTCOME values (outcome_seq.nextval, ?, ?, ?, ?, ?, ?, ?, null, 'smshelper_user', sysdate)");
		for (Outcome outcome: outcomes) {
			preparedStmt.setString(1, outcome.getIdNumber());
			if (outcome.getNames().length() > 100)
				preparedStmt.setString(2, outcome.getNames().substring(0,100));
			else
				preparedStmt.setString(2, outcome.getNames());
			if (outcome.getSurname().length() > 13)
				preparedStmt.setString(3, outcome.getSurname().substring(0,13));
			else
				preparedStmt.setString(3, outcome.getSurname());
			preparedStmt.setString(4, outcome.getMobile());
			preparedStmt.setString(5, outcome.getParty());
			preparedStmt.setString(6, outcome.getApplication());
			preparedStmt.setString(7, fileName);
			preparedStmt.executeUpdate();
		}
		preparedStmt.close();
    }

    private static List<Outcome> findOutcomes() throws Exception {
    	List<Outcome> outcomes = new ArrayList<Outcome>();
    	int limit = 0; 
    	Statement statement = connection.createStatement();
    	ResultSet resultSet = statement.executeQuery("select count(ID) from SENT_SMS where created >= trunc(sysdate)");
		if (resultSet.next())
			limit = SMS_MAX_PER_DAY - resultSet.getInt(1);
		if (limit > 0) {
			resultSet = statement.executeQuery("select ID, ID_NUMBER, NAMES, SURNAME, MOBILE, PARTY, APPLICATION from OUTCOME where PROCESSED is null and ROWNUM <= " + String.valueOf(limit));
			while (resultSet.next())
				outcomes.add(Outcome.builder().id(resultSet.getLong(1)).idNumber(resultSet.getString(2)).names(resultSet.getString(3)).surname(resultSet.getString(4)).mobile(resultSet.getString(5)).party(resultSet.getString(6)).application(resultSet.getString(7)).build());
		} else {
	        System.out.println("SMS limit reached for today");
	    }
		return outcomes;
	}

    private static void insertSentSms(Outcome outcome) throws Exception {
		PreparedStatement preparedStmt = connection.prepareStatement("insert into SENT_SMS values (sent_sms_seq.nextval, ?, ?, ?, 'smshelper_user', sysdate)");
		preparedStmt.setString(1, outcome.getIdNumber());
		preparedStmt.setString(2, outcome.getMobile());
		preparedStmt.setString(3, PERIOD);
		preparedStmt.executeUpdate();
		preparedStmt.close();
    }

    private static void updateOutcome(Outcome outcome) throws Exception {
		PreparedStatement preparedStmt = connection.prepareStatement("update OUTCOME set PROCESSED = sysdate where ID = ?");
		preparedStmt.setLong(1, outcome.getId());
		preparedStmt.executeUpdate();
		preparedStmt.close();
    }

    private static void sendSms(Outcome outcome) throws Exception {
    	String mask = outcome.getIdNumber().substring(0,4) + "..." + outcome.getIdNumber().substring(10,13);
		String message = outcome.getSurname().toUpperCase() + ". ID " + mask + ", AppID " + outcome.getApplication() + ". You have unclaimed SASSA Covid grant payments at the Post Office. Payments are suspended until funds are collected.";
		String json = "{\"to\": \"" + outcome.getMobile() + "\", \"message\": \"" + message + "\", \"ems\": \"0\", \"userref\": \"" + outcome.getApplication() + "\"}";
		Client client = ClientBuilder.newClient();
		int retryCount = 0;
		while (true) {
			Response response = client.target(SMS_SERVER_URL)
									  .request(MediaType.APPLICATION_JSON)
									  .accept(MediaType.APPLICATION_JSON)
									  .header(HttpHeaders.AUTHORIZATION, "Basic " + SMS_SERVER_TOKEN)
									  .post(Entity.json(json));
			if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
				retryCount++;
				if (retryCount > SMS_SERVER_RETRIES)
					throw new WebApplicationException(response);
			} else {
				JSONArray jsonArray = new JSONArray(response.readEntity(String.class));
// 				System.out.println(jsonArray.toString());
				if (jsonArray.length() > 0 && "153".equals(jsonArray.getJSONObject(0).get("Error")))
					throw new RuntimeException("FAILED to send SMS - " + jsonArray.toString());
				break;
			}
		}
		client.close();
    }
}