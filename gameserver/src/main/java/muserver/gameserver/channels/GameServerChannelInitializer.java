package muserver.gameserver.channels;

import io.netty.channel.socket.SocketChannel;
import muserver.common.channels.BaseChannelInitializer;
import muserver.common.objects.GameServerConfigs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameServerChannelInitializer extends BaseChannelInitializer {
 private final static Logger LOGGER = LogManager.getLogger(GameServerChannelHandler.class);

 public GameServerChannelInitializer(GameServerConfigs configs) {
  super(configs);
  getLogger().info("Initializing {} channel", configs.name());
 }

 public static Logger getLogger() {
  return LOGGER;
 }

 @Override
 protected void initChannel(SocketChannel socketChannel) {
  GameServerConfigs configs = (GameServerConfigs) configs();
  getLogger().info("Initializing {0} channel handler", configs.port());
  socketChannel.pipeline().addLast(new GameServerChannelHandler(configs));
 }
}
