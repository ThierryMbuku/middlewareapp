package prosense.sassa.oaamwrapper.api.control;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class JacksonConfig implements ContextResolver<ObjectMapper> {
    @Override
    public ObjectMapper getContext(final Class<?> type) {
    	DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return new ObjectMapper()
                .registerModule(new JSR310Module().addSerializer(ZonedDateTime.class, new JsonSerializer<ZonedDateTime>() {
                    @Override
                    public void serialize(ZonedDateTime zonedDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                        jsonGenerator.writeString(dateTimeFormatter.format(zonedDateTime));
                    }
                }).addDeserializer(ZonedDateTime.class, new JsonDeserializer<ZonedDateTime>() {
                    @Override
                    public ZonedDateTime deserialize(JsonParser jsonparser, DeserializationContext context) throws IOException {
                        return ZonedDateTime.of(LocalDateTime.parse(jsonparser.getText(), dateTimeFormatter), ZoneId.systemDefault());
                    }
                }))
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
}