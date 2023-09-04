package prosense.sassa.srdeft.payday.control;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.google.common.collect.Sets;

import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import prosense.sassa.srdeft.api.control.App;
import prosense.sassa.srdeft.api.entity.AppException;
import prosense.sassa.srdeft.payday.entity.PayDay;
import prosense.sassa.srdeft.publicholiday.entity.PublicHoliday;
import prosense.sassa.srdeft.publicholiday.control.PublicHolidayStore;

import javax.enterprise.context.Dependent;

import javax.inject.Inject;

import java.util.*;
import java.util.stream.Collectors;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.MultivaluedHashMap;


@Dependent
public class PayDayAgent {
    
    @Inject
    PublicHolidayStore publicHolidayStore;

    @Inject
    @App
    DateTimeFormatter dateTimeFormatter;

    public static final String processingDate = "processingDate";
    public static final String subService = "subService";
    public static final String period = "period";
    public static final String day = "payDay";

    public static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void validate(final ObjectNode objectNode) {
        final Set<String> messages = Sets.newHashSet();
        Optional.ofNullable(objectNode.get(processingDate)).filter(f -> !f.isNull()).ifPresent(n -> {
            if (!n.isTextual()) {
                messages.add(processingDate + " invalid");
            } else {
                try {
                    LocalDate.parse(n.asText(), dateFormatter);
                } catch (DateTimeParseException e) {
                    messages.add(processingDate + " invalid: " + e.getMessage());
                }
            }
        });
        Optional.ofNullable(objectNode.get(subService)).filter(f -> !f.isNull()).ifPresent(n -> {
            if (!n.isTextual()) {
                messages.add(subService + " invalid");
            } else {
                try {
                    PayDay.SubService.valueOf(n.asText());
                } catch (IllegalArgumentException e) {
                    messages.add(subService + " invalid: " + Arrays.toString(PayDay.SubService.values()));
                }
            }
        });
        if (!messages.isEmpty()) {
            throw AppException.builder().badRequest400().messages(messages).build();
        }
    }

    public void validateEvaluate(final PayDay payDay) {
        final Set<String> messages = Sets.newHashSet();
        if (payDay.getProcessingDate() == null) {
            messages.add(processingDate + " mandatory");
        }
        if (payDay.getSubService() == null) {
            messages.add(subService + " mandatory");
        }
        if (!messages.isEmpty()) {
            throw AppException.builder().unprocessableEntity422().messages(messages).build();
        }
    }

    public PayDay forEvaluate(final ObjectNode objectNode, final String user) {
        final PayDay.PayDayBuilder builder = PayDay.builder();
        Optional.ofNullable(objectNode.get(processingDate)).map(m -> LocalDate.parse(m.asText(), dateFormatter).atStartOfDay(ZoneId.systemDefault())).ifPresent(builder::processingDate);
        Optional.ofNullable(objectNode.get(subService)).map(JsonNode::asText).ifPresent(builder::subService);
        return builder.build();
    }

    public PayDay evaluate(final PayDay payDay) {
    	ZonedDateTime paymentDate = payDay.getProcessingDate();
		if (paymentDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
			throw AppException.builder().badRequest400().message(dateFormatter.format(paymentDate) + " is Sunday").build();
		}
        final MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<>();
        queryParameters.add("gteqDate", dateTimeFormatter.format(paymentDate));
        Set<PublicHoliday> publicHolidays = publicHolidayStore.search(queryParameters);
		if (publicHolidays.stream().map(PublicHoliday::getDate).anyMatch(paymentDate::equals)) {
			throw AppException.builder().badRequest400().message(dateFormatter.format(paymentDate) + " is Public Holiday").build();
		}
    	int days = getNumberOfDays(payDay.getSubService());
		int addedDays = 0;
		while (addedDays < days) {
			paymentDate = paymentDate.plusDays(1);
			if (!(paymentDate.getDayOfWeek() == DayOfWeek.SUNDAY || publicHolidays.stream().map(PublicHoliday::getDate).anyMatch(paymentDate::equals))) {
				++addedDays;
			}
		}
        payDay.setPeriod(DateTimeFormatter.ofPattern("MMMyyyy").format(paymentDate).toUpperCase());
        payDay.setPayDay(Long.valueOf(DateTimeFormatter.ofPattern("dd").format(paymentDate)));
        return payDay;
    }
    
    private int getNumberOfDays(String subServiceType) {
        switch (subServiceType) {
            case "same_day": return 0;
            case "one_day": return 1;
            case "two_day": return 2;
            default: return 0;
        }
    }
        
    public Map<String, Object> toMap(final PayDay payDay) {
        final Map<String, Object> map = new LinkedHashMap<>();
        map.put(processingDate, dateFormatter.format(payDay.getProcessingDate()));
        map.put(subService, payDay.getSubService());
        map.put(period, payDay.getPeriod());
        map.put(day, payDay.getPayDay());
        return map;
    }
}
