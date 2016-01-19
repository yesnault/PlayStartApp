package controllers.account.settings;

import controllers.Secured;
import models.Token;
import models.User;
import play.Logger;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.account.settings.password;
import play.libs.mailer.Email;
import play.libs.mailer.MailerClient;

import javax.inject.Inject;

import java.net.MalformedURLException;

/**
 * User: yesnault
 * Date: 15/05/12
 */
@Security.Authenticated(Secured.class)
public class Password extends Controller {
    @Inject
    MailerClient mailerClient;

    /**
     * Password Page. Ask the user to change his password.
     *
     * @return index settings
     */
    public Result index() {
        return ok(password.render(User.findByEmail(request().username())));
    }

    /**
     * Send a mail with the reset link.
     *
     * @return password page with flash error or success
     */
    public Result runPassword() {
        User user = User.findByEmail(request().username());
        try {
            Token t = new Token();
            t.sendMailResetPassword(user,mailerClient);
            flash("success", Messages.get("resetpassword.mailsent"));
            return ok(password.render(user));
        } catch (MalformedURLException e) {
            Logger.error("Cannot validate URL", e);
            flash("error", Messages.get("error.technical"));
        }
        return badRequest(password.render(user));
    }
}
