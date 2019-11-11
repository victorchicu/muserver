package muserver.dataserver.initializers;

import io.netty.channel.socket.SocketChannel;
import muserver.common.channels.AbstractChannelInitializer;
import muserver.dataserver.handlers.DataServerProtocolHandler;

import java.util.Map;

public class DataServerChannelInitializer extends AbstractChannelInitializer {
 public DataServerChannelInitializer(Map<String, Object> props) {
  super(props);
 }

 @Override
 protected void initChannel(SocketChannel socketChannel) {
  socketChannel.pipeline().addLast(new DataServerProtocolHandler(props()));
 }
}