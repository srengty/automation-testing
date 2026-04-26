package gic.ip.lesson02;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import gic.ip.lesson02.auth.AuthenticationService;
import gic.ip.lesson02.authImpl.MockAuthentication;

public class AuthenticationTest {
    AuthenticationService getAuthService() {
        return new MockAuthentication();
    }

    @Test
    public void testLoginSuccess() {
        System.out.println("Login with user pagna and password 123456");
        assertNotNull(getAuthService().attempt("pagna", "123456"));
    }
    @Test
    public void testLoginFailedWrongUsername() {
        System.out.println("Login with user naiea and password 123456");
        assertNull(getAuthService().attempt("naiea", "123456"));
    }
    @Test
    public void testLoginFailedWrongPassword() {
        System.out.println("Login with user naieang and password 12356");
        assertNull(getAuthService().attempt("naieang", "12356"));
    }
    @Test
    public void testLoginFailedEmptyFields() {
        System.out.println("Login with empty username and password ");
        assertNull(getAuthService().attempt("", ""));
    }
    @Test
    public void testLoginFailIsBanned() {
        System.out.println("Login with user naieang and password 123456");
        assertNull(getAuthService().attempt("naieang", "123456"));
    }
    @Test
    public void testLoginFailSQLInjection() {
        // QUERY: SELECT * FROM users WHERE username="pagna" AND password="123456"
        // QUERY: SELECT * FROM users WHERE username="admin" OR "1"="1" AND password="123" OR "1"="1"
        System.out.println("Login with user admin\" OR \"1\"=\"1 and password 123");
        assertNull(getAuthService().attempt("admin\" OR \"1\"=\"1", "123\" OR \"1\"=\"1"));
    }
}
