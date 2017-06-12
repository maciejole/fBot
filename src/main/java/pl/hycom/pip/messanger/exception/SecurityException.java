package pl.hycom.pip.messanger.exception;

public class SecurityException extends Exception {

    private static final long serialVersionUID = -8016225481025916988L;

    public SecurityException() {
        super();
    }

    public SecurityException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public SecurityException(String message, Throwable cause) {
        super(message, cause);
    }

    public SecurityException(String message) {
        super(message);
    }

    public SecurityException(Throwable cause) {
        super(cause);
    }

}
