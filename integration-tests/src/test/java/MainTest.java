import com.google.common.collect.ImmutableMap;
import muserver.common.AbstractServer;
import muserver.common.utils.PacketUtils;
import muserver.connectserver.ConnectServer;
import muserver.connectserver.intializers.TcpConnectServerInitializer;
import muserver.dataserver.DataServer;
import muserver.dataserver.initializers.TcpDataServerInitializer;
import muserver.gameserver.GameServer;
import muserver.gameserver.initializers.TcpGameServerInitializer;
import muserver.joinserver.JoinServer;
import muserver.joinserver.initializers.TcpJoinServerInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;

public class MainTest {
 private final static Logger logger = LogManager.getLogger(MainTest.class);

 public static void main(String... args) throws Exception {
  List<AbstractServer> servers = Arrays.asList(
    new ConnectServer(
      new TcpConnectServerInitializer(ImmutableMap.of("port", 44405))
    ),
    new DataServer(
      new TcpDataServerInitializer(ImmutableMap.of("port", 55960))
    ),
    new GameServer(
      new TcpGameServerInitializer(ImmutableMap.of("port", 55901))
    ),
    new JoinServer(
      new TcpJoinServerInitializer(ImmutableMap.of("port", 55970))
    )
  );
  try {
   servers.stream().forEach(x -> x.start());
   Thread.sleep(Long.MAX_VALUE);
  } catch (Exception e) {
   logger.fatal(e.getMessage(), e);
  } finally {
   servers.stream().forEach(x -> x.shutdown());
  }
 }
}