package models.utils;

import play.Logger;

/**
 * User: yesnault
 * Date: 25/01/12
 */
public class ExceptionFactory {

    public static AppException getNewAppException(Exception exception) {
        Logger.error(exception.getMessage());
        AppException app = new AppException();
        app.initCause(exception);
        return app;
    }
}
