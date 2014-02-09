package models.utils;

import com.typesafe.plugin.MailerAPI;
import com.typesafe.plugin.MailerPlugin;
import play.Configuration;
import play.Logger;
import play.libs.Akka;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * Send a mail with Play20StartApp.
 * <p/>
 * User: yesnault
 * Date: 24/01/12
 */
public class Mail {

    /**
     * 1 second delay on sending emails
     */
    private static final int DELAY = 1;

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
     * Send a email, using Akka to offload it to an actor.
     *
     * @param envelop envelop to send
     */
    public static void sendMail(Mail.Envelop envelop) {
        EnvelopJob envelopJob = new EnvelopJob(envelop);
        final FiniteDuration delay = Duration.create(DELAY, TimeUnit.SECONDS);
        Akka.system().scheduler().scheduleOnce(delay, envelopJob, Akka.system().dispatcher());
    }

    static class EnvelopJob implements Runnable {
        Mail.Envelop envelop;

        public EnvelopJob(Mail.Envelop envelop) {
            this.envelop = envelop;
        }

        public void run() {
            MailerAPI email = play.Play.application().plugin(MailerPlugin.class).email();

            final Configuration root = Configuration.root();
            final String mailFrom = root.getString("mail.from");
            email.setFrom(mailFrom);
            email.setSubject(envelop.subject);
            for (String toEmail : envelop.toEmails) {
                email.setRecipient(toEmail);
                Logger.debug("Mail.sendMail: Mail will be sent to " + toEmail);
            }

            final String mailSign = root.getString("mail.sign");
            email.send(envelop.message + "\n\n " + mailSign,
                    envelop.message + "<br><br>--<br>" + mailSign);

            Logger.debug("Mail sent - SMTP:" + root.getString("smtp.host")
                    + ":" + root.getString("smtp.port")
                    + " SSL:" + root.getString("smtp.ssl")
                    + " user:" + root.getString("smtp.user")
                    + " password:" + root.getString("smtp.password"));
        }
    }
}
