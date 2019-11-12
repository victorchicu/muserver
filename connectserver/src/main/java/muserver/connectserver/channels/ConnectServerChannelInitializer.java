package muserver.connectserver.channels;

import muserver.common.channels.AbstractChannelInitializer;
import io.netty.channel.socket.SocketChannel;

import java.util.Map;

public class ConnectServerChannelInitializer extends AbstractChannelInitializer {
 public ConnectServerChannelInitializer(Map<String, Object> props) {
  super(props);
 }

 @Override
 protected void initChannel(SocketChannel socketChannel) {
  socketChannel.pipeline().addLast(new ConnectServerChannelHandler(props()));
 }
}