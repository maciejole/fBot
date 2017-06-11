package pl.hycom.pip.messanger.exception;

/**
 * Created by Monia on 2017-05-27.
 */
public class EmailNotUniqueException extends Exception {

    private static final long serialVersionUID = 8779583852449852654L;

    public EmailNotUniqueException(Throwable cause) {
        super(cause);
    }
}
