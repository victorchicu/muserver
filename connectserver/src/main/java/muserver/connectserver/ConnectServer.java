package muserver.connectserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import muserver.common.AbstractServer;
import muserver.common.channels.AbstractChannelInitializer;
import muserver.common.objects.ConnectorConfigs;
import muserver.connectserver.channels.ConnectServerChannelInitializer;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.nio.charset.Charset;

public class ConnectServer extends AbstractServer {
 private static final Logger logger = LogManager.getLogger(ConnectServer.class);
 private static final ObjectMapper objectMapper = new ObjectMapper();

 public ConnectServer(AbstractChannelInitializer initializer) {
  super(initializer);
 }

 public static void main(String[] args) {
  ConnectServer connectServer = null;
  try (InputStream stream = ConnectServer.class.getClassLoader().getResourceAsStream(args[0])) {
   if (stream == null) {
    throw new IllegalStateException("Couldn't load configs from resources");
   } else {
    String json = IOUtils.toString(stream, Charset.defaultCharset());
    ConnectorConfigs connectorConfigs = objectMapper.readValue(json, ConnectorConfigs.class);
    connectServer = new ConnectServer(new ConnectServerChannelInitializer(connectorConfigs));
    connectServer.start();
    Thread.sleep(Long.MAX_VALUE);
   }
  } catch (Exception e) {
   logger.fatal(e.getMessage(), e);
  } finally {
   if (connectServer != null) {
    connectServer.shutdown();
   }
  }
 }
}