package aero.loretta.client.exception;

import java.io.Serial;

public class ApiErrorException extends LorettaClientException {
    @Serial
    private static final long serialVersionUID = 2763960146557682043L;

    private final String correlationId;

    public ApiErrorException(String message, String correlationId) {
        super(message);
        this.correlationId = correlationId;
    }

    public ApiErrorException(String message, Throwable cause, String correlationId) {
        super(message, cause);
        this.correlationId = correlationId;
    }

    public ApiErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String correlationId) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.correlationId = correlationId;
    }
}
