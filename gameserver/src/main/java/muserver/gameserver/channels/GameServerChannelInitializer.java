package muserver.gameserver.channels;

import io.netty.channel.socket.SocketChannel;
import muserver.common.channels.AbstractChannelInitializer;

import java.util.Map;

public class GameServerChannelInitializer extends AbstractChannelInitializer {
 public GameServerChannelInitializer() {
  super(null);
 }

 @Override
 protected void initChannel(SocketChannel socketChannel) {
  socketChannel.pipeline().addLast(new GameServerChannelHandler(null));
 }
}
