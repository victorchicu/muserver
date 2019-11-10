package muserver.joinserver.initializers;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import muserver.common.channels.AbstractChannelInitializer;
import muserver.joinserver.handlers.TcpJoinServerHandler;

import java.util.Map;

public class TcpJoinServerInitializer extends AbstractChannelInitializer {
 public TcpJoinServerInitializer(Map<String, Object> props) {
  super(props);
 }

 @Override
 protected void initChannel(SocketChannel socketChannel) throws Exception {
  socketChannel.pipeline().addLast(new TcpJoinServerHandler(props()));
 }
}
