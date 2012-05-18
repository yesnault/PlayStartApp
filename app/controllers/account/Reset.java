package controllers.account;

import models.ResetToken;
import models.User;
import models.utils.AppException;
import models.utils.Mail;
import org.apache.commons.mail.EmailException;
import play.Configuration;
import play.Logger;
import play.data.Form;
import play.data.validation.Constraints;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.account.reset.ask;
import views.html.account.reset.reset;
import views.html.account.reset.runAsk;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

/**
 * ResetToken password :
 * - ask for an email address.
 * - send a link pointing them to a reset page.
 * - show the reset page and set them reset it.
 *
 * <p/>
 * User: yesnault
 * Date: 20/01/12
 */
public class Reset extends Controller {

    public static class AskForm {
        @Constraints.Required
        public String email;
    }

    public static class ResetForm {
        @Constraints.Required
        public String password;
    }

    /**
     * Display the reset password form.
     *
     * @return reset password form
     */
    public static Result ask() {
        Form<AskForm> askForm = form(AskForm.class);
        return ok(ask.render(askForm));
    }

    /**
     * Run ask password.
     *
     * @return reset password form if error, runAsk render otherwise
     */
    public static Result runAsk() {
        Form<AskForm> askForm = form(AskForm.class).bindFromRequest();

        if (askForm.hasErrors()) {
            flash("error", Messages.get("signup.valid.email"));
            return badRequest(ask.render(askForm));
        }

        final String email = askForm.get().email;
        Logger.debug("runAsk: email = " + email);
        User user = User.findByEmail(email);
        Logger.debug("runAsk: user = " + user);

        if (user == null) {
            Logger.debug("No user found with email " + email);
            flash("error", Messages.get("error.notfound"));
            return badRequest(ask.render(askForm));
        }

        Logger.debug("Sending password reset link to user " + user);

        try {
            sendMailPasswordResetLink(user);
            return ok(runAsk.render());
        } catch (EmailException e) {
            Logger.debug("Cannot send email", e);
            flash("error", Messages.get("error.sending.email"));
            return badRequest(ask.render(askForm));
        } catch (MalformedURLException e) {
            Logger.error("Cannot validate URL", e);
            flash("error", Messages.get("error.technical"));
        }
        return badRequest(ask.render(askForm));
    }

    /**
     * Send the Email to confirm ask new password.
     *
     * @throws org.apache.commons.mail.EmailException
     *          Exception when sending mail
     */
    private static void sendMailPasswordResetLink(User user) throws EmailException, MalformedURLException {
        String subject = Messages.get("mail.reset.ask.subject");

        ResetToken resetToken = new ResetToken();
        resetToken.token = UUID.randomUUID().toString();
        resetToken.userId = user.id;
        resetToken.save();

        String externalServer = Configuration.root().getString("server.hostname");

        // Should use reverse routing here.
        String urlString = "http://" + externalServer + "/reset/" + resetToken.token;
        URL url = new URL(urlString); // validate the URL
        Logger.debug("sendMailPasswordResetLink: url = " + url);
        String message = Messages.get("mail.reset.ask.message", url.toString());
        Logger.debug("sendMailPasswordResetLink: message = " + message);

        Mail.Envelop envelop = new Mail.Envelop(subject, message, user.email);
        Mail.sendMail(envelop);
    }


    public static Result reset(String token) {

        if (token == null) {
            flash("error", Messages.get("error.technical"));
            Form<AskForm> askForm = form(AskForm.class);
            return badRequest(ask.render(askForm));
        }

        ResetToken resetToken = ResetToken.findByToken(token);
        if (resetToken == null) {
            flash("error", Messages.get("error.technical"));
            Form<AskForm> askForm = form(AskForm.class);
            return badRequest(ask.render(askForm));
        }

        if (resetToken.isExpired()) {
            resetToken.delete();
            flash("error", Messages.get("error.expiredresetlink"));
            Form<AskForm> askForm = form(AskForm.class);
            return badRequest(ask.render(askForm));
        }

        Form<ResetForm> resetForm = form(ResetForm.class);
        return ok(reset.render(resetForm, token));
    }

    /**
     *
     * @return reset password form
     */
    public static Result runReset(String token) {
        Form<ResetForm> resetForm = form(ResetForm.class).bindFromRequest();

        if (resetForm.hasErrors()) {
            flash("error", Messages.get("signup.valid.password"));
            return badRequest(reset.render(resetForm, token));
        }

        try {
            ResetToken resetToken = ResetToken.findByToken(token);
            if (resetToken == null) {
              flash("error", Messages.get("error.technical"));
              return badRequest(reset.render(resetForm, token));
            }

            if (resetToken.isExpired()) {
                resetToken.delete();
                flash("error", Messages.get("error.expiredresetlink"));
                return badRequest(reset.render(resetForm, token));
            }

            // check email
            User user = User.find.byId(resetToken.userId);
            if (user == null)   {
                // display no detail (email unknown for example) to
                // avoir check email by foreigner
                flash("error", Messages.get("error.technical"));
                return badRequest(reset.render(resetForm, token));
            }

            String password = resetForm.get().password;
            user.changePassword(password);

            // Send email saying that the password has just been changed.
            sendPasswordChanged(user);
            flash("success", Messages.get("resetpassword.success"));
            return ok(reset.render(resetForm, token));
        } catch (AppException e) {
            flash("error", Messages.get("error.technical"));
            return badRequest(reset.render(resetForm, token));
        } catch (EmailException e) {
            flash("error", Messages.get("error.technical"));
            return badRequest(reset.render(resetForm, token));
        }

    }

    /**
     * Send mail with the new password.
     *
     * @param user          user created
     * @throws EmailException Exception when sending mail
     */
    private static void sendPasswordChanged(User user) throws EmailException {
        String subject = Messages.get("mail.reset.confirm.subject");
        String message = Messages.get("mail.reset.confirm.message");
        Mail.Envelop envelop = new Mail.Envelop(subject, message, user.email);
        Mail.sendMail(envelop);
    }
}
