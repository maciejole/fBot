package pl.hycom.pip.messanger.exception;

/**
 * Created by Monia on 2017-05-28.
 */
public class PasswordEncryptionException extends Exception {

    private static final long serialVersionUID = 108346106636295453L;

    /**
     * Konstruktor służący do tworzenia wyjątku.
     *
     * @param message
     *            opis wyjątku
     */
    public PasswordEncryptionException(String message) {
        super(message);
    }
}
