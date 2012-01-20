package models.utils;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
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
     * Init globals mail parameters.
     *
     * @return Email object initialized
     */
    private static Email init() {
        Email email = new HtmlEmail();

        email.setHostName(Configuration.root().getString("mail.hostname"));
        email.setSmtpPort(Configuration.root().getInt("mail.port"));
        if (Configuration.root().getString("mail.user") != null) {
            email.setAuthenticator(new DefaultAuthenticator(Configuration.root().getString("mail.user"),
                    Configuration.root().getString("mail.password")));
        }

        email.setTLS(Configuration.root().getBoolean("mail.tls"));
        return email;
    }

    /**
     * Send a email.
     *
     * @param envelop envelop to send
     * @throws EmailException configuration mail exception
     */
    public static void sendMail(Mail.Envelop envelop) throws EmailException {


        Email email = init();
        try {
            email.setFrom(Configuration.root().getString("mail.from"));
            email.setSubject(envelop.subject);
            ((HtmlEmail) email).setHtmlMsg(envelop.message + "<br><br>--<br>" + Configuration.root().getString("mail.sign"));
            for (String toEmail : envelop.toEmails) {
                email.addTo(toEmail);
            }
            email.send();
        } catch (EmailException e) {
            Logger.error(e.getMessage());
            throw e;
        }
    }
}
