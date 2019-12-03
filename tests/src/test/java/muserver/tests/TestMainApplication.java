package muserver.tests;

import muserver.connectserver.ConnectServerApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackageClasses = { ConnectServerApplication.class })
public class TestMainApplication {
 public static void main(String... args) throws Exception {
  SpringApplication.run(TestMainApplication.class, args);
 }
}