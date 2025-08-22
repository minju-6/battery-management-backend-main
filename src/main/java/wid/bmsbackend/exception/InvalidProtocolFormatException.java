package wid.bmsbackend.exception;

public class InvalidProtocolFormatException extends RuntimeException {
    public InvalidProtocolFormatException(Throwable cause) {
        super(cause);
    }

    public InvalidProtocolFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
