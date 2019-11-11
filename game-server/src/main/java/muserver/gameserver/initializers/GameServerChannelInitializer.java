package muserver.gameserver.initializers;

import io.netty.channel.socket.SocketChannel;
import muserver.common.channels.AbstractChannelInitializer;
import muserver.gameserver.handlers.GameServerProtocolHandler;

import java.util.Map;

public class GameServerChannelInitializer extends AbstractChannelInitializer {
 public GameServerChannelInitializer(Map<String, Object> props) {
  super(props);
 }

 @Override
 protected void initChannel(SocketChannel socketChannel) {
  socketChannel.pipeline().addLast(new GameServerProtocolHandler(props()));
 }
}
