package aero.loretta.client.exception;

public class XmlParsingException extends LorettaClientException {
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
