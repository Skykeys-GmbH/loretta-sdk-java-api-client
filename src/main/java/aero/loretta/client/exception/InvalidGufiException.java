package aero.loretta.client.exception;

import java.io.Serial;

public class InvalidGufiException extends LorettaClientException {
    @Serial
    private static final long serialVersionUID = 4931672510288720047L;

    public InvalidGufiException() {
    }

    public InvalidGufiException(String message) {
        super(message);
    }

    public InvalidGufiException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidGufiException(Throwable cause) {
        super(cause);
    }

    public InvalidGufiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
