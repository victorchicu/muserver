package muserver.gameserver.clients;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import muserver.gameserver.channels.DataServerClientChannelHandler;

import java.net.InetSocketAddress;

public class DataServerClient {
 private final String host;
 private final int port;

 public DataServerClient(String host, int port) {
  this.host = host;
  this.port = port;
 }

 public void start() throws Exception {
  EventLoopGroup group = new NioEventLoopGroup();
  try {
   Bootstrap b = new Bootstrap();
   b.group(group)
     .channel(NioSocketChannel.class)
     .remoteAddress(new InetSocketAddress(host, port))
     .handler(new ChannelInitializer<SocketChannel>() {
      @Override
      public void initChannel(SocketChannel ch) throws Exception {
       ch.pipeline().addLast(new DataServerClientChannelHandler());
      }
     });
   ChannelFuture f = b.connect().sync();
   f.channel().closeFuture().sync();
  } finally {
   group.shutdownGracefully().sync();
  }
 }
}
