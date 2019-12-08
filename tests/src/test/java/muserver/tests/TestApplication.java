package muserver.tests;

import muserver.connectserver.ConnectServerApplication;
import muserver.dataserver.DataServerApplication;
import muserver.gameserver.GameServerApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackageClasses = { ConnectServerApplication.class, DataServerApplication.class, GameServerApplication.class })
public class TestApplication {
 public static void main(String... args) throws Exception {
  SpringApplication.run(TestApplication.class, args);
 }
}