package muserver.joinserver.channels;

import io.netty.channel.socket.SocketChannel;
import muserver.common.channels.AbstractChannelInitializer;

import java.util.Map;

public class JoinServerChannelInitializer extends AbstractChannelInitializer {
 public JoinServerChannelInitializer(Map<String, Object> props) {
  super(props);
 }

 @Override
 protected void initChannel(SocketChannel socketChannel) {
  socketChannel.pipeline().addLast(new JoinServerChannelHandler(props()));
 }
}
