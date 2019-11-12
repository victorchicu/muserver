package muserver.connectserver;

import muserver.common.AbstractServer;
import muserver.common.channels.AbstractChannelInitializer;
import muserver.connectserver.channels.ConnectServerChannelInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class ConnectServer extends AbstractServer {
 private final static Logger logger = LogManager.getLogger(ConnectServer.class);

 public ConnectServer(AbstractChannelInitializer initializer) {
  super(initializer);
 }

 public static void main(String[] args) {
  ConnectServer connectServer = null;
  try {
   Map<String, Object> props = new HashMap<>();
   props.put("port", 44405);
   connectServer = new ConnectServer(new ConnectServerChannelInitializer(props));
   connectServer.start();
   Thread.sleep(Long.MAX_VALUE);
  } catch (Exception e) {
   logger.fatal(e.getMessage(), e);
  } finally {
   if (connectServer != null) {
    connectServer.shutdown();
   }
  }
 }
}