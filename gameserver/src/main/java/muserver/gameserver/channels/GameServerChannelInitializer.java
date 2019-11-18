package muserver.gameserver.channels;

import io.netty.channel.socket.SocketChannel;
import muserver.common.channels.AbstractChannelInitializer;
import muserver.common.objects.GameServerConfigs;

public class GameServerChannelInitializer extends AbstractChannelInitializer {
 public GameServerChannelInitializer(GameServerConfigs configs) {
  super(configs);
 }

 @Override
 protected void initChannel(SocketChannel socketChannel) {
  socketChannel.pipeline().addLast(new GameServerChannelHandler((GameServerConfigs) configs()));
 }
}
