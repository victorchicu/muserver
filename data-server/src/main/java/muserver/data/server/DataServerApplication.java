package muserver.data.server;

import muserver.base.BaseServer;
import muserver.data.server.configs.DataServerProperties;
import muserver.data.server.channels.DataServerChannelHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DataServerApplication extends BaseServer implements CommandLineRunner {
 @Autowired
 private DataServerProperties dataServerProperties;

 public static void main(String[] args) {
  SpringApplication.run(DataServerApplication.class, args);
 }

 @Override
 public void run(String... args) throws Exception {
  start(dataServerProperties.getPort(), new DataServerChannelHandler(dataServerProperties));
 }
}
