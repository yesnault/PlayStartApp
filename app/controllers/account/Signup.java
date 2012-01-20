package controllers.account;

import models.User;
import models.utils.AppException;
import models.utils.Mail;
import org.apache.commons.mail.EmailException;
import play.Logger;
import play.data.Form;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.account.signup.confirm;
import views.html.account.signup.create;
import views.html.account.signup.created;

/**
 * Signup to Play20StartApp : save and send confirm mail.
 * <p/>
 * User: yesnault
 * Date: 31/01/12
 */
public class Signup extends Controller {

    /**
     * Display the create form.
     *
     * @return create form
     */
    public static Result create() {
        Form<User> userForm = form(User.class);
        return ok(create.render(userForm));
    }

    /**
     * Save the new user.
     *
     * @return Successfull page or created form if bad
     */
    public static Result save() {
        Form<User> userForm = form(User.class).bindFromRequest();

        if (userForm.hasErrors()) {
            return badRequest(create.render(userForm));
        }

        User user = userForm.get();
        Result resultError = checkBeforeSave(userForm, user);

        if (resultError != null) {
            return resultError;
        }

        try {
            user.save();
        } catch (Exception e) {
            Logger.error(e.getMessage());
            flash("error", Messages.get("error.technical"));
            return badRequest(create.render(userForm));
        }

        try {
            sendMailAskForConfirmation(user);
        } catch (EmailException e) {
            flash("error", Messages.get("error.sending.email"));
            return badRequest(create.render(userForm));
        }

        return ok(created.render());
    }

    /**
     * Check if the username / email already exist.
     *
     * @param userForm User Form submitted
     * @param user     user class
     * @return Result if there was a problem, null otherwise
     */
    private static Result checkBeforeSave(Form<User> userForm, User user) {
        // Check unique email
        if (User.findByEmail(user.email) != null) {
            flash("error", Messages.get("error.email.already.exist"));
            return badRequest(create.render(userForm));
        }

        // Check unique username
        if (User.findByEmail(user.email) != null) {
            flash("error", Messages.get("error.email.already.exist"));
            return badRequest(create.render(userForm));
        }

        try {
            user.initPassword();
        } catch (AppException e) {
            flash("error", Messages.get("error.technical"));
            return badRequest(create.render(userForm));
        }
        return null;
    }

    /**
     * Send the welcome Email with the link to confirm.
     *
     * @param user user created
     * @throws EmailException Exception when sending mail
     */
    private static void sendMailAskForConfirmation(User user) throws EmailException {

        String subject = Messages.get("mail.confirm.subject");

        String url = "http://" + request().headers().get("HOST")[0];
        url += "/confirm/" + user.email + "/" + user.passwordHash;
        String message = Messages.get("mail.confirm.message", url);

        Mail.Envelop envelop = new Mail.Envelop(subject, message, user.email);
        Mail.sendMail(envelop);
    }

    /**
     * Valid an account with the url in the confirm mail.
     *
     * @param email Email
     * @param hash  Hash Password
     * @return Confirmationpage
     */
    public static Result confirm(String email, String hash) {

        User user = User.findByEmail(email);
        if (user == null) {
            flash("error", Messages.get("error.unknown.email"));
            return badRequest(confirm.render());
        }

        if (user.validated) {
            flash("error", Messages.get("error.account.already.validated"));
            return badRequest(confirm.render());
        }

        try {
            user = User.confirm(email, hash);
        } catch (AppException e) {
            flash("error", Messages.get("error.technical"));
            return badRequest(confirm.render());
        }

        if (user != null) {
            try {
                sendMailConfirmation(user);
            } catch (EmailException e) {
                flash("error", Messages.get("error.sending.confirm.email"));
            }
            flash("success", Messages.get("account.successfully.validated"));
            return ok(confirm.render());
        } else {
            flash("error", Messages.get("error.confirm"));
            return badRequest(confirm.render());
        }
    }

    /**
     * Send the confirm mail.
     *
     * @param user user created
     * @throws EmailException Exception when sending mail
     */
    private static void sendMailConfirmation(User user) throws EmailException {
        String subject = Messages.get("mail.welcome.subject");
        String message = Messages.get("mail.welcome.message");
        Mail.Envelop envelop = new Mail.Envelop(subject, message, user.email);
        Mail.sendMail(envelop);
    }
}
