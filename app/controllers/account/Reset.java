package controllers.account;

import models.User;
import models.utils.AppException;
import models.utils.Mail;
import org.apache.commons.mail.EmailException;
import play.data.Form;
import play.data.validation.Constraints;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.account.reset.ask;
import views.html.account.reset.confirm;
import views.html.account.reset.runAsk;

/**
 * Reset password :
 * - send ask mail with the new password.
 * - send the new password.
 * <p/>
 * User: yesnault
 * Date: 20/01/12
 */
public class Reset extends Controller {

    public static class Resetpassword {
        @Constraints.Required
        public String email;
    }

    /**
     * Display the reset password form.
     *
     * @return reset password form
     */
    public static Result ask() {
        Form<Resetpassword> resetForm = form(Resetpassword.class);
        return ok(ask.render(resetForm));
    }

    /**
     * Run ask password.
     *
     * @return reset password form if error, runAsk render otherwise
     */
    public static Result runAsk() {
        Form<Resetpassword> resetForm = form(Resetpassword.class).bindFromRequest();

        if (resetForm.hasErrors()) {
            return badRequest(ask.render(resetForm));
        }

        User user = User.findByEmail(resetForm.get().email);

        if (user != null) {
            try {
                sendMailAskResetPassword(user);
            } catch (EmailException e) {
                flash("error", Messages.get("error.sending.email"));
                return badRequest(ask.render(resetForm));
            }
        }

        return ok(runAsk.render());
    }

    /**
     * Send the Email to confirm ask new password.
     *
     * @throws org.apache.commons.mail.EmailException
     *          Exception when sending mail
     */
    private static void sendMailAskResetPassword(User user) throws EmailException {

        String subject = Messages.get("mail.reset.ask.subject");

        String url = "http://" + request().headers().get("HOST")[0];
        url += "/reset/confirm/" + user.email + "/" + user.passwordHash;
        String message = Messages.get("mail.reset.ask.message", url);

        Mail.Envelop envelop = new Mail.Envelop(subject, message, user.email);
        Mail.sendMail(envelop);
    }

    /**
     * Display the confirm page.
     *
     * @return reset password form
     */
    public static Result confirm(String email, String hash) {
        User user = User.findByEmail(email);
        if (user == null) {
            flash("error", Messages.get("error.technical"));
            return badRequest(confirm.render());
        }

        try {
            // check email and hash from request (GET HTTP)
            user = User.confirm(email, hash);
        } catch (AppException e) {
            flash("error", Messages.get("error.technical"));
            return badRequest(confirm.render());
        }

        if (user != null) {
            try {
                String clearPassword = user.resetPassword();
                user.save();
                sendMailNewPassword(user, clearPassword);
            } catch (AppException e) {
                flash("error", Messages.get("error.technical"));
                return badRequest(confirm.render());
            } catch (EmailException e) {
                flash("error", Messages.get("error.technical"));
                return badRequest(confirm.render());
            }
            flash("success", Messages.get("resetpassword.success"));
            return ok(confirm.render());
        } else {
            // display no detail (email unknown for example) to
            // avoir check email by foreigner
            flash("error", Messages.get("error.technical"));
            return badRequest(confirm.render());
        }
    }

    /**
     * Send mail with the new password.
     *
     * @param user          user created
     * @param clearPassword user clear password
     * @throws EmailException Exception when sending mail
     */
    private static void sendMailNewPassword(User user, String clearPassword) throws EmailException {
        String subject = Messages.get("mail.reset.confirm.subject");
        String message = Messages.get("mail.reset.confirm.message", clearPassword);
        Mail.Envelop envelop = new Mail.Envelop(subject, message, user.email);
        Mail.sendMail(envelop);
    }
}
