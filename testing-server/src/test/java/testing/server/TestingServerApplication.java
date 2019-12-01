package testing.server;

import muserver.auth.server.AuthServerApplication;
import muserver.data.server.DataServerApplication;
import muserver.game.server.GameServerApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackageClasses = { AuthServerApplication.class, DataServerApplication.class, GameServerApplication.class })
public class TestingServerApplication {
 public static void main(String... args) throws Exception {
  SpringApplication.run(TestingServerApplication.class, args);
 }
}