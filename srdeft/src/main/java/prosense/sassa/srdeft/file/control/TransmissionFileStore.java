package prosense.sassa.srdeft.file.control;

import com.google.common.base.Strings;

import prosense.sassa.srdeft.file.entity.transmission.*;
import prosense.sassa.srdeft.transmissionfile.entity.TransmissionFile;
import prosense.sassa.srdeft.covjob.entity.CovJob;
import prosense.sassa.srdeft.covjob.boundary.CovJobsResource;

import java.util.Set;

import javax.enterprise.context.Dependent;

import javax.inject.Inject;

import java.nio.file.Files;
import java.nio.file.Paths;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;


@Dependent
public class TransmissionFileStore {
    @Inject
    FileAgent fileAgent;

    @Inject
    CovJobsResource covJobsResource;

    private final String seq_ackfilenumber = "ackfilenumber";

    private int numberOfFiles;
    private int numberOfRecords;

    public TransmissionFile generate(TransmissionFile transmissionFile) throws Exception {
    	if (transmissionFile.getType().equals(TransmissionFile.Type.ctl_begin.name()))
    		return generateBeginOfDayFile(transmissionFile);
    	else if (transmissionFile.getType().equals(TransmissionFile.Type.ctl_end.name()))
    		return generateEndOfDayFile(transmissionFile);
    	else
    		return generateAckFile(transmissionFile);
    }
    
    private TransmissionFile generateBeginOfDayFile(TransmissionFile transmissionFile) throws Exception {
        String fileName = fileAgent.getFileNamePrefix(transmissionFile.getSubService()) + "0Z1C";
        BeginOfDayFile  beginOfDayFile = BeginOfDayFile.builder()
        								.recordIdentifier("00")
        								.transactionType("01")
        								.processingDate(DateTimeFormatter.ofPattern(fileAgent.dateFormat8).format(ZonedDateTime.now()))
        								.serviceType(Strings.padEnd(fileAgent.getServiceValue(transmissionFile.getService()), 4, ' '))
        								.subServiceType(transmissionFile.getSubService().equalsIgnoreCase("two_day") ? Strings.padEnd("DATED", 10, ' ') : Strings.padEnd(fileAgent.getSubServiceValue(transmissionFile.getSubService()), 10, ' '))
        								.destination("4173")
        								.originator("ACBJ")
        								.dataDirection("IN ")
        								.testLiveIndicator(transmissionFile.getTest() ? "TEST" : "LIVE")
        								.filler(Strings.padEnd("", 139, ' '))
        								.build();
        Files.write(Paths.get(fileAgent.getFilePath(), fileName), beginOfDayFile.toLines());
        transmissionFile.setFileName(fileName);
        transmissionFile.setContent(beginOfDayFile.toString());
        return transmissionFile;
    }

    private TransmissionFile generateEndOfDayFile(TransmissionFile transmissionFile) throws Exception {
        String fileName = fileAgent.getFileNamePrefix(transmissionFile.getSubService()) + "0Z9C";
        Set<CovJob> covJobs = covJobsResource.search(CovJob.builder().type(transmissionFile.getService()).subService(transmissionFile.getSubService()).status(CovJob.Status.completed.name()).test(transmissionFile.getTest()).build());
        calculateNumbers(covJobs);
        EndOfDayFile endOfDayFile = EndOfDayFile.builder()
        							.recordIdentifier("00")
        							.transactionType("09")
        							.processingDate(DateTimeFormatter.ofPattern(fileAgent.dateFormat8).format(ZonedDateTime.now()))
        							.serviceType(Strings.padEnd(fileAgent.getServiceValue(transmissionFile.getService()), 4, ' '))
        							.subServiceType(transmissionFile.getSubService().equalsIgnoreCase("two_day") ? Strings.padEnd("DATED", 10, ' ') : Strings.padEnd(fileAgent.getSubServiceValue(transmissionFile.getSubService()), 10, ' '))
        							.destination("4173")
        							.originator("ACBJ")
        							.dataDirection("IN ")
        							.testLiveIndicator(transmissionFile.getTest() ? "TEST" : "LIVE")
        							.filler1(Strings.padEnd("", 1, ' '))
        							.numberOfFiles(Strings.padStart(String.valueOf(numberOfFiles), 4, '0'))
        							.numberOfRecords(Strings.padStart(String.valueOf(numberOfRecords), 8, '0'))
        							.filler2(Strings.padEnd("", 126, ' '))
        							.build();
        Files.write(Paths.get(fileAgent.getFilePath(), fileName), endOfDayFile.toLines());
        transmissionFile.setFileName(fileName);
        transmissionFile.setContent(endOfDayFile.toString());
        return transmissionFile;
    }

    private TransmissionFile generateAckFile(TransmissionFile transmissionFile) throws Exception {
        String fileName = getAckFileName(transmissionFile);
        AckFile ackFile = AckFile.builder()
        					.recordIdentifier("00")
        					.transactionType(getAckTransactionType(transmissionFile))
        					.processingDate(DateTimeFormatter.ofPattern(fileAgent.dateFormat8).format(ZonedDateTime.now()))
        					.serviceType(Strings.padEnd(fileAgent.getServiceValue(transmissionFile.getService()), 4, ' '))
        					.subServiceType(Strings.padEnd("REPORTS", 10, ' '))
        					.destination("4173")
        					.originator("ACBJ")
        					.dataDirection("OUT")
        					.testLiveIndicator(transmissionFile.getTest() ? "TEST" : "LIVE")
        					.fileName(fileName)
        					.fileNumber(transmissionFile.getType().equals(TransmissionFile.Type.ack_vet.name()) ? Strings.padStart(String.valueOf(fileAgent.getNextSequence(seq_ackfilenumber, null, null)), 4, '0') : "0000")
        					.errorCode(Strings.padEnd((transmissionFile.getErrorCode() != null ? transmissionFile.getErrorCode() : ""), 20, ' '))
        					.filler(Strings.padEnd("", 107, ' '))
        					.build();
        Files.write(Paths.get(fileAgent.getFilePath(), fileName), ackFile.toLines());
        transmissionFile.setFileName(fileName);
        transmissionFile.setContent(ackFile.toString());
        return transmissionFile;
    }
    
    private void calculateNumbers(Set<CovJob> covJobs) {
	    numberOfFiles = 0;
    	numberOfRecords = 0;
    	covJobs.forEach(covJob -> {
	    	covJob.getCashBooks().forEach(cashBook -> {
	    		numberOfFiles += cashBook.getDataFiles().size();
			    cashBook.getDataFiles().forEach(dataFile -> {
	    			numberOfRecords += Integer.valueOf(dataFile.getNumberOfRecords());
	    		});
    		});
    	});
    }
        
    private String getAckTransactionType(TransmissionFile transmissionFile) {
    	if(transmissionFile.getAckStatus().equals(TransmissionFile.AckStatus.positive.name())) {
    		if(transmissionFile.getType().equals(TransmissionFile.Type.ack_begin.name()))
    			return "51";
    		else if(transmissionFile.getType().equals(TransmissionFile.Type.ack_end.name()))
    			return "59";
    		else
    			return "52";
    	} else {
    		if(transmissionFile.getType().equals(TransmissionFile.Type.ack_begin.name()))
    			return "61";
    		else if(transmissionFile.getType().equals(TransmissionFile.Type.ack_end.name()))
    			return "69";
    		else
    			return "62";
    	}
    }
    
    private String getAckFileName(TransmissionFile transmissionFile) {
    	if(transmissionFile.getAckStatus().equals(TransmissionFile.AckStatus.positive.name())) {
    		if(transmissionFile.getType().equals(TransmissionFile.Type.ack_begin.name()))
    			return "ERSZ0Z1A";
    		else if(transmissionFile.getType().equals(TransmissionFile.Type.ack_end.name()))
    			return "ERSZ0Z9A";
    		else
    			return transmissionFile.getFileName().replaceFirst("(?s)D(?!.*?D)", "A");
    	} else {
    		if(transmissionFile.getType().equals(TransmissionFile.Type.ack_begin.name()))
    			return "ERSZ0Z1N";
    		else if(transmissionFile.getType().equals(TransmissionFile.Type.ack_end.name()))
    			return "ERSZ0Z9N";
    		else
    			return transmissionFile.getFileName().replaceFirst("(?s)D(?!.*?D)", "N");
    	}
    }
}