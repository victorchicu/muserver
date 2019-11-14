package muserver.dataserver.channels;

import io.netty.channel.socket.SocketChannel;
import muserver.common.channels.AbstractChannelInitializer;
import muserver.common.objects.IConfigs;

import java.util.Map;

public class DataServerChannelInitializer extends AbstractChannelInitializer {
 public DataServerChannelInitializer() {
  super(null);
 }

 @Override
 protected void initChannel(SocketChannel socketChannel) {
  socketChannel.pipeline().addLast(new DataServerChannelHandler(null));
 }
}