import com.google.common.collect.ImmutableMap;
import muserver.common.AbstractServer;
import muserver.common.objects.ConnectorConfigs;
import muserver.common.objects.GameConfigs;
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
  List<AbstractServer> servers = Arrays.asList(
    new ConnectServer(
      new ConnectServerChannelInitializer(ConnectorConfigs.create(44405, ImmutableMap.of(
        0, ConnectorConfigs.Server.create("192.168.0.50", 55901, "GS-1", true)
      )))
    ),
    new GameServer(
      new GameServerChannelInitializer(GameConfigs.create(55901, "1.04.05"))
    )
  );
  try {
   servers.forEach(AbstractServer::start);
   Thread.sleep(Long.MAX_VALUE);
  } catch (Exception e) {
   logger.fatal(e.getMessage(), e);
  } finally {
   servers.forEach(AbstractServer::shutdown);
  }
 }
}