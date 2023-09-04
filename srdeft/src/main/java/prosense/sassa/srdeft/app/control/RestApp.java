package prosense.sassa.srdeft.app.control;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import java.util.HashSet;
import java.util.Set;

import org.glassfish.jersey.jackson.JacksonFeature;

import prosense.sassa.srdeft.api.control.AppExceptionMapper;
import prosense.sassa.srdeft.api.control.JacksonConfig;
import prosense.sassa.srdeft.app.boundary.AppResource;
import prosense.sassa.srdeft.covjob.boundary.CovJobsResource;
import prosense.sassa.srdeft.transmissionfile.boundary.TransmissionFilesResource;
import prosense.sassa.srdeft.batchcovjob.boundary.BatchCovJobsResource;
import prosense.sassa.srdeft.publicholiday.boundary.PublicHolidaysResource;
import prosense.sassa.srdeft.payday.boundary.PayDaysResource;


@ApplicationPath("api")
public class RestApp extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(JacksonFeature.class);
        classes.add(JacksonConfig.class);
        classes.add(AppExceptionMapper.class);
        classes.add(AppResource.class);
        classes.add(CovJobsResource.class);
        classes.add(TransmissionFilesResource.class);
        classes.add(BatchCovJobsResource.class);
        classes.add(PublicHolidaysResource.class);
        classes.add(PayDaysResource.class);
        return classes;
    }
}
