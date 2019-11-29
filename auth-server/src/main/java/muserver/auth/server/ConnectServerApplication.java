package muserver.auth.server;

import muserver.auth.server.channels.ConnectServerChannelHandler;
import muserver.auth.server.configs.ConnectServerProperties;
import muserver.base.server.BaseServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ConnectServerApplication extends BaseServer implements CommandLineRunner {
 @Autowired
 private ConnectServerProperties connectServerProperties;

 public static void main(String[] args) {
  SpringApplication.run(ConnectServerApplication.class, args);
 }

 @Override
 public void run(String... args) throws Exception {
  start(connectServerProperties.getPort(), new ConnectServerChannelHandler(connectServerProperties));
 }
}