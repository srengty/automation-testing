package lab01;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class UserTest {
    @Test
    public void testChangeEmail(){
        User user = new User();
        assertNull(user.getEmail());
        user.setEmail("johny@gmail.com");
        assertEquals("johny@gmail.com", user.getEmail());
    }
}
