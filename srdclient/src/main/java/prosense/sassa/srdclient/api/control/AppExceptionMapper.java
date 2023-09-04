package prosense.sassa.srdclient.api.control;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Throwables;
import prosense.sassa.srdclient.api.entity.AppError;
import prosense.sassa.srdclient.api.entity.AppException;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Provider
public class AppExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
        final List<Throwable> causalChain = Throwables.getCausalChain(exception);
        if (causalChain.isEmpty()) {
            final Throwable rootCause = Throwables.getRootCause(exception);
            final AppError appError = AppError.builder().message(Optional.ofNullable(rootCause.getMessage()).orElse(rootCause.toString())).build();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).entity(appError).type(MediaType.APPLICATION_JSON).build();
        }
        Optional<JsonProcessingException> optionalJsonProcessingException = causalChain.stream().filter(t -> t instanceof JsonProcessingException).map(t -> (JsonProcessingException) t).findFirst();
        if (optionalJsonProcessingException.isPresent()) {
            final AppError appError = AppError.builder().message(optionalJsonProcessingException.get().getMessage()).build();
            return Response.status(422).header("validation-exception", "true").entity(appError).type(MediaType.APPLICATION_JSON).build();
        }
        Optional<ConstraintViolationException> optionalConstraintViolation = causalChain.stream().filter(t -> t instanceof ConstraintViolationException).map(t -> (ConstraintViolationException) t).findFirst();
        if (optionalConstraintViolation.isPresent()) {
            final List<String> collected = optionalConstraintViolation.get().getConstraintViolations().stream().map(m1 -> Optional.ofNullable(m1.getPropertyPath().toString()).map(p -> p.replaceAll("^[^.]+\\.[^.]+\\.?", "")).filter(f -> !f.isEmpty()).map(m2 -> m2 + " " + m1.getMessage()).orElse(m1.getMessage())).collect(Collectors.toList());
            final AppError appError = AppError.builder().messages(collected).build();
            return Response.status(422).header("validation-exception", "true").entity(appError).type(MediaType.APPLICATION_JSON).build();
        }
        final Optional<AppException> optionalApp = causalChain.stream().filter(t -> t instanceof AppException).map(t -> (AppException) t).findFirst();
        if (optionalApp.isPresent()) {
            return Response.status(optionalApp.get().getCode()).entity(optionalApp.get().toAppError()).type(MediaType.APPLICATION_JSON).build();
        }
        final AppError appError = AppError.builder().messages(causalChain.stream().map(Throwable::getMessage).collect(Collectors.toList())).build();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).entity(appError).type(MediaType.APPLICATION_JSON).build();
    }
}