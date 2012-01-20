package models.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * User: yesnault
 * Date: 25/01/12
 */
public class HashTest {

    @Test
    public void getHashString() throws AppException {
        int salt = Hash.getSalt();
        String password = Hash.getHashString(salt, "fooTest");

        Assert.assertNotNull(salt);
        Assert.assertNotNull(password);

        String password2 = Hash.getHashString(salt, "fooTest");

        Assert.assertEquals(password, password2);
        Assert.assertEquals(password.length(), 50);

    }
}
