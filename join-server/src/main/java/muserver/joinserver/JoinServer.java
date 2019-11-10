package muserver.joinserver;

import muserver.common.AbstractServer;
import muserver.common.channels.AbstractChannelInitializer;
import muserver.joinserver.initializers.TcpJoinServerInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class JoinServer extends AbstractServer {
 private final static Logger logger = LogManager.getLogger(JoinServer.class);

 public JoinServer(AbstractChannelInitializer initializer) {
  super(initializer);
 }

 public static void main(String[] args) throws Exception {
  JoinServer joinServer = null;
  try {
   Map<String, Object> props = new HashMap<>();
   props.put("port", 55970);
   joinServer = new JoinServer(new TcpJoinServerInitializer(props));
   joinServer.start();
   Thread.sleep(Long.MAX_VALUE);
  } catch (Exception e) {
   logger.fatal(e.getMessage(), e);
  } finally {
   if (joinServer != null) {
    joinServer.shutdown();
   }
  }
 }
}
