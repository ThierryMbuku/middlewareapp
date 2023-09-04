package prosense.sassa.srdeft.file.control;

import org.slf4j.Logger;

import com.google.common.base.Strings;
import com.google.common.base.Splitter;
import com.google.common.collect.Sets;

import org.apache.commons.lang3.StringUtils;

import prosense.sassa.srdeft.api.control.App;
import prosense.sassa.srdeft.api.entity.AppError;
import prosense.sassa.srdeft.api.entity.AppException;
import prosense.sassa.srdeft.api.control.Property;
import prosense.sassa.srdeft.covjob.entity.CovJob;
import prosense.sassa.srdeft.covjob.boundary.CovJobsResource;
import prosense.sassa.srdeft.batchcovjob.entity.BatchCovJob;
import prosense.sassa.srdeft.batchcovjob.boundary.BatchCovJobsResource;
import prosense.sassa.srdeft.batchcovjob.control.BatchCovJobAgent;
import prosense.sassa.srdeft.outcome.srd.entity.Outcome;
import prosense.sassa.srdeft.outcome.srd.boundary.OutcomesResource;
import prosense.sassa.srdeft.userdetail.entity.UserDetail;
import prosense.sassa.srdeft.userdetail.boundary.UserDetailsResource;
import prosense.sassa.srdeft.cashbook.entity.PatchOutcomeError;
import prosense.sassa.srdeft.cashbook.boundary.CashBooksResource;
import prosense.sassa.srdeft.transmissionfile.entity.TransmissionFile;
import prosense.sassa.srdeft.transmissionfile.boundary.TransmissionFilesResource;
import prosense.sassa.srdeft.alert.control.AlertStore;

import prosense.sassa.srdeft.file.entity.transaction.cashbook.*;
import prosense.sassa.srdeft.file.entity.transaction.datafile.*;

import javax.enterprise.context.Dependent;

import javax.inject.Inject;

import java.util.*;
import java.util.stream.Collectors;

import java.math.BigInteger;

import java.nio.file.Files;
import java.nio.file.Paths;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.WebApplicationException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.SQLException;

import javax.sql.DataSource;


@Dependent
public class TransactionFileStore {
    @Inject
    @App
    Logger logger;

    @Inject
    FileAgent fileAgent;

    @Inject
    CovJobsResource covJobsResource;

    @Inject
    BatchCovJobsResource batchCovJobsResource;
    
    @Inject
    OutcomesResource outcomesResource;

    @Inject
    UserDetailsResource userDetailsResource;

    @Inject
    CashBooksResource cashBooksResource;

    @Inject
    TransmissionFilesResource transmissionFilesResource;
    
    @Inject
    AlertStore alertStore;

    @Inject
    @Property
    private String amount;
    
    @Inject
    @Property
    private String srd_server;

    @Inject
    @Property
    private String srd_api;

    @Inject
    @Property
    private String transactionsPerFile;

    @Inject
    @Property
    private String macgen_url;

    @Inject
    @Property
    private String macgen_keySpec;

    @Inject
    @Property
    private String macgen_macMode;

    @Inject
    @Property
    private String macgen_zmkid;

    @Inject
    DataSource srdDataSource;

    private final String approved = "approved";
    private final String dateFormat8cb = "ddMMyyyy";
    private final String outcome_resource = "outcome";

    private final String seq_filename = "filename";
    private final String seq_filenumber = "filenumber";
    private final String seq_installationgeneration = "installationgeneration";
    private final String seq_datafileserial = "datafileserial";
    private final String seq_cashbookserial = "cashbookserial";
    private final String seq_usergeneration = "usergeneration";
    private final String seq_usersequence = "usersequence";
    private final String seq_cashbookfilename = "cashbookfilename";

    private CovJob covJob;
    private UserDetail userDetail;
    private prosense.sassa.srdeft.cashbook.entity.CashBook cashBookAudit;
    private List<Outcome> outcomes;
    private DataFile dataFile;
    private CashBook cashBook;
    private Set<PatchOutcomeError> patchOutcomeErrors;
    private boolean first = true;
    private long cashBookTotal = 0;
    private BigInteger dataFileHashTotal;
    private Map<String, String> macgen;
    private Connection srdConn;
    
    private List<Outcome> pendingOutcomes;
    private Set<Long> processedOutcomeIds;
    private Set<String> generatedFileNames;
    private Map<String, Long> sequenceCounts;
	private List<String> messages;
	private boolean beginOfDay;

    private BatchCovJob batchCovJob;
	private boolean batchFailed = false;
	private List<String> batchMessages;
    private long transactionsTotal;
    private long transactionsProcessed;
    private long transactionsRejected;

    public void generate(CovJob covJob) {
        new Thread(() -> execute(covJob, 1)).start();
    }
    
    public void generate(BatchCovJob batchCovJob) {
        new Thread(() -> execute(batchCovJob)).start();
    }
    
