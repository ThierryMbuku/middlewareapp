package prosense.sassa.sapo.api.control;

import org.slf4j.Logger;

import javax.annotation.Priority;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.Collectors;

@Log
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
@Dependent
public class LogInterceptor implements Serializable {

    @Inject
    @App
    private Logger logger;

    @AroundInvoke
    public Object intercept(InvocationContext context) throws Throwable {
        try {
            logger.debug("{}|start|{} - {}", context.getMethod().getName(), context.getTarget().getClass().getName(), Arrays.asList(context.getParameters()).stream().map(Object::toString).collect(Collectors.joining(", ", "[", "]")));
            Object result = context.proceed();
            logger.debug("{}|end|{} - {}", context.getMethod().getName(), context.getTarget().getClass().getName(), result);
            return result;
        } catch (Throwable t) {
            logger.debug("{}|end|{} - {}", context.getMethod().getName(), context.getTarget().getClass().getName(), t.getMessage());
            throw t;
        }
    }

}
