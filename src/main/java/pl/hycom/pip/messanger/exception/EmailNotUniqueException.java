package pl.hycom.pip.messanger.exception;

/**
 * Created by Monia on 2017-05-27.
 */
public class EmailNotUniqueException extends Exception {

    private static final long serialVersionUID = 8779583852449852654L;

    public EmailNotUniqueException(Throwable cause) {
        super(cause);
    }

    public EmailNotUniqueException() {
        super();
    }

    public EmailNotUniqueException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public EmailNotUniqueException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailNotUniqueException(String message) {
        super(message);
    }

}
