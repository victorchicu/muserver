package base;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import java.util.concurrent.TimeUnit;

public abstract class BaseServer {
 private static final Logger logger = LoggerFactory.getLogger(BaseServer.class);

 private final StopWatch stopWatch;
 private final EventLoopGroup tcpParentLoopGroup, tcpChildLoopGroup;

 public BaseServer() {
  stopWatch = new StopWatch();
  tcpChildLoopGroup = new NioEventLoopGroup(1);
  tcpParentLoopGroup = new NioEventLoopGroup(1);
 }

 public ChannelFuture start(int port, SimpleChannelInboundHandler<ByteBuf>... handlers) {
  logger.info("Starting {} on port {}", getClass().getSimpleName(), port);

  stopWatch.start();

  ChannelFuture serverBootstrap = new ServerBootstrap()
    .group(tcpParentLoopGroup, tcpChildLoopGroup)
    .channel(NioServerSocketChannel.class)
    .childHandler(new ChannelsInitializer(handlers))
    .bind(port);

  stopWatch.stop();

  logger.info("Started {} in {} seconds", getClass().getSimpleName(), stopWatch.getTotalTimeSeconds());

  return serverBootstrap;
 }

 public void shutdownGracefully() {
  tcpChildLoopGroup.shutdownGracefully();
  tcpParentLoopGroup.shutdownGracefully();
 }

 public void shutdown(long quietPeriod, long timeout, TimeUnit unit) {
  tcpChildLoopGroup.shutdownGracefully(quietPeriod, timeout, unit);
  tcpParentLoopGroup.shutdownGracefully(quietPeriod, timeout, unit);
 }

 static class ChannelsInitializer extends ChannelInitializer<SocketChannel> {
  private SimpleChannelInboundHandler<ByteBuf>[] handlers;

  public ChannelsInitializer(SimpleChannelInboundHandler<ByteBuf>... handlers) {
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