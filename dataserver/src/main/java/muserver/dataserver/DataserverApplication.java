package muserver.dataserver;

import muserver.common.BaseServer;
import muserver.dataserver.handlers.DataServerChannelHandler;
import muserver.dataserver.components.DataserverProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DataserverApplication extends BaseServer implements CommandLineRunner {
 @Autowired
 private DataserverProperties dataserverProperties;

 public static void main(String[] args) {
  SpringApplication.run(DataserverApplication.class, args);
 }

 @Override
 public void run(String... args) throws Exception {
  start(dataserverProperties.getPort(), new DataServerChannelHandler(dataserverProperties));
 }
}
