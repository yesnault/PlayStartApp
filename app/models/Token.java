package models;

import models.utils.Mail;
import play.Configuration;
import play.Logger;
import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import play.i18n.Messages;
import play.libs.mailer.Email;
import play.libs.mailer.MailerClient;

import javax.annotation.Nullable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * @author wsargent
 * @since 5/15/12
 */
@Entity
public class Token extends Model {
    MailerClient mailerClient;

    // Reset tokens will expire after a day.
    private static final int EXPIRATION_DAYS = 1;


    public enum TypeToken {
        password("reset"), email("email");
        private String urlPath;

        TypeToken(String urlPath) {
            this.urlPath = urlPath;
        }

    }

    public void setMailerClient(MailerClient mailerClient) {
        this.mailerClient = mailerClient;
    }

    @Id
    public String token;

    @Constraints.Required
    @Formats.NonEmpty
    public Long userId;

    @Constraints.Required
    @Enumerated(EnumType.STRING)
    public TypeToken type;

    @Formats.DateTime(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date dateCreation;

    @Constraints.Required
    @Formats.NonEmpty
    public String email;

    // -- Queries
    @SuppressWarnings("unchecked")
    public static Model.Finder<String, Token> find = new Finder(String.class, Token.class);

    /**
     * Retrieve a token by id and type.
     *
     * @param token token Id
     * @param type  type of token
     * @return a resetToken
     */
    public static Token findByTokenAndType(String token, TypeToken type) {
        return find.where().eq("token", token).eq("type", type).findUnique();
    }

    /**
     * @return true if the reset token is too old to use, false otherwise.
     */
    public boolean isExpired() {
        return dateCreation != null && dateCreation.before(expirationTime());
    }

    /**
     * @return a date before which the password link has expired.
     */
    private Date expirationTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, -EXPIRATION_DAYS);
        return cal.getTime();
    }

    /**
     * Return a new Token.
     *
     * @param user  user
     * @param type  type of token
     * @param email email for a token change email
     * @return a reset token
     */
    private Token getNewToken(User user, TypeToken type, String email) {
        Token token = new Token();
        token.token = UUID.randomUUID().toString();
        token.userId = user.id;
        token.type = type;
        token.email = email;
        token.save();
        return token;
    }

    /**
     * Send the Email to confirm ask new password.
     *
     * @param user the current user
     * @throws java.net.MalformedURLException if token is wrong.
     */
    public void sendMailResetPassword(User user) throws MalformedURLException {
        sendMail(user, TypeToken.password, null);
    }

    /**
     * Send the Email to confirm ask new password.
     *
     * @param user  the current user
     * @param email email for a change email token
     * @throws java.net.MalformedURLException if token is wrong.
     */
    public void sendMailChangeMail(User user, @Nullable String email) throws MalformedURLException {
        sendMail(user, TypeToken.email, email);
    }

    /**
     * Send the Email to confirm ask new password.
     *
     * @param user  the current user
     * @param type  token type
     * @param email email for a change email token
     * @throws java.net.MalformedURLException if token is wrong.
     */
    private void sendMail(User user, TypeToken type, String email) throws MalformedURLException {

        Token token = getNewToken(user, type, email);
        String externalServer = Configuration.root().getString("server.hostname");

        String subject = null;
        String message = null;
        String toMail = null;

        // Should use reverse routing here.
        String urlString = urlString = "http://" + externalServer + "/" + type.urlPath + "/" + token.token;
        URL url = new URL(urlString); // validate the URL

        switch (type) {
            case password:
                subject = Messages.get("mail.reset.ask.subject");
                message = Messages.get("mail.reset.ask.message", url.toString());
                toMail = user.email;
                break;
            case email:
                subject = Messages.get("mail.change.ask.subject");
                message = Messages.get("mail.change.ask.message", url.toString());
                toMail = token.email; // == email parameter
                break;
        }

        Logger.debug("sendMailResetLink: url = " + url);
        Mail.Envelop envelop = new Mail.Envelop(subject, message, toMail);
        Mail mailer = new Mail(mailerClient);
        mailer.sendMail(envelop);
    }

}
