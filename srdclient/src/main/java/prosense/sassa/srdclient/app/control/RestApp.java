package prosense.sassa.srdclient.app.control;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import java.util.HashSet;
import java.util.Set;

import org.glassfish.jersey.jackson.JacksonFeature;

import prosense.sassa.srdclient.api.control.AppExceptionMapper;
import prosense.sassa.srdclient.api.control.JacksonConfig;
import prosense.sassa.srdclient.app.boundary.AppResource;
import prosense.sassa.srdclient.sarscompliance.boundary.SarsCompliancesResource;
import prosense.sassa.srdclient.mqevent.boundary.MQEventsResource;


@ApplicationPath("api")
public class RestApp extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(JacksonFeature.class);
        classes.add(JacksonConfig.class);
        classes.add(AppExceptionMapper.class);
        classes.add(AppResource.class);
        classes.add(SarsCompliancesResource.class);
        classes.add(MQEventsResource.class);
        return classes;
    }
}
