package lab01;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.junit.Test;

public class AuthenticationTest {
    @Test
    public void testLoginEmptyFields() throws IOException, InterruptedException{
        String url = "http://localhost:3000/auth/login";
        String email = "john@mail.com";
        String pwd= "changeme";
        HttpClient http = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url)).POST(
            HttpRequest.BodyPublishers.ofString("{\"email\":\""+email+"\",\"password\":\""+pwd+"\"}")
        )
        .header("Content-Type", "application/json")
        .build();
        HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode(),200);
    }
}