    private void execute(final BatchCovJob batchCovJob) {
        logger.info("begin");
        this.batchCovJob = batchCovJob;
		sendBatchCovJobAlert();
		batchFailed = false;
		batchMessages = new ArrayList<String>();
		transactionsTotal = 0L;
		transactionsProcessed = 0L;
		transactionsRejected = 0L;
		long paySeq = 1;
		CovJob covJob = CovJob.builder().verificationPeriod(batchCovJob.getVerificationPeriod()).period(batchCovJob.getPeriod()).payDay(batchCovJob.getPayDay()).subService(batchCovJob.getSubService()).type(batchCovJob.getType()).byRegion(batchCovJob.getByRegion()).test(batchCovJob.getTest()).status(batchCovJob.getStatus()).signStatus(batchCovJob.getSignStatus()).build();
        try {
			while(outcomesResource.searchApprovedByCovJob(covJob, paySeq).size() > 0) {
				covJob = CovJob.builder().verificationPeriod(batchCovJob.getVerificationPeriod()).period(batchCovJob.getPeriod()).payDay(batchCovJob.getPayDay()).subService(batchCovJob.getSubService()).type(batchCovJob.getType()).byRegion(batchCovJob.getByRegion()).test(batchCovJob.getTest()).status(BatchCovJob.Status.started.name()).signStatus(batchCovJob.getSignStatus()).batchCovJob(batchCovJob.getId()).start(ZonedDateTime.now()).creator(fileAgent.user).created(ZonedDateTime.now()).build();
				execute(covJobsResource.create(covJob), paySeq);
				if(batchFailed)
					break;
				paySeq += 1;
			}
			if(batchFailed) {
				batchMessages.add("(batch processing interrupted due to covJob " + covJob.getId() + " failure)");
				if(transactionsProcessed == 0L)
					updateBatchCovJob(BatchCovJob.Status.failed.name());
				else
					updateBatchCovJob(BatchCovJob.Status.partially_completed.name());
			} else {
				batchMessages.add("(batch processing completed successfully)");
		        updateBatchCovJob(BatchCovJob.Status.completed.name());
		    }
		} catch (Exception e) {
			logger.error("exception", e);
			batchMessages.add(StringUtils.substring("(system - failure - " + e.getMessage() + ")", 0, 200));
			if(transactionsProcessed == 0L)
				updateBatchCovJob(BatchCovJob.Status.failed.name());
			else
				updateBatchCovJob(BatchCovJob.Status.partially_completed.name());
		}
		sendBatchCovJobAlert();
        logger.info("end");
    }

    private void execute(final CovJob covJob, final long paySeq) {
        logger.info("begin");
        if(covJob.getByRegion()) {
			this.covJob = covJob;
        	beginOfDay = false;
			messages = new ArrayList<String>();
            try {
				srdConn = srdDataSource.getConnection();
				loadStagingData(paySeq);
				messages.add("(total - " + pendingOutcomes.size() + ")");
                fileAgent.checkDailySequences();
                Set<UserDetail> userDetails = userDetailsResource.searchAll();
                userDetails.forEach(userDetail -> {
					this.userDetail = userDetail;
					initializeRegion();
                    try {
                        while(true) {
                            outcomes = pendingOutcomes.stream().filter((Outcome o)-> o.getProvince().equalsIgnoreCase(userDetail.getRegion())).limit(Integer.parseInt(transactionsPerFile)).collect(Collectors.toList());
                            logger.info("outcome count for region " + userDetail.getRegion() + " :: " + outcomes.size());
                            if(outcomes.size() == 0)
                                break;
                            generateDataFile();
                        }
                        if(cashBookAudit.getDataFiles().size() > 0) {
                            generateCashBook();
                            cashBooksResource.create(cashBookAudit);
                            cashBookAudit = null;
                            if(!beginOfDay)
                            	beginOfDay = true;
                            messages.add("("+ userDetail.getRegion() + " - success - " + processedOutcomeIds.size() + ")");
                            transactionsProcessed += processedOutcomeIds.size();
                        }
                    } catch (WebApplicationException e) {
                        rollbackRegion();
						throw e;
                    } catch (Exception e) {
                        logger.error("exception", e);
                        rollbackRegion();
						messages.add(StringUtils.substring("("+ userDetail.getRegion() + " - failure - " + e.getMessage() + ")", 0, 200));
						batchFailed = true;
                    }
                });
		        updateCovJob(CovJob.Status.completed.name());
            } catch (Exception e) {
                logger.error("exception", e);
				messages.add(StringUtils.substring("(system - failure - " + e.getMessage() + ")", 0, 200));
				updateCovJob(CovJob.Status.failed.name());
				batchFailed = true;
			} finally {
				try {
					if(srdConn != null)
						srdConn.close();
				} catch (Exception e) {
					logger.error("exception", e);
				}
			}
			if(beginOfDay)
				transmissionFilesResource.create(TransmissionFile.builder().service(covJob.getType()).subService(covJob.getSubService()).type(TransmissionFile.Type.ctl_begin.name()).test(covJob.getTest()).creator(fileAgent.user).created(ZonedDateTime.now()).build());
        }
		cleanUp();
        logger.info("end");
    }

    private void generateDataFile() throws Exception  {
        dataFile = DataFile.builder().build();
        patchOutcomeErrors = new HashSet<PatchOutcomeError>();
        transformDataFile();
        Files.write(Paths.get(fileAgent.getFilePath(), dataFile.getControlHeader().getFileName()), dataFile.toLines());
        generatedFileNames.add(dataFile.getControlHeader().getFileName());
        cashBookAudit.getDataFiles().add(prosense.sassa.srdeft.cashbook.entity.DataFile.builder()
                                        .fileName(dataFile.getControlHeader().getFileName())
                                        .fileNumber(dataFile.getControlHeader().getFileNumber())
                                        .installationGenerationNumber(dataFile.getInstallationHeader().getInstallationGenerationNumber())
                                        .userGenerationNumber(dataFile.getUserHeader().getUserGenerationNumber())
                                        .serialNumber(dataFile.getInstallationHeader().getSerialNumber())
                                        .numberOfRecords(dataFile.getEofTrailer().getNumberOfRecords())
                                        .recordCount(dataFile.getInstallationTrailer().getRecordCount())
                                        .firstSequenceNumber(dataFile.getUserTrailer().getFirstSequenceNumber())
                                        .lastSequenceNumber(dataFile.getUserTrailer().getLastSequenceNumber())
                                        .totalDebitValue(dataFile.getUserTrailer().getTotalDebitValue())
                                        .totalCreditValue(dataFile.getUserTrailer().getTotalCreditValue())
                                        .userCode(dataFile.getContraRecord().getUserCode())
                                        .userBranch(dataFile.getContraRecord().getUserBranch())
                                        .userNominatedAccountNumber(dataFile.getContraRecord().getUserNominatedAccountNumber())
                                        .actionDate(dataFile.getContraRecord().getActionDate())
                                        .testLiveIndicator(dataFile.getControlHeader().getTestLiveIndicator())
                                        .settlementDate(dataFile.getControlHeader().getSettlementDate())
                                        .patchOutcomeErrors(patchOutcomeErrors)
                                        .creator(fileAgent.user).created(ZonedDateTime.now()).build());
        dataFile = null;
    }

    private void transformDataFile() {
        transformDataFileControlHeader();
        transformDataFileInstallationHeader();
        transformDataFileUserHeader();
        transformDataFileTransactionRecords();
        transformDataFileContraRecord();
        calculateDataFileHashTotal();
        transformDataFileUserTrailer();
        transformDataFileInstallationTrailer();
        generateKeyAndMacOfHashTotal();
        transformDataFileEOFTrailer();
    }

    private void transformDataFileControlHeader() {
        final ControlHeader.ControlHeaderBuilder builder = ControlHeader.builder();
        builder.recordIdentifier("01");
        builder.processingDate(DateTimeFormatter.ofPattern(fileAgent.dateFormat8).format(ZonedDateTime.now()));
        builder.serviceType(Strings.padEnd(fileAgent.getServiceValue(covJob.getType()), 4, ' '));
        builder.subServiceType(covJob.getSubService().equalsIgnoreCase("two_day") ? Strings.padEnd("DATED", 10, ' ') : Strings.padEnd(fileAgent.getSubServiceValue(covJob.getSubService()), 10, ' '));
        builder.destination("4173");
        builder.originator("ACBJ");
        builder.fileName(fileAgent.getFileNamePrefix(covJob.getSubService()) + Strings.padStart(String.valueOf(getNextSequence(seq_filename)), 3, '0') + "D");
        builder.fileNumber(Strings.padStart(String.valueOf(getNextSequence(seq_filenumber)), 4, '0'));
        builder.dataType("DATA");
        builder.dataDirection("IN ");
        builder.settlementDate(DateTimeFormatter.ofPattern(fileAgent.dateFormat8).format(ZonedDateTime.now()));
        builder.testLiveIndicator(covJob.getTest() ? "TEST" : "LIVE");
        builder.recordSize("0180");
        builder.bankCode("4173");
        builder.reportType(Strings.padEnd("", 10, ' '));
        builder.fileType(Strings.padEnd("", 10, ' '));
        builder.settlementWindow(Strings.padEnd("", 2, ' '));
        builder.transactionType(Strings.padEnd("", 30, ' '));
        builder.filler("");
        dataFile.setControlHeader(builder.build());
    }
    
    private void transformDataFileInstallationHeader() {
        final InstallationHeader.InstallationHeaderBuilder builder = InstallationHeader.builder();
        builder.recordIdentifier("02");
        builder.volumeNumber("1001");
        builder.serialNumber("SRD" + Strings.padStart(String.valueOf(getNextSequence(seq_datafileserial)), 5, '0'));
        builder.installationIDCodeFrom(Strings.padStart(String.valueOf(userDetail.getInstallationCode()), 4, '0'));
        builder.installationIDCodeTo("0021");
        builder.creationDate(DateTimeFormatter.ofPattern(fileAgent.dateFormat6).format(ZonedDateTime.now()));
        builder.purgeDate(DateTimeFormatter.ofPattern(fileAgent.dateFormat6).format(ZonedDateTime.now().plusDays(14)));
        builder.installationGenerationNumber(covJob.getTest() ? "TEST" : Strings.padStart(String.valueOf(getNextSequence(seq_installationgeneration)), 4, '0'));
        builder.blockLength("1800");
        builder.recordLength("0180");
        builder.service("MAGTAPE   ");
        builder.filler1(Strings.padEnd("", 8, ' '));
        builder.filler2(Strings.padEnd("", 116, ' '));
        dataFile.setInstallationHeader(builder.build());
    }
    
    private void transformDataFileUserHeader() {
        final UserHeader.UserHeaderBuilder builder = UserHeader.builder();
        builder.recordIdentifier("04");
        builder.userCode(Strings.padStart(String.valueOf(userDetail.getUserCode()), 4, '0'));
        builder.creationDate(DateTimeFormatter.ofPattern(fileAgent.dateFormat6).format(ZonedDateTime.now()));
        builder.purgeDate(DateTimeFormatter.ofPattern(fileAgent.dateFormat6).format(ZonedDateTime.now().plusDays(14)));
        builder.firstActionDate(fileAgent.getActionDate(fileAgent.dateFormat6, String.valueOf(covJob.getPayDay()) + covJob.getPeriod()));
        builder.lastActionDate(fileAgent.getActionDate(fileAgent.dateFormat6, String.valueOf(covJob.getPayDay()) + covJob.getPeriod()));
        builder.firstSequenceNumber(Strings.padStart(String.valueOf(getNextSequence(seq_usersequence)), 6, '0'));
        builder.userGenerationNumber(covJob.getTest() ? "TEST" : Strings.padStart(String.valueOf(getNextSequence(seq_usergeneration)), 4, '0'));
        builder.typeOfService(Strings.padEnd(fileAgent.getSubServiceValue(covJob.getSubService()), 10, ' '));
        builder.filler(Strings.padEnd("", 130, ' '));
        dataFile.setUserHeader(builder.build());
    }
    
    private void transformDataFileTransactionRecords() {
        Set<TransactionRecord> transactionRecords = new HashSet<TransactionRecord>();
        first = true;
        outcomes.forEach(outcome -> {
			final TransactionRecord.TransactionRecordBuilder builder = TransactionRecord.builder();
            try {
                if(!covJob.getTest()) {
//                     patchOutcome(outcome.getId(), "filed/" + dataFile.getControlHeader().getFileName(), "");
                    updateSrdOutcomeFiled(outcome.getId(), dataFile.getControlHeader().getFileName());
//                     outcomesResource.updateProcessed(outcome.getId(), true);
                }
				processedOutcomeIds.add(outcome.getId());
				builder.recordIdentifier("10");
				builder.userBranch(Strings.padStart(String.valueOf(userDetail.getBranchCode()), 6, '0'));
				builder.userNominatedAccountNumber(Strings.padStart(String.valueOf(userDetail.getAccountNumber()), 11, '0'));
				builder.userCode(Strings.padStart(String.valueOf(userDetail.getUserCode()), 4, '0'));
				builder.userSequenceNumber(first ? dataFile.getUserHeader().getFirstSequenceNumber() : Strings.padStart(String.valueOf(getNextSequence(seq_usersequence)), 6, '0'));
				if(first)
					first = false;
				builder.homingBranch(Strings.padStart(outcome.getBranch(), 6, '0'));
				builder.homingAccountNumber(outcome.getAccount().length() > 11 ? Strings.padStart("", 11, '0') : Strings.padStart(outcome.getAccount(), 11, '0'));
				builder.typeOfAccount(fileAgent.getBankServAccountType(outcome.getType()));
				builder.amount(Strings.padStart(String.valueOf(Math.round(Double.valueOf(amount)*100)), 11, '0'));
				builder.actionDate(fileAgent.getActionDate(fileAgent.dateFormat6, String.valueOf(covJob.getPayDay()) + covJob.getPeriod()));
				builder.entryClass("62");
				builder.taxCode("0");
				builder.filler1("00");
				builder.filler2("0");
				builder.userReference(Strings.padEnd(userDetail.getUserReference(), 10, ' ') + Strings.padEnd(covJob.getPeriod(), 20, ' '));
				builder.homingAccountName(Strings.padEnd(outcome.getHolder(), 30, ' '));
				builder.nonStandardHomingAccountNumber(outcome.getAccount().length() > 11 ? Strings.padStart(outcome.getAccount(), 20, '0') : Strings.padStart("", 20, '0'));
				builder.filler3(Strings.padEnd("", 16, ' '));
				builder.homingInstitution("21");
				builder.filler4(Strings.padEnd("", 12, ' '));
            } catch(AppException e) {
                patchOutcomeErrors.add(PatchOutcomeError.builder().outcome(outcome.getId()).type("filed").error(e.getMessage()).creator(fileAgent.user).created(ZonedDateTime.now()).build());
                return;
            } finally {
				updateSrdOutcomeStagingProcessed(outcome.getId(), true);
                pendingOutcomes.remove(outcome);
            }
            transactionRecords.add(builder.build());
        });
        dataFile.setTransactionRecords(transactionRecords);
    }
    
    private void transformDataFileContraRecord() {
        final ContraRecord.ContraRecordBuilder builder = ContraRecord.builder();
        builder.recordIdentifier("12");
        builder.userBranch(Strings.padStart(String.valueOf(userDetail.getBranchCode()), 6, '0'));
        builder.userNominatedAccountNumber(Strings.padStart(String.valueOf(userDetail.getAccountNumber()), 11, '0'));
        builder.userCode(Strings.padStart(String.valueOf(userDetail.getUserCode()), 4, '0'));
        builder.userSequenceNumber(Strings.padStart(String.valueOf(getNextSequence(seq_usersequence)), 6, '0'));
        builder.homingBranch(Strings.padStart(String.valueOf(userDetail.getBranchCode()), 6, '0'));
        builder.homingAccountNumber(Strings.padStart(userDetail.getAccountNumber(), 11, '0'));
        builder.typeOfAccount("1");
        builder.amount(Strings.padStart(String.valueOf(Math.round(dataFile.getTransactionRecords().size()*Double.valueOf(amount)*100)), 11, '0'));
        builder.actionDate(fileAgent.getActionDate(fileAgent.dateFormat6, String.valueOf(covJob.getPayDay()) + covJob.getPeriod()));
        builder.entryClass("10");
        builder.filler1("0000");
        builder.userReference(Strings.padEnd(userDetail.getUserReference(), 10, ' ') + Strings.padEnd("CONTRA", 11, ' ') + Strings.padEnd(covJob.getPeriod(), 9, ' '));
        builder.nominatedAccountName(Strings.padStart(String.valueOf(userDetail.getUserName()), 30, '0'));
        builder.filler2(Strings.padEnd("", 130, ' '));
        dataFile.setContraRecord(builder.build());
        cashBookTotal += dataFile.getTransactionRecords().size()*Double.valueOf(amount);
    }
    
    private void transformDataFileUserTrailer() {
        final UserTrailer.UserTrailerBuilder builder = UserTrailer.builder();
        builder.recordIdentifier("92");
        builder.userCode(Strings.padStart(String.valueOf(userDetail.getUserCode()), 4, '0'));
        builder.firstSequenceNumber(dataFile.getUserHeader().getFirstSequenceNumber());
        builder.lastSequenceNumber(dataFile.getContraRecord().getUserSequenceNumber());
        builder.firstActionDate(fileAgent.getActionDate(fileAgent.dateFormat6, String.valueOf(covJob.getPayDay()) + covJob.getPeriod()));
        builder.lastActionDate(fileAgent.getActionDate(fileAgent.dateFormat6, String.valueOf(covJob.getPayDay()) + covJob.getPeriod()));
        builder.noOfDebitRecords("000001");
        builder.noOfCreditRecords(Strings.padStart(String.valueOf(dataFile.getTransactionRecords().size()), 6, '0'));
        builder.noOfContraRecords("000001");
        builder.totalDebitValue(Strings.padStart(String.valueOf(Math.round(dataFile.getTransactionRecords().size()*Double.valueOf(amount)*100)), 12, '0'));
        builder.totalCreditValue(Strings.padStart(String.valueOf(Math.round(dataFile.getTransactionRecords().size()*Double.valueOf(amount)*100)), 12, '0'));
        builder.hashTotalOfHomingAccountNumbers(Strings.padStart(dataFileHashTotal.toString(), 12, '0'));
        builder.filler(Strings.padEnd("", 96, ' '));
        dataFile.setUserTrailer(builder.build());
    }
    
    private void transformDataFileInstallationTrailer() {
        final InstallationTrailer.InstallationTrailerBuilder builder = InstallationTrailer.builder();
        builder.recordIdentifier("94");
        builder.volumeNumber("1001");
        builder.serialNumber(dataFile.getInstallationHeader().getSerialNumber());
        builder.installationIDCodeFrom(Strings.padStart(String.valueOf(userDetail.getInstallationCode()), 4, '0'));
        builder.installationIDCodeTo("0021");
        builder.creationDate(DateTimeFormatter.ofPattern(fileAgent.dateFormat6).format(ZonedDateTime.now()));
        builder.purgeDate(DateTimeFormatter.ofPattern(fileAgent.dateFormat6).format(ZonedDateTime.now().plusDays(14)));
        builder.installationGenerationNumber(dataFile.getInstallationHeader().getInstallationGenerationNumber());
        builder.blockLength("1800");
        builder.recordLength("0180");
        builder.service("MAGTAPE   ");
        builder.blockCount(Strings.padStart(String.valueOf(Math.abs((dataFile.getTransactionRecords().size() + 4) / 10) + 1), 6, '0'));
        builder.recordCount(Strings.padStart(String.valueOf(dataFile.getTransactionRecords().size() + 5), 6, '0'));
        builder.userHeaderTrailerCount("000002");
        builder.filler(Strings.padEnd("", 106, ' '));
        dataFile.setInstallationTrailer(builder.build());
    }
    
    private void transformDataFileEOFTrailer() {
        final EOFTrailer.EOFTrailerBuilder builder = EOFTrailer.builder();
        builder.recordIdentifier("99");
        builder.processingDate(DateTimeFormatter.ofPattern(fileAgent.dateFormat8).format(ZonedDateTime.now()));
        builder.serviceType(dataFile.getControlHeader().getServiceType());
        builder.subServiceType(dataFile.getControlHeader().getSubServiceType());
        builder.destination("4173");
        builder.numberOfRecords(Strings.padStart(String.valueOf(dataFile.getTransactionRecords().size() + 7), 6, '0'));
        builder.sourceIdentifier("00009999");
        builder.encryptedWorkingKey16(Strings.padEnd("", 16, ' '));
        builder.macOfHashTotal(Strings.padStart(macgen.get("mac"), 16, '0').toUpperCase());
        builder.hashTotal(Strings.padStart(dataFileHashTotal.toString(), 12, '0'));
        builder.encryptedWorkingKey32(Strings.padStart(macgen.get("makUnderZmk"), 32, ' ').toUpperCase());
        builder.filler("");
        dataFile.setEofTrailer(builder.build());
    }
    
    private void generateCashBook() throws Exception  {
        cashBook = CashBook.builder().build();
        transformCashBook();
        String fileName = "OZDOW.COV" + userDetail.getRegionCode() + ".P.BAS.P" + DateTimeFormatter.ofPattern(fileAgent.dateFormat8).format(ZonedDateTime.now());
        fileName += Strings.padStart(String.valueOf(getNextSequence(seq_cashbookfilename)), 2, '0');
        Files.write(Paths.get(fileAgent.getFilePath(), fileName), cashBook.toLines());
        generatedFileNames.add(fileName);
        cashBookAudit.setCovJob(covJob.getId());
        cashBookAudit.setFileName(fileName);
        cashBookAudit.setSerialNumber(cashBook.getHeaderRecord().getSerialNumber());
        cashBookAudit.setDepartmentCode(cashBook.getDetailLine0().getDepartmentCode());
        cashBookAudit.setIssueDate(cashBook.getDetailLine0().getIssueDate());
        cashBookAudit.setPaymentAmount(cashBook.getDetailLine1().getPaymentAmount());
        cashBookAudit.setPaymentDescription(cashBook.getDetailLine1().getPaymentDescription());
        cashBookAudit.setCreator(fileAgent.user);
        cashBookAudit.setCreated(ZonedDateTime.now());
        cashBook = null;
    }

    private void transformCashBook() {
        transformCashBookHeaderRecord();
        transformCashBookDetailLine0();
        transformCashBookDetailLine1();
        transformCashBookTrailerRecord();
    }

    private void transformCashBookHeaderRecord() {
        final HeaderRecord.HeaderRecordBuilder builder = HeaderRecord.builder();
        builder.sourceSystem("COVI19");
        builder.dateCreated(DateTimeFormatter.ofPattern(dateFormat8cb).format(ZonedDateTime.now()));
        builder.timeCreated(DateTimeFormatter.ofPattern("HHmmss").format(ZonedDateTime.now()));
        builder.serialNumber(Strings.padStart(String.valueOf(getNextSequence(seq_cashbookserial)), 10, '0'));
        builder.atcSourceSerialNumber(Strings.padStart("", 10, '0'));
        builder.dateInterfaced(DateTimeFormatter.ofPattern(dateFormat8cb).format(ZonedDateTime.now()));
        builder.cashDate(DateTimeFormatter.ofPattern(dateFormat8cb).format(ZonedDateTime.now()));
        builder.filler(Strings.padEnd("", 174, ' '));
        cashBook.setHeaderRecord(builder.build());
    }
    
