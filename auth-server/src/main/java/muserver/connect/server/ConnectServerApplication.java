package muserver.connect.server;

import muserver.base.BaseServer;
import muserver.connect.server.channels.ConnectServerChannelHandler;
import muserver.connect.server.configs.ConnectServerProperties;
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