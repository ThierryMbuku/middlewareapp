package prosense.sassa.mqclient.api.control;

import javax.inject.Qualifier;
import javax.annotation.Priority;
import java.lang.annotation.*;

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Context {
}
