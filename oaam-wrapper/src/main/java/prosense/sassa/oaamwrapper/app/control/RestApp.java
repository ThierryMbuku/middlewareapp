package prosense.sassa.oaamwrapper.app.control;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.jackson.JacksonFeature;

import prosense.sassa.oaamwrapper.api.control.AppExceptionMapper;
import prosense.sassa.oaamwrapper.api.control.JacksonConfig;
import prosense.sassa.oaamwrapper.app.boundary.AppResource;
import prosense.sassa.oaamwrapper.transaction.boundary.TransactionsResource;


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
        return classes;
    }
}
