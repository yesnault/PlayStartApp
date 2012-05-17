package models;

import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Calendar;
import java.util.Date;

/**
 * @author wsargent
 * @since 5/15/12
 */
@Entity
public class ResetToken extends Model {
    // Reset tokens will expire after a day.
    private static final int EXPIRATION_DAYS = 1;

    @Id
    public String token;

    @Constraints.Required
    @Formats.NonEmpty
    public Long userId;

    @Formats.DateTime(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date dateCreation;

    // -- Queries
    @SuppressWarnings("unchecked")
    public static Model.Finder<String, ResetToken> find = new Finder(String.class, ResetToken.class);


    public static ResetToken findByToken(String token) {
        return find.where().eq("token", token).findUnique();
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
}
