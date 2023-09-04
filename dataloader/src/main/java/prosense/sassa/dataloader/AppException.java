package prosense.sassa.dataloader;

import com.google.common.base.Throwables;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class AppException extends RuntimeException {
    private Response.StatusType status;

    AppException(Throwable messages, Response.StatusType status) {
        super(messages);
        this.status = status;
    }

    public static AppExceptionBuilder builder() {
        return new AppExceptionBuilder();
    }

    public Response.StatusType getStatus() {
        return status;
    }

    public int getCode() {
        return getStatus().getStatusCode();
    }

    public AppError toAppError() {
        return AppError.builder().messages(Throwables.getCausalChain(this.getCause()).stream().map(Throwable::getMessage).collect(Collectors.toList())).build();
    }

    public static class AppExceptionBuilder {
        private ArrayList<String> messages;
        private Response.StatusType status;
        private Throwable throwable;

        AppExceptionBuilder() {
        }

        public AppExceptionBuilder throwable(Throwable throwable) {
            this.throwable = throwable;
            return this;
        }

        public AppExceptionBuilder message(String message) {
            if (this.messages == null) {
                this.messages = new ArrayList<>();
            }
            this.messages.add(message);
            return this;
        }

        public AppExceptionBuilder messages(Collection<? extends String> messages) {
            if (this.messages == null) {
                this.messages = new ArrayList<>();
            }

            this.messages.addAll(messages);
            return this;
        }

        public AppExceptionBuilder badRequest400() {
            this.status = Status.BAD_REQUEST;
            return this;
        }

        public AppExceptionBuilder unauthorised401() {
            this.status = Status.UNAUTHORIZED;
            return this;
        }

        public AppExceptionBuilder forbidden403() {
            this.status = Status.FORBIDDEN;
            return this;
        }

        public AppExceptionBuilder notFound404() {
            this.status = Status.NOT_FOUND;
            return this;
        }

        public AppExceptionBuilder methodNotAllowed405() {
            this.status = Status.METHOD_NOT_ALLOWED;
            return this;
        }

        public AppExceptionBuilder unsupportedMediaType415() {
            this.status = Status.UNSUPPORTED_MEDIA_TYPE;
            return this;
        }

        public AppExceptionBuilder unprocessableEntity422() {
            this.status = new Response.StatusType() {
                @Override
                public int getStatusCode() {
                    return 422;
                }

                @Override
                public Status.Family getFamily() {
                    return Status.Family.CLIENT_ERROR;
                }

                @Override
                public String getReasonPhrase() {
                    return "Unprocessable Entity";
                }
            };
            return this;
        }

        public AppExceptionBuilder internalServerError500() {
            this.status = Status.INTERNAL_SERVER_ERROR;
            return this;
        }

        public AppExceptionBuilder status(Response.StatusType status) {
            this.status = status;
            return this;
        }

        public AppException build() {
            if (this.messages == null) {
                this.messages = new ArrayList<>();
            }
            if (this.throwable != null) {
                Throwables.getCausalChain(this.throwable).stream().map(Throwable::getMessage).forEach(messages::add);
            }
            Collections.reverse(messages);
            this.throwable = messages.stream().map(Throwable::new).reduce((t1, t2) -> new Throwable(t2.getMessage(), t1)).orElse(new Throwable("unknown"));
            this.status = this.status == null ? Status.INTERNAL_SERVER_ERROR : this.status;
            return new AppException(this.throwable, this.status);
        }

        public String toString() {
            return "AppExceptionBuilder(messages=" + this.messages + ", status=" + this.status + ")";
        }
    }
}