    private void transformCashBookDetailLine0() {
        final DetailLine0.DetailLine0Builder builder = DetailLine0.builder();
        builder.lineIdentifier(Strings.padStart("", 3, '0'));
        builder.departmentCode(userDetail.getDepartmentCode());
        builder.paymentMethod(Strings.padEnd("EBT", 6, ' '));
        builder.serialNumber(Strings.padEnd("", 8, ' '));
        builder.cashbookNumber(Strings.padStart("", 9, '0'));
        builder.issueDate(DateTimeFormatter.ofPattern(dateFormat8cb).format(ZonedDateTime.now()));
        builder.sign("+");
        builder.totalAmount(Strings.padStart(String.valueOf(Math.round(Double.valueOf(cashBookTotal)*100)), 16, '0'));
        builder.beneficiaryName(Strings.padEnd("SRD", 32, ' '));
        builder.actionDate(fileAgent.getActionDate(dateFormat8cb, String.valueOf(covJob.getPayDay()) + covJob.getPeriod()));
        builder.persalFlag("N");
        builder.entitySystemNumber(Strings.padStart("", 8, '0'));
        builder.bankAccountNumber(Strings.padStart("", 15, '0'));
        builder.bankCode(Strings.padEnd("0", 6, ' '));
        builder.bankBranchName(Strings.padEnd("ALL BANKS", 32, ' '));
        builder.bankBranchNumber(Strings.padStart("", 6, '0'));
        builder.bankAccountType(Strings.padStart("", 6, '0'));
        builder.bankAccountHolderName(Strings.padEnd("COVI19", 60, ' '));
        cashBook.setDetailLine0(builder.build());
    }
    
    private void transformCashBookDetailLine1() {
        final DetailLine1.DetailLine1Builder builder = DetailLine1.builder();
        builder.lineIdentifier(Strings.padStart("1", 3, '0'));
        builder.cashBookNumber(Strings.padStart("", 9, '0'));
        builder.paymentOrigin(Strings.padEnd("ALL", 6, ' '));
        builder.paymentNumber(Strings.padEnd("0", 8, ' '));
        builder.sourceDocumentType("RLFGRT");
        builder.sourceDocumentNumber(Strings.padEnd("", 32, ' '));
        builder.purchaseOrderNumber(Strings.padEnd("", 32, ' '));
        builder.sign("+");
        builder.paymentAmount(Strings.padStart(String.valueOf(Math.round(Double.valueOf(cashBookTotal)*100)), 16, '0'));
        builder.paymentDescription(Strings.padEnd("ALL" + covJob.getPeriod(), 57, ' '));
        builder.filler(Strings.padEnd("", 60, ' '));
        cashBook.setDetailLine1(builder.build());
    }
    
    private void transformCashBookTrailerRecord() {
        final TrailerRecord.TrailerRecordBuilder builder = TrailerRecord.builder();
        builder.lineIdentifier("TRAILER");
        builder.recordCount(Strings.padStart("2", 8, '0'));
        builder.sign("+");
        builder.hashTotal(Strings.padStart(String.valueOf(Math.round(Double.valueOf(cashBookTotal)*100)), 16, '0'));
        builder.filler(Strings.padEnd("", 198, ' '));
        cashBook.setTrailerRecord(builder.build());
    }
    
    private void calculateDataFileHashTotal() {
        dataFileHashTotal = BigInteger.ZERO;
        dataFile.getTransactionRecords().forEach(transactionRecord -> {
            dataFileHashTotal = dataFileHashTotal.add(new BigInteger(transactionRecord.getHomingAccountNumber()));
            dataFileHashTotal = dataFileHashTotal.add((new BigInteger(transactionRecord.getNonStandardHomingAccountNumber())).mod(BigInteger.valueOf(100000000000L)));
        });
        dataFileHashTotal = dataFileHashTotal.add(new BigInteger(dataFile.getContraRecord().getHomingAccountNumber()));
        dataFileHashTotal = dataFileHashTotal.mod(BigInteger.valueOf(1000000000000L));
    }

