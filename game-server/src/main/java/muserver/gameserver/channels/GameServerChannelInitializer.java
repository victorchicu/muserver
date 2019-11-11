package muserver.gameserver.channels;

import io.netty.channel.socket.SocketChannel;
import muserver.common.channels.AbstractChannelInitializer;

import java.util.Map;

public class GameServerChannelInitializer extends AbstractChannelInitializer {
 public GameServerChannelInitializer(Map<String, Object> props) {
  super(props);
 }

 @Override
 protected void initChannel(SocketChannel socketChannel) {
  socketChannel.pipeline().addLast(new GameServerChannelHandler(props()));
 }
}
