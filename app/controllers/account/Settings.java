package controllers.account;

import controllers.Secured;
import models.ResetWork;
import models.User;
import play.Logger;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.account.settings.password;

import java.net.MalformedURLException;

/**
 * User: yesnault
 * Date: 15/05/12
 */
@Security.Authenticated(Secured.class)
public class Settings extends Controller {

    /**
     * Main page Settings
     *
     * @return index Settings
     */
    public static Result index() {
        return password();
    }

    /**
     * Password Page. Ask the user to change his password.
     *
     * @return Password Page
     */
    public static Result password() {
        return ok(password.render(User.findByEmail(request().username())));
    }

    /**
     * Send a mail with the reset link.
     *
     * @return password page with flash error or success
     */
    public static Result runPassword() {
        User user = User.findByEmail(request().username());
        try {
            ResetWork.sendMailPasswordResetLink(user);
            flash("success", Messages.get("resetpassword.mailsent"));
            return ok(password.render(user));
        } catch (MalformedURLException e) {
            Logger.error("Cannot validate URL", e);
            flash("error", Messages.get("error.technical"));
        }
        return badRequest(password.render(user));
    }
}
