package models;

import models.utils.Mail;
import play.Configuration;
import play.Logger;
import play.i18n.Messages;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

/**
 * User: yesnault
 * Date: 20/06/12
 */
public class ResetWork {

    /**
     * Send the Email to confirm ask new password.
     *
     * @param user the current user
     * @throws MalformedURLException if token is wrong.
     */
    public static void sendMailPasswordResetLink(User user) throws MalformedURLException {
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
}
