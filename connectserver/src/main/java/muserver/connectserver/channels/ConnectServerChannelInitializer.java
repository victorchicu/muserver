package muserver.connectserver.channels;

import muserver.common.channels.AbstractChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import muserver.common.objects.ConnectorServerConfigs;

public class ConnectServerChannelInitializer extends AbstractChannelInitializer {
 public ConnectServerChannelInitializer(ConnectorServerConfigs connectorServerConfigs) {
  super(connectorServerConfigs);
 }

 @Override
 protected void initChannel(SocketChannel socketChannel) {
  socketChannel.pipeline().addLast(new ConnectServerChannelHandler((ConnectorServerConfigs) configs()));
 }
}