package prosense.sassa.oaamwrapper.api.control;

import javax.annotation.Priority;
import javax.interceptor.Interceptor;
import javax.interceptor.InterceptorBinding;
import java.lang.annotation.*;

@Inherited
@InterceptorBinding
@Priority(Interceptor.Priority.APPLICATION)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface Log {
}
