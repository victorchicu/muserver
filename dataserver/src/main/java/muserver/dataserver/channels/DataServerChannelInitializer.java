package muserver.dataserver.channels;

import io.netty.channel.socket.SocketChannel;
import muserver.common.channels.AbstractChannelInitializer;

import java.util.Map;

public class DataServerChannelInitializer extends AbstractChannelInitializer {
 public DataServerChannelInitializer(Map<String, Object> props) {
  super(props);
 }

 @Override
 protected void initChannel(SocketChannel socketChannel) {
  socketChannel.pipeline().addLast(new DataServerChannelHandler(props()));
 }
}