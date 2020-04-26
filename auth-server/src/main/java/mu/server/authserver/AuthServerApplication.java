package mu.server.authserver;

import mu.server.authserver.properties.AuthProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuthServerApplication implements CommandLineRunner {
    private final AuthProperties authProperties;

    @Autowired
    public AuthServerApplication(AuthProperties authProperties) {
        this.authProperties = authProperties;
    }

    public static void main(String[] args) {
        SpringApplication.run(AuthServerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(authProperties);
    }

}
