package prosense.sassa.mqclient.app.control;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import java.util.HashSet;
import java.util.Set;

import org.glassfish.jersey.jackson.JacksonFeature;

import prosense.sassa.mqclient.api.control.AppExceptionMapper;
import prosense.sassa.mqclient.api.control.JacksonConfig;
import prosense.sassa.mqclient.api.control.Property;
import prosense.sassa.mqclient.app.boundary.AppResource;
import prosense.sassa.mqclient.transaction.boundary.TransactionsResource;
import prosense.sassa.mqclient.mqevent.boundary.MQEventsResource;
import prosense.sassa.mqclient.beneficiary.boundary.BeneficiariesResource;
import prosense.sassa.mqclient.ping.boundary.PingsResource;
import prosense.sassa.mqclient.beneficiary.boundary.OutcomeErrorsResource;


@ApplicationPath("api")
public class RestApp extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(JacksonFeature.class);
        classes.add(JacksonConfig.class);
        classes.add(AppExceptionMapper.class);
        classes.add(AppResource.class);
        classes.add(TransactionsResource.class);
        classes.add(MQEventsResource.class);
        classes.add(BeneficiariesResource.class);
        classes.add(PingsResource.class);
        classes.add(OutcomeErrorsResource.class);
        return classes;
    }
}
