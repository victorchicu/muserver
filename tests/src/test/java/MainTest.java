import com.google.common.collect.ImmutableMap;
import muserver.common.BaseServer;
import muserver.common.objects.ConnectServerConfigs;
import muserver.common.objects.GameServerConfigs;
import muserver.connectserver.ConnectServer;
import muserver.connectserver.channels.ConnectServerChannelInitializer;
import muserver.gameserver.GameServer;
import muserver.gameserver.channels.GameServerChannelInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;

public class MainTest {
 private final static Logger logger = LogManager.getLogger(MainTest.class);

 public static void main(String... args) {
  List<BaseServer> servers = Arrays.asList(
    new ConnectServer(
      new ConnectServerChannelInitializer(ConnectServerConfigs.create(44405, ImmutableMap.of(
        0, ConnectServerConfigs.Server.create("192.168.0.50", 55901)
      )))
    ),
    new GameServer(
      new GameServerChannelInitializer(GameServerConfigs.create("GS-1", 55901, "1.04.05"))
    )
  );
  try {
   servers.forEach(BaseServer::start);
   Thread.sleep(Long.MAX_VALUE);
  } catch (Exception e) {
   logger.fatal(e.getMessage(), e);
  } finally {
   servers.forEach(BaseServer::shutdown);
  }
 }
}