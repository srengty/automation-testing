package gic.ip.lesson02.auth;

public interface AuthenticationService {
    Object attempt(String username, String password);
}
