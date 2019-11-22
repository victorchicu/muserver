package muserver.connectserver.channels;

import muserver.common.channels.BaseChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import muserver.common.objects.ConnectServerConfigs;

public class ConnectServerChannelInitializer extends BaseChannelInitializer {
 private final ConnectServerConfigs configs;
 public ConnectServerChannelInitializer(ConnectServerConfigs configs) {
  super(configs);
  this.configs = configs;
 }

 @Override
 protected void initChannel(SocketChannel socketChannel) {
  socketChannel.pipeline().addLast(new ConnectServerChannelHandler(configs));
 }
}