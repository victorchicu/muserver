package muserver.connectserver.intializers;

import muserver.common.channels.AbstractChannelInitializer;
import muserver.connectserver.handlers.TcpConnectServerHandler;
import io.netty.channel.socket.SocketChannel;

import java.util.Map;

public class TcpConnectServerInitializer extends AbstractChannelInitializer {
 public TcpConnectServerInitializer(Map<String, Object> props) {
  super(props);
 }

 @Override
 protected void initChannel(SocketChannel socketChannel) {
  socketChannel.pipeline().addLast(new TcpConnectServerHandler(props()));
 }
}