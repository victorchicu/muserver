import com.google.common.collect.ImmutableMap;
import muserver.common.AbstractServer;
import muserver.common.objects.ConnectorConfigs;
import muserver.connectserver.ConnectServer;
import muserver.connectserver.channels.ConnectServerChannelInitializer;
import muserver.dataserver.DataServer;
import muserver.dataserver.channels.DataServerChannelInitializer;
import muserver.gameserver.GameServer;
import muserver.gameserver.channels.GameServerChannelInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class MainTest {
 private final static Logger logger = LogManager.getLogger(MainTest.class);

 public static void main(String... args) {
  List<AbstractServer> servers = Arrays.asList(
   new ConnectServer(
    new ConnectServerChannelInitializer(ConnectorConfigs.create(44405, ImmutableMap.of(
     0, ConnectorConfigs.Server.create("127.0.0.1", 55901, "GameServer", true)
    )))
   ),
   new DataServer(
    new DataServerChannelInitializer()
   ),
   new GameServer(
    new GameServerChannelInitializer()
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