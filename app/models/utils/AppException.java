package models.utils;

/**
 * User: yesnault
 * Date: 25/01/12
 */
public class AppException extends Exception {

    public AppException() {
    }

    public AppException(String message) {
        super(message);
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
    }

    public AppException(Throwable cause) {
        super(cause);
    }
}
