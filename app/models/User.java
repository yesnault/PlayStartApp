package models;

import models.utils.AppException;
import models.utils.Hash;
import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * User: yesnault
 * Date: 20/01/12
 */
@Entity
public class User extends Model {

    @Id
    public Long id;

    @Constraints.Required
    @Formats.NonEmpty
    @Column(unique = true)
    public String email;

    @Constraints.Required
    @Formats.NonEmpty
    @Column(unique = true)
    public String fullname;

    @Constraints.Required
    transient
    public String password;

    public String passwordHash;

    public int passwordSalt;

    @Formats.DateTime(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date dateCreation;

    @Formats.NonEmpty
    public Boolean validated = false;

    // -- Queries
    public static Model.Finder<String, User> find = new Model.Finder(String.class, User.class);

    /**
     * Retrieve a user from an email.
     *
     * @param email email to search
     * @return a user
     */
    public static User findByEmail(String email) {
        return find.where().eq("email", email).findUnique();
    }

    /**
     * Retrieve a user from a fullname.
     *
     * @param fullname Full name
     * @return a user
     */
    public static User findByFullname(String fullname) {
        return find.where().eq("fullname", fullname).findUnique();
    }


    /**
     * Init the salt and hash password and detroy the clear password.
     *
     * @throws models.utils.AppException App Exception
     */
    public void initPassword() throws AppException {
        this.passwordSalt = Hash.getSalt();
        this.passwordHash = Hash.getHashString(this.passwordSalt, this.password);
        this.password = null;
    }

    /**
     * Reset the password with a new password.
     * Call to initPassword in internal.
     *
     * @return clear password generated
     * @throws AppException App Exception
     */
    public String resetPassword() throws AppException {
        String clearPassword = Hash.getRandomString();
        this.password = clearPassword;
        this.initPassword();
        return clearPassword;
    }

    /**
     * Authenticate a User, from a email and clear password.
     *
     * @param email         email
     * @param clearPassword clear password
     * @return User if authenticated, null otherwise
     * @throws AppException App Exception
     */
    public static User authenticate(String email, String clearPassword) throws AppException {

        // get the user with email only to keep the salt password
        User user = find.where().eq("email", email).findUnique();
        if (user != null) {
            // get the hash password from the salt + clear password
            String passwordHash = Hash.getHashString(user.passwordSalt, clearPassword);

            // try to get the user with email and Hash Password
            return authenticateWithHash(email, passwordHash);
        }
        return null;
    }

    /**
     * Confirm an account.
     *
     * @param email        email
     * @param hashPassword hash password
     * @return User or null if not validated
     * @throws AppException App Exception
     */
    public static User confirm(String email, String hashPassword) throws AppException {
        User user = authenticateWithHash(email, hashPassword);

        if (user != null) {
            user.validated = true;
            user.save();
        }
        return user;
    }

    /**
     * Try to retrieve a user from an email and a hash password
     *
     * @param email        email
     * @param passwordHash Hash Password
     * @return a user or null if user unknown
     */
    private static User authenticateWithHash(String email, String passwordHash) {
        // try to get the user with email and Hash Password
        return find.where()
                .eq("email", email)
                .eq("passwordHash", passwordHash)
                .findUnique();
    }

}
