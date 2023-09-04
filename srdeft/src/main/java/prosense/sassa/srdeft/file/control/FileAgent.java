package prosense.sassa.srdeft.file.control;

import org.slf4j.Logger;

import javax.enterprise.context.Dependent;

import javax.inject.Inject;

import java.nio.file.Files;
import java.nio.file.Paths;

import java.text.SimpleDateFormat;  

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import java.util.*;

import prosense.sassa.srdeft.api.control.App;
import prosense.sassa.srdeft.api.control.Property;
import prosense.sassa.srdeft.sequence.entity.Sequence;
import prosense.sassa.srdeft.sequence.boundary.SequencesResource;


@Dependent
public class FileAgent {
    @Inject
    @App
    Logger logger;

    @Inject
    @Property
    String filePath;
    
    @Inject
    SequencesResource sequencesResource;

    public final String user = "srdeft_user";
    public final String daily = "daily";
    public final String dateFormat6 = "yyMMdd";
    public final String dateFormat8 = "yyyyMMdd";

    public String getFilePath() throws Exception{
        String path = filePath + DateTimeFormatter.ofPattern(dateFormat8).format(ZonedDateTime.now());
        if (!Files.exists(Paths.get(path)))
            Files.createDirectory(Paths.get(path));
        return path;
    }

    public void checkDailySequences() {
        Set<Sequence> sequences = sequencesResource.searchAll();
        sequences.forEach(sequence -> {
            if(sequence.getFrequency().equals(daily) && sequence.getUpdated() != null && !DateTimeFormatter.ofPattern(dateFormat6).format(sequence.getUpdated()).equals(DateTimeFormatter.ofPattern(dateFormat6).format(ZonedDateTime.now()))) {
                resetSequence(sequence);
            }
        });
    }
    
    private void resetSequence(Sequence sequence) {
        sequence.setCurValue(0L);
        sequence.setUpdator(user);
        sequence.setUpdated(ZonedDateTime.now());
        sequencesResource.update(sequence);
    }
    
    public long getNextSequence(String name, String subService, String region) {
        Sequence sequence = sequencesResource.read(Sequence.builder().name(name).subService(subService).region(region).build());
        if(sequence.getCurValue() < sequence.getMaxValue())
        	sequence.setCurValue(sequence.getCurValue() + 1L);
        else
            sequence.setCurValue(sequence.getMinValue());
        sequence.setUpdator(user);
        sequence.setUpdated(ZonedDateTime.now());
        sequence = sequencesResource.update(sequence);
        return sequence.getCurValue();
    }
        
    public void rollbackSequence(String name, String subService, String region, long count) {
    	if(count < 1)
    		return;
        Sequence sequence = sequencesResource.read(Sequence.builder().name(name).subService(subService).region(region).build());
        if((sequence.getCurValue() - count) >= (sequence.getMinValue() - 1L))
        	sequence.setCurValue(sequence.getCurValue() - count);
        else
            sequence.setCurValue(sequence.getMaxValue() - (count - sequence.getCurValue()));
        sequence.setUpdator(user);
        sequence.setUpdated(ZonedDateTime.now());
        sequencesResource.update(sequence);
    }
        
    public String getServiceValue(String serviceType) {
        switch (serviceType) {
            case "eft": return "EFT";
            default: return null;
        }
    }
        
    public String getSubServiceValue(String subServiceType) {
        switch (subServiceType) {
            case "same_day": return "SAMEDAY";
            case "one_day": return "ONE DAY";
            case "two_day": return "TWO DAY";
            default: return null;
        }
    }
        
    public String getFileNamePrefix(String subServiceType) {
        switch (subServiceType) {
            case "same_day": return "ESSZ";
            case "one_day": return "ENSZ";
            case "two_day": return "EJSZ";
            default: return null;
        }
    }
        
    public String getBankServAccountType(String accountType) {
        switch (accountType) {
            case "cheque": return "1";
            case "savings": return "2";
            case "transmission": return "3";
            default: return null;
        }
    }
        
    public String getActionDate(String dateFormat, String value) {
        String actionDay = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMMyyyy");
            Date date = sdf.parse(value);
            sdf.applyPattern(dateFormat);
            actionDay = sdf.format(date);
        } catch(Exception e){
            logger.error("exception", e);
        }
        return actionDay;
    }
}
