package muserver.dataserver;

import muserver.common.BaseServer;
import muserver.common.channels.BaseChannelInitializer;
import muserver.dataserver.channels.DataServerChannelInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DataServer extends BaseServer {
 private final static Logger logger = LogManager.getLogger(DataServer.class);

 public DataServer(BaseChannelInitializer initializer) {
  super(initializer);
 }

 public static void main(String[] args) {
  DataServer dataServer = null;
  try {
   dataServer = new DataServer(new DataServerChannelInitializer());

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

