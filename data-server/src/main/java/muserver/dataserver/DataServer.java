package muserver.dataserver;

import muserver.common.AbstractServer;
import muserver.common.channels.AbstractChannelInitializer;
import muserver.dataserver.channels.DataServerChannelInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class DataServer extends AbstractServer {
 private final static Logger logger = LogManager.getLogger(DataServer.class);

 public DataServer(AbstractChannelInitializer initializer) {
  super(initializer);
 }

 public static void main(String[] args) {
  DataServer dataServer = null;
  try {
   Map<String, Object> props = new HashMap<>();
   props.put("port", 55960);
   dataServer = new DataServer(new DataServerChannelInitializer(props));
   dataServer.start();
   Thread.sleep(Long.MAX_VALUE);
  } catch (Exception e) {
   logger.fatal(e.getMessage(), e);
  } finally {
   if (dataServer != null) {
    dataServer.shutdown();
   }
  }
 }
}

