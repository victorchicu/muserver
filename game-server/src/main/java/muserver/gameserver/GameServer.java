package muserver.gameserver;

import muserver.common.AbstractServer;
import muserver.common.channels.AbstractChannelInitializer;
import muserver.gameserver.initializers.GameServerChannelInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class GameServer extends AbstractServer {
 private final static Logger logger = LogManager.getLogger(GameServer.class);

 public GameServer(AbstractChannelInitializer initializer) {
  super(initializer);
 }

 public static void main(String[] args) {
  GameServer gameServer = null;
  try {
   Map<String, Object> props = new HashMap<>();
   props.put("port", 55901);
   gameServer = new GameServer(new GameServerChannelInitializer(props));
   gameServer.start();
   Thread.sleep(Long.MAX_VALUE);
  } catch (Exception e) {
   logger.fatal(e.getMessage(), e);
  } finally {
   if (gameServer != null) {
    gameServer.shutdown();
   }
  }
 }
}
