package prosense.sassa.srdclient.converters;

import java.util.Date;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import java.time.ZonedDateTime;
import java.time.ZoneId;


@Converter(autoApply = true)
public class ZonedDateTimeConverter implements AttributeConverter<ZonedDateTime, Date> {

    public Date convertToDatabaseColumn(ZonedDateTime zonedDateTime) {
        if(zonedDateTime == null) {
            return null;
        }
        return Date.from(zonedDateTime.toInstant());
    }

    public ZonedDateTime convertToEntityAttribute(Date date) {
        if(date == null) {
            return null;
        }
        return ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }
}
