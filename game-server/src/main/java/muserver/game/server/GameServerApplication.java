package muserver.game.server;

import muserver.base.server.BaseServer;
import muserver.game.server.utils.KeyReader;
import muserver.game.server.channels.GameServerChannelHandler;
import muserver.game.server.configs.GameServerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.ResourceUtils;

import java.io.File;

@SpringBootApplication
public class GameServerApplication  extends BaseServer implements CommandLineRunner {
 @Autowired
 private GameServerProperties gameServerProperties;

 public static void main(String[] args) {
  SpringApplication.run(GameServerApplication.class, args);
 }

 @Override
 public void run(String... args) throws Exception {
  File enc2Dat = ResourceUtils.getFile("classpath:Enc2.dat"), dec1Dat = ResourceUtils.getFile("classpath:Dec1.dat");
  KeyReader.readEnc2DatFile(enc2Dat.toPath());
  KeyReader.readDec1DatFile(dec1Dat.toPath());
  start(gameServerProperties.getPort(), new GameServerChannelHandler(gameServerProperties));
 }
}