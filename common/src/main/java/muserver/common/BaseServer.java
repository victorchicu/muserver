package muserver.common;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import muserver.common.channels.BaseChannelInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

public abstract class BaseServer {
 private final static Logger logger = LogManager.getLogger(BaseServer.class);
 private final EventLoopGroup tcpParentLoopGroup, tcpChildLoopGroup;
 private final BaseChannelInitializer baseChannelInitializer;

 public BaseServer(BaseChannelInitializer baseChannelInitializer) {
  this.baseChannelInitializer = baseChannelInitializer;
  tcpChildLoopGroup = new NioEventLoopGroup(1);
  tcpParentLoopGroup = new NioEventLoopGroup(1);
 }

 public ChannelFuture start() {
  logger.info("Start {} on port {}", getClass().getSimpleName(), baseChannelInitializer.configs().port());
  return new ServerBootstrap()
   .group(tcpParentLoopGroup, tcpChildLoopGroup)
   .channel(NioServerSocketChannel.class)
   .childHandler(baseChannelInitializer)
   .bind(baseChannelInitializer.configs().port());
 }

 public void shutdownGracefully() {
  tcpChildLoopGroup.shutdownGracefully();
  tcpParentLoopGroup.shutdownGracefully();
 }

 public void shutdown(long quietPeriod, long timeout, TimeUnit unit) {
  tcpChildLoopGroup.shutdownGracefully(quietPeriod, timeout, unit);
  tcpParentLoopGroup.shutdownGracefully(quietPeriod, timeout, unit);
 }
}