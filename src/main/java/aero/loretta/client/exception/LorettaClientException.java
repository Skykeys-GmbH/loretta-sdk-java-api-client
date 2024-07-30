package aero.loretta.client.exception;

import java.io.Serial;

public class LorettaClientException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 4585704452329243616L;

    public LorettaClientException() {
    }

    public LorettaClientException(String message) {
        super(message);
    }

    public LorettaClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public LorettaClientException(Throwable cause) {
        super(cause);
    }

    public LorettaClientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
