package muserver.baseserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.concurrent.TimeUnit;

public abstract class BaseServer {
// private static final Logger logger = LoggerFactory.getLogger(BaseServer.class);

 private final EventLoopGroup tcpParentLoopGroup, tcpChildLoopGroup;

 public BaseServer() {
  tcpChildLoopGroup = new NioEventLoopGroup(1);
  tcpParentLoopGroup = new NioEventLoopGroup(1);
 }

 public ChannelFuture start(int port, SimpleChannelInboundHandler<ByteBuf>... handlers) {
  return new ServerBootstrap()
    .group(tcpParentLoopGroup, tcpChildLoopGroup)
    .channel(NioServerSocketChannel.class)
    .childHandler(new MultipleHandlerInitializer(handlers))
    .bind(port);
 }

 public void shutdownGracefully() {
  tcpChildLoopGroup.shutdownGracefully();
  tcpParentLoopGroup.shutdownGracefully();
 }

 public void shutdown(long quietPeriod, long timeout, TimeUnit unit) {
  tcpChildLoopGroup.shutdownGracefully(quietPeriod, timeout, unit);
  tcpParentLoopGroup.shutdownGracefully(quietPeriod, timeout, unit);
 }

 static class MultipleHandlerInitializer extends ChannelInitializer<SocketChannel> {
  private SimpleChannelInboundHandler<ByteBuf>[] handlers;

  public MultipleHandlerInitializer(SimpleChannelInboundHandler<ByteBuf>... handlers) {
   this.handlers = handlers;
  }

  @Override
  protected void initChannel(SocketChannel socketChannel) throws Exception {
   for (SimpleChannelInboundHandler<ByteBuf> handler : handlers) {
    socketChannel.pipeline().addLast(handler);
   }
  }
 }
}