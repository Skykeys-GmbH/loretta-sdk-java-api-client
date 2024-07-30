package aero.loretta.client.exception;

public class InvalidGufiException extends LorettaClientException {
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
