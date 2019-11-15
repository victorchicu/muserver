package muserver.connectserver.channels;

import muserver.common.channels.AbstractChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import muserver.common.objects.ConnectorConfigs;

import java.util.Map;

public class ConnectServerChannelInitializer extends AbstractChannelInitializer {
 public ConnectServerChannelInitializer(ConnectorConfigs connectorConfigs) {
  super(connectorConfigs);
 }

 @Override
 protected void initChannel(SocketChannel socketChannel) {
  socketChannel.pipeline().addLast(new ConnectServerChannelHandler((ConnectorConfigs) configs()));
 }
}