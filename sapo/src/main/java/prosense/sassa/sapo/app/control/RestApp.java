package prosense.sassa.sapo.app.control;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.jackson.JacksonFeature;

import prosense.sassa.sapo.api.control.AppExceptionMapper;
import prosense.sassa.sapo.app.boundary.AppResource;
import prosense.sassa.sapo.businessrules.fingerprint.boundary.FingerprintsResource;
import prosense.sassa.sapo.verification.boundary.VerificationsResource;


@ApplicationPath("api")
public class RestApp extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();

        classes.add(JacksonFeature.class);
        classes.add(AppExceptionMapper.class);
        classes.add(AppResource.class);
        classes.add(FingerprintsResource.class);
        classes.add(VerificationsResource.class);
        return classes;
    }
}
