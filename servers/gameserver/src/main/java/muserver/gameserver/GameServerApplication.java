package muserver.gameserver;

import muserver.baseserver.BaseServer;
import muserver.gameserver.channels.GameServerChannelHandler;
import muserver.gameserver.configs.GameServerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GameServerApplication  extends BaseServer implements CommandLineRunner {
 @Autowired
 private GameServerProperties gameServerProperties;

 public static void main(String[] args) {
  SpringApplication.run(GameServerApplication.class, args);
 }

 @Override
 public void run(String... args) throws Exception {
  start(gameServerProperties.getPort(), new GameServerChannelHandler(gameServerProperties));
 }
}
