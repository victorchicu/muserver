package muserver.dataserver.initializers;

import io.netty.channel.socket.SocketChannel;
import muserver.common.channels.AbstractChannelInitializer;
import muserver.dataserver.handlers.TcpDataServerHandler;

import java.util.Map;

public class TcpDataServerInitializer extends AbstractChannelInitializer {
 public TcpDataServerInitializer(Map<String, Object> props) {
  super(props);
 }

 @Override
 protected void initChannel(SocketChannel socketChannel) throws Exception {
  socketChannel.pipeline().addLast(new TcpDataServerHandler(props()));
 }
}