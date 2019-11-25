package muserver.connectserver;

import muserver.common.BaseServer;
import muserver.common.Globals;
import muserver.common.channels.BaseChannelInitializer;
import muserver.common.objects.ConnectServerConfigs;
import muserver.connectserver.channels.ConnectServerChannelInitializer;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConnectServer extends BaseServer {
 public ConnectServer(BaseChannelInitializer initializer) {
  super(initializer);
 }

 public static void main(String[] args) throws Exception {
  ConnectServer connectServer = null;

  if (args.length == 0) {
   throw new IllegalArgumentException("Arguments are empty no pathname to the configuration");
  }

  Path filePath = Paths.get(args[0]);

  if (!Files.exists(filePath)) {
   throw new FileNotFoundException(String.format("File with the specified pathname: %s does not exist", args[0]));
  }

  byte[] bytes = Files.readAllBytes(filePath);

  try (InputStream stream = new ByteArrayInputStream(bytes)) {
   String json = IOUtils.toString(stream, Charset.defaultCharset());

   ConnectServerConfigs configs = Globals.getObjectMapper().readValue(json, ConnectServerConfigs.class);

   connectServer = new ConnectServer(new ConnectServerChannelInitializer(configs));
   connectServer.start();

   Thread.sleep(Long.MAX_VALUE);
  } finally {
   if (connectServer != null) {
    connectServer.shutdownGracefully();
   }
  }
 }
}