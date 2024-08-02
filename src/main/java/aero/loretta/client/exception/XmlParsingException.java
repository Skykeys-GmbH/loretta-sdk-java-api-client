package aero.loretta.client.exception;

import java.io.Serial;

public class XmlParsingException extends LorettaClientException {
    @Serial
    private static final long serialVersionUID = -7226201216569164931L;

    public XmlParsingException() {
    }

    public XmlParsingException(String message) {
        super(message);
    }

    public XmlParsingException(String message, Throwable cause) {
        super(message, cause);
    }

    public XmlParsingException(Throwable cause) {
        super(cause);
    }

    public XmlParsingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
