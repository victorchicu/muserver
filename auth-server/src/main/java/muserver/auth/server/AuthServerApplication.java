package muserver.auth.server;

import muserver.auth.server.channels.AuthServerChannelHandler;
import muserver.auth.server.configs.AuthServerProperties;
import muserver.server.base.BaseServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuthServerApplication extends BaseServer implements CommandLineRunner {
 @Autowired
 private AuthServerProperties authServerProperties;

 public static void main(String[] args) {
  SpringApplication.run(AuthServerApplication.class, args);
 }

 @Override
 public void run(String... args) throws Exception {
  start(authServerProperties.getPort(), new AuthServerChannelHandler(authServerProperties));
 }
}