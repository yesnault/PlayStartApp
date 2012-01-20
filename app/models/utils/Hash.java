package models.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * Hash util class.
 * <p/>
 * User: yesnault
 * Date: 25/01/12
 */
public class Hash {

    /**
     * Get a hash string from a clear string and a salt.
     *
     * @param salt        the salt
     * @param clearString the clear string
     * @return a hash of the clear string
     * @throws AppException APP Exception, from NoSuchAlgorithmException
     */
    public static String getHashString(int salt, String clearString) throws AppException {

        StringBuilder toHash = new StringBuilder();
        toHash.append(salt);
        toHash.append(clearString);

        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.reset();
        } catch (NoSuchAlgorithmException e) {
            throw ExceptionFactory.getNewAppException(e);
        }

        md.update(toHash.toString().getBytes());
        byte byteData[] = md.digest();

        //convert the byte to hex format
        StringBuilder sb = new StringBuilder();

        for (byte aByteData : byteData) {
            sb.append(Integer.toString((aByteData & 0xff) + 0x100, 16).substring(1));
        }

        // and we keep a substring :)
        return sb.substring(5, 55);
    }

    /**
     * Get a new salt, between 1 and 9000000.
     *
     * @return a salt
     */
    public static int getSalt() {
        int lower = 1;
        int higher = 9000000;

        return (int) (Math.random() * (higher - lower)) + lower;
    }

    /**
     * Return a random String
     *
     * @return
     */
    public static String getRandomString() {
        return UUID.randomUUID().toString();
    }
}
