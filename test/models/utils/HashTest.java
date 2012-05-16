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
        String password = Hash.createPassword("fooTest");
        Assert.assertNotNull(password);

        boolean matches = Hash.checkPassword("fooTest", password);

        Assert.assertTrue("Password does not match but should match", matches);

        boolean badPassword = Hash.checkPassword("badPassword", password);

        Assert.assertFalse("Password matches but should not match", badPassword);
    }
}