    private void generateKeyAndMacOfHashTotal() {
        Client client = ClientBuilder.newClient();
        Form form = new Form().param("keySpec", macgen_keySpec)
        					.param("macMode", macgen_macMode)
        					.param("message", dataFileHashTotal.toString())
        					.param("zmkid", macgen_zmkid);
		Response response = client.target(macgen_url)
								.request(MediaType.APPLICATION_FORM_URLENCODED)
							  	.post(Entity.form(form));
        if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL)
			throw new WebApplicationException(response);
        macgen = Splitter.on("\n").withKeyValueSeparator("=").split((response.readEntity(String.class)).trim());
    }

    private void patchOutcome(long id, String path, String json) {
        Client client = ClientBuilder.newClient();
        MultivaluedMap<String, Object> httpHeaders = new MultivaluedHashMap<>();
		httpHeaders.add("User", fileAgent.user);
		Response response = client.target("http://" + srd_server).path(srd_api)
							  .path(outcome_resource).path(String.valueOf(id)).path(path)
							  .request(MediaType.APPLICATION_JSON)
							  .headers(httpHeaders)
							  .method("PATCH", Entity.json(json));
        if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
			if(response.getStatusInfo().equals(Status.BAD_REQUEST))
	            throw AppException.builder().status(response.getStatusInfo()).messages(response.readEntity(AppError.class).messages).build();
			else
				throw new WebApplicationException(response);
        }
        client.close();
    }

    private void updateSrdOutcomeFiled(long id, String payFile) {
        String query = "update OUTCOME set FILED = ?, PAYFILE = ?, UPDATED = ?, UPDATOR = 'srdeft_user' "+
                    "where ID = ? and FILED is null and PAYFILE is null and OUTCOME = 'approved'";
		try {
			PreparedStatement preparedStmt = srdConn.prepareStatement(query);
			Timestamp ts = new Timestamp(System.currentTimeMillis());
			preparedStmt.setTimestamp(1, ts);
			preparedStmt.setString(2, payFile);
			preparedStmt.setTimestamp(3, ts);
			preparedStmt.setLong(4, id);
			int i = preparedStmt.executeUpdate();
			preparedStmt.close();
			preparedStmt = null;
			if(i == 0)
				throw AppException.builder().badRequest400().message("unprocessed outcome not found").build();
		} catch (SQLException e) {
            throw AppException.builder().badRequest400().message(e.getMessage()).build();
        }
    }

    private void updateSrdOutcomePayRejected(long id) {
        String query = "update OUTCOME set OUTCOME = 'payrejected', UPDATED = ?, UPDATOR = 'srdeft_user' "+
                    "where ID = ? and FILED is null and PAYFILE is null and OUTCOME = 'approved'";
		try {
			PreparedStatement preparedStmt = srdConn.prepareStatement(query);
			Timestamp ts = new Timestamp(System.currentTimeMillis());
			preparedStmt.setTimestamp(1, ts);
			preparedStmt.setLong(2, id);
			preparedStmt.executeUpdate();
			preparedStmt.close();
			preparedStmt = null;
		} catch (SQLException e) {
            throw AppException.builder().badRequest400().message(e.getMessage()).build();
        }
    }

    private void updateSrdOutcomeNotFiled(long id) {
        String query = "update OUTCOME set FILED = null, PAYFILE = null, UPDATED = ?, UPDATOR = 'srdeft_user' "+
                    "where ID = ? and FILED is not null and PAYFILE is not null and OUTCOME = 'approved'";
		try {
			PreparedStatement preparedStmt = srdConn.prepareStatement(query);
			Timestamp ts = new Timestamp(System.currentTimeMillis());
			preparedStmt.setTimestamp(1, ts);
			preparedStmt.setLong(2, id);
			preparedStmt.executeUpdate();
			preparedStmt.close();
			preparedStmt = null;
		} catch (SQLException e) {
            throw AppException.builder().badRequest400().message(e.getMessage()).build();
        }
    }

    private void updateSrdOutcomeStagingProcessed(long id, boolean processed) {
        String query = "update OUTCOME_STAGING set PROCESSED = ? where OID = ?";
		try {
			PreparedStatement preparedStmt = srdConn.prepareStatement(query);
			Timestamp ts = new Timestamp(System.currentTimeMillis());
			if(processed)
				preparedStmt.setTimestamp(1, ts);
			else
				preparedStmt.setTimestamp(1, null);
			preparedStmt.setLong(2, id);
			preparedStmt.executeUpdate();
			preparedStmt.close();
			preparedStmt = null;
		} catch (SQLException e) {
            throw AppException.builder().badRequest400().message(e.getMessage()).build();
        }
    }

    private void loadStagingData(final long paySeq) {
    	logger.info("begin");
		pendingOutcomes = new ArrayList<>();
		Set<Outcome> outcomes = outcomesResource.searchApprovedByCovJob(covJob, paySeq);
		logger.info("outcomes size :: " + outcomes.size());
		transactionsTotal += outcomes.size();
		outcomes.forEach(outcome -> {
			try {
				if(outcome.getBranch() == null || outcome.getAccount() == null || !StringUtils.isNumeric(outcome.getAccount()) || outcome.getType() == null || outcome.getHolder() == null) {
// 					patchOutcome(outcome.getId(), "", "{\"outcome\": \"payrejected\"}");
					updateSrdOutcomePayRejected(outcome.getId());
// 					outcomesResource.updateProcessed(outcome.getId(), true);
					updateSrdOutcomeStagingProcessed(outcome.getId(), true);
					transactionsRejected += 1L;
					return;
				}
				outcome.setAccount(String.valueOf(new BigInteger(outcome.getAccount())));
				if(outcome.getAccount().length() > 11) {
// 					patchOutcome(outcome.getId(), "", "{\"outcome\": \"payrejected\"}");
					updateSrdOutcomePayRejected(outcome.getId());
// 					outcomesResource.updateProcessed(outcome.getId(), true);
					updateSrdOutcomeStagingProcessed(outcome.getId(), true);
					transactionsRejected += 1L;
					return;
				}
// 				if(outcome.getBranch().equals("198765") && outcome.getAccount().length() == 10 && outcome.getAccount().substring(9).equals("1"))
// 					outcome.setType("cheque");
				if(outcome.getHolder().length() > 30)
					outcome.setHolder(outcome.getHolder().substring(0, 30));
				pendingOutcomes.add(outcome);
            } catch(AppException e) {
				logger.error("exception", e);
				return;
			} catch (Exception e) {
				pendingOutcomes = new ArrayList<>();
				throw e;
			}
		});
		logger.info("pendingOutcomes size :: " + pendingOutcomes.size());
		outcomes = null;
    	logger.info("end");
    }
    
    private void initializeRegion() {
		cashBookTotal = 0;
		processedOutcomeIds = Sets.newHashSet();
		generatedFileNames = Sets.newHashSet();
		sequenceCounts = new HashMap();
		sequenceCounts.put(seq_filename, 0L);
		sequenceCounts.put(seq_filenumber, 0L);
		sequenceCounts.put(seq_installationgeneration, 0L);
		sequenceCounts.put(seq_datafileserial, 0L);
		sequenceCounts.put(seq_cashbookserial, 0L);
		sequenceCounts.put(seq_usergeneration, 0L);
		sequenceCounts.put(seq_usersequence, 0L);
		sequenceCounts.put(seq_cashbookfilename, 0L);
		cashBookAudit = prosense.sassa.srdeft.cashbook.entity.CashBook.builder().dataFiles(new HashSet<prosense.sassa.srdeft.cashbook.entity.DataFile>()).build();
    }
    
    private long getNextSequence(String name) {
		if(name.equals(seq_filename) || name.equals(seq_filenumber) || name.equals(seq_datafileserial) || name.equals(seq_cashbookserial)) {
			sequenceCounts.put(name, sequenceCounts.get(name) + 1L);
			return fileAgent.getNextSequence(name, covJob.getSubService(), null);
		} else if(name.equals(seq_installationgeneration) || name.equals(seq_usergeneration) || name.equals(seq_cashbookfilename)) {
			sequenceCounts.put(name, sequenceCounts.get(name) + 1L);
			return fileAgent.getNextSequence(name, null, userDetail.getRegion());
		} else if(name.equals(seq_usersequence)) {
			sequenceCounts.put(name, sequenceCounts.get(name) + 1L);
			return fileAgent.getNextSequence(name, covJob.getSubService(), userDetail.getRegion());
		} else
			return 0L;
    }
    
    private void rollbackRegion() {
    	logger.info("processedOutcomeIds size:: " + processedOutcomeIds.size());
    	logger.info("generatedFileNames size:: " + generatedFileNames.size());
    	logger.info("sequenceCounts size:: " + sequenceCounts.size());
    	logger.info("pendingOutcomes size:: " + pendingOutcomes.size());
    	processedOutcomeIds.forEach(id -> {
			if(!covJob.getTest()) {
				try {
// 					patchOutcome(id, "notfiled", "");
					updateSrdOutcomeNotFiled(id);
// 					outcomesResource.updateProcessed(id, false);
					updateSrdOutcomeStagingProcessed(id, false);
				} catch(Exception e) {
					logger.error("exception", e);
					return;
				}
			}
    	});
    	generatedFileNames.forEach(fileName -> {
    		try {
		        Files.deleteIfExists(Paths.get(fileAgent.getFilePath(), fileName));
            } catch(Exception e) {
            	logger.error("exception", e);
            	return;
            }
    	});
    	sequenceCounts.forEach((name, count) -> {
    		try {
				if(name.equals(seq_filename) || name.equals(seq_filenumber) || name.equals(seq_datafileserial) || name.equals(seq_cashbookserial)) {
					fileAgent.rollbackSequence(name, covJob.getSubService(), null, count);
				} else if(name.equals(seq_installationgeneration) || name.equals(seq_usergeneration) || name.equals(seq_cashbookfilename)) {
					fileAgent.rollbackSequence(name, null, userDetail.getRegion(), count);
				} else if(name.equals(seq_usersequence)) {
					fileAgent.rollbackSequence(name, covJob.getSubService(), userDetail.getRegion(), count);
				}
            } catch(Exception e) {
            	logger.error("exception", e);
            	return;
            }
		});
    	pendingOutcomes = new ArrayList<>();
    }
    
    private void updateCovJob(String status) {
        covJob.setEnd(ZonedDateTime.now());
        covJob.setStatus(status);
        covJob.setMessages(StringUtils.substring(messages.toString(), 0, 4000));
        covJob.setUpdator(fileAgent.user);
        covJob.setUpdated(ZonedDateTime.now());
        covJobsResource.update(covJob);
    }

    private void updateBatchCovJob(String status) {
        batchCovJob.setEnd(ZonedDateTime.now());
        batchCovJob.setStatus(status);
        batchCovJob.setTransactionsTotal(transactionsTotal);
        batchCovJob.setTransactionsProcessed(transactionsProcessed);
        batchCovJob.setTransactionsRejected(transactionsRejected);
        batchCovJob.setMessages(StringUtils.substring(batchMessages.toString(), 0, 4000));
        batchCovJob.setUpdator(fileAgent.user);
        batchCovJob.setUpdated(ZonedDateTime.now());
        batchCovJobsResource.update(batchCovJob);
    }

    private void sendBatchCovJobAlert() {
    	String subject = "batchCovJob id " + batchCovJob.getId() + " " + batchCovJob.getStatus();
    	String body = BatchCovJobAgent.id + " # " + batchCovJob.getId();
    	body += "\n" + BatchCovJobAgent.status + " # " + batchCovJob.getStatus();
    	body += "\n" + BatchCovJobAgent.start + " # " + batchCovJob.getStart();
    	if(batchCovJob.getEnd() != null) {
			body += "\n" + BatchCovJobAgent.end + " # " + batchCovJob.getEnd();
			body += "\n" + BatchCovJobAgent.transactionsTotal + " # " + batchCovJob.getTransactionsTotal();
			body += "\n" + BatchCovJobAgent.transactionsProcessed + " # " + batchCovJob.getTransactionsProcessed();
			body += "\n" + BatchCovJobAgent.transactionsRejected + " # " + batchCovJob.getTransactionsRejected();
			body += "\n" + BatchCovJobAgent.messages + " # " + batchCovJob.getMessages();
		}
		alertStore.sendAlert(subject, body);
    }

    private void cleanUp() {
		covJob = null;
		userDetail = null;
		cashBookAudit = null;
		outcomes = null;
		dataFile = null;
		cashBook = null;
		patchOutcomeErrors = null;
		dataFileHashTotal = null;
		macgen = null;
		pendingOutcomes = null;
		processedOutcomeIds = null;
		generatedFileNames = null;
		sequenceCounts = null;
		messages = null;
		srdConn = null;
    }
}