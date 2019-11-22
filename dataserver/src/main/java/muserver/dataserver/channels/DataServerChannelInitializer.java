package muserver.dataserver.channels;

import io.netty.channel.socket.SocketChannel;
import muserver.common.channels.BaseChannelInitializer;

public class DataServerChannelInitializer extends BaseChannelInitializer {
 public DataServerChannelInitializer() {
  super(null);
 }

 @Override
 protected void initChannel(SocketChannel socketChannel) {
  socketChannel.pipeline().addLast(new DataServerChannelHandler(null));
 }
}