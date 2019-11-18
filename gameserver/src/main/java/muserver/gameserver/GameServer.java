package muserver.gameserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import muserver.common.AbstractServer;
import muserver.common.channels.AbstractChannelInitializer;
import muserver.common.objects.GameServerConfigs;
import muserver.gameserver.channels.GameServerChannelInitializer;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.nio.charset.Charset;

public class GameServer extends AbstractServer {
 private static final Logger logger = LogManager.getLogger(GameServer.class);
 private static final ObjectMapper objectMapper = new ObjectMapper();

 public GameServer(AbstractChannelInitializer initializer) {
  super(initializer);
 }

 public static void main(String[] args) {
  GameServer gameServer = null;
  try (InputStream stream = GameServer.class.getClassLoader().getResourceAsStream(args[0])) {
   if (stream == null) {
    throw new IllegalStateException("Couldn't load configs from resources");
   } else {
    String json = IOUtils.toString(stream, Charset.defaultCharset());
    GameServerConfigs gameServerConfigs = objectMapper.readValue(json, GameServerConfigs.class);
    gameServer = new GameServer(new GameServerChannelInitializer(gameServerConfigs));
    gameServer.start();
    Thread.sleep(Long.MAX_VALUE);
   }
  } catch (Exception e) {
   logger.fatal(e.getMessage(), e);
  } finally {
   if (gameServer != null) {
    gameServer.shutdown();
   }
  }
 }
}
