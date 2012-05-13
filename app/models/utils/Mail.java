package models.utils;

import com.typesafe.plugin.MailerAPI;
import com.typesafe.plugin.MailerPlugin;
import play.Configuration;
import play.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Send a mail with Play20StartApp.
 * <p/>
 * User: yesnault
 * Date: 24/01/12
 */
public class Mail {

    /**
     * Envelop to prepare.
     */
    public static class Envelop {
        private String subject;
        private String message;
        private List<String> toEmails;

        /**
         * Constructor of Envelop.
         *
         * @param subject  the subject
         * @param message  a message
         * @param toEmails list of emails adress
         */
        public Envelop(String subject, String message, List<String> toEmails) {
            this.subject = subject;
            this.message = message;
            this.toEmails = toEmails;
        }

        public Envelop(String subject, String message, String email) {
            this.message = message;
            this.subject = subject;
            this.toEmails = new ArrayList<String>();
            this.toEmails.add(email);
        }
    }


    /**
     * Send a email.
     *
     * @param envelop envelop to send
     */
    public static void sendMail(Mail.Envelop envelop) {
        MailerAPI email = play.Play.application().plugin(MailerPlugin.class).email();

        email.addFrom(Configuration.root().getString("mail.from"));
        email.setSubject(envelop.subject);
        for (String toEmail : envelop.toEmails) {
            email.addRecipient(toEmail);
            Logger.debug("Mail will be send to " + toEmail);
        }
        email.send(envelop.message + "\n\n " + Configuration.root().getString("mail.sign"),
                envelop.message + "<br><br>--<br>" + Configuration.root().getString("mail.sign"));

        Logger.debug("Mail sent - SMTP:" + Configuration.root().getString("smtp.host")
                + ":" + Configuration.root().getString("smtp.port")
                + " SSL:" + Configuration.root().getString("smtp.ssl")
                + " user:" + Configuration.root().getString("smtp.user")
                + " password:" + Configuration.root().getString("smtp.password"));
    }
}
