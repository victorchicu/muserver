package muserver.joinserver.initializers;

import io.netty.channel.socket.SocketChannel;
import muserver.common.channels.AbstractChannelInitializer;
import muserver.joinserver.handlers.JoinServerProtocolHandler;

import java.util.Map;

public class JoinServerChannelInitializer extends AbstractChannelInitializer {
 public JoinServerChannelInitializer(Map<String, Object> props) {
  super(props);
 }

 @Override
 protected void initChannel(SocketChannel socketChannel) {
  socketChannel.pipeline().addLast(new JoinServerProtocolHandler(props()));
 }
}
