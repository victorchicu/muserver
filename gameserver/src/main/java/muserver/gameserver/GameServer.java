package muserver.gameserver;

import muserver.common.BaseServer;
import muserver.common.Globals;
import muserver.common.channels.BaseChannelInitializer;
import muserver.common.objects.GameServerConfigs;
import muserver.common.utils.MuKeyFactory;
import muserver.gameserver.channels.GameServerChannelInitializer;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GameServer extends BaseServer {
 public GameServer(BaseChannelInitializer initializer) {
  super(initializer);
 }

 public static void main(String[] args) throws Exception {
  GameServer gameServer = null;

  if (args.length == 0) {
   throw new IllegalArgumentException("Arguments are empty no pathname to the configuration");
  }

  Path filePath = Paths.get(args[0]);

  if (!Files.exists(filePath)) {
   throw new FileNotFoundException(String.format("File with the specified pathname %s does not exist", args[0]));
  }

  MuKeyFactory.parse();

  byte[] bytes = Files.readAllBytes(Paths.get(args[0]));

  try (InputStream stream = new ByteArrayInputStream(bytes)) {
   String json = IOUtils.toString(stream, Charset.defaultCharset());

   GameServerConfigs configs = Globals.getObjectMapper().readValue(json, GameServerConfigs.class);

   gameServer = new GameServer(new GameServerChannelInitializer(configs));

   gameServer.start();

   Thread.sleep(Long.MAX_VALUE);
  } finally {
   if (gameServer != null) {
    gameServer.shutdownGracefully();
   }
  }
 }
}
