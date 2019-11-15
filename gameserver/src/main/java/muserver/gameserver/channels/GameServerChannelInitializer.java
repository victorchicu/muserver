package muserver.gameserver.channels;

import io.netty.channel.socket.SocketChannel;
import muserver.common.channels.AbstractChannelInitializer;
import muserver.common.objects.GameConfigs;

import java.util.Map;

public class GameServerChannelInitializer extends AbstractChannelInitializer {
 public GameServerChannelInitializer(GameConfigs configs) {
  super(configs);
 }

 @Override
 protected void initChannel(SocketChannel socketChannel) {
  socketChannel.pipeline().addLast(new GameServerChannelHandler((GameConfigs) configs()));
 }
}
