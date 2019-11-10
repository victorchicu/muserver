package muserver.gameserver.initializers;

import io.netty.channel.socket.SocketChannel;
import muserver.common.channels.AbstractChannelInitializer;
import muserver.gameserver.handlers.TcpGameServerHandler;

import java.util.Map;

public class TcpGameServerInitializer extends AbstractChannelInitializer {
 public TcpGameServerInitializer(Map<String, Object> props) {
  super(props);
 }

 @Override
 protected void initChannel(SocketChannel socketChannel) throws Exception {
  socketChannel.pipeline().addLast(new TcpGameServerHandler(props()));
 }
}
