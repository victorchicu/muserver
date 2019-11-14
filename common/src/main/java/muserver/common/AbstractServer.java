package muserver.common;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import muserver.common.channels.AbstractChannelInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbstractServer {
 private final static Logger logger = LogManager.getLogger(AbstractServer.class);
 private final EventLoopGroup tcpParentLoopGroup, tcpChildLoopGroup;
 private final AbstractChannelInitializer initializer;

 public AbstractServer(AbstractChannelInitializer initializer) {
  this.initializer = initializer;
  tcpChildLoopGroup = new NioEventLoopGroup(1);
  tcpParentLoopGroup = new NioEventLoopGroup(1);
 }

 public ChannelFuture start() {
  logger.info("Start {} on port {}", getClass().getSimpleName(), initializer.configs().port());
  return new ServerBootstrap()
   .group(tcpParentLoopGroup, tcpChildLoopGroup)
   .channel(NioServerSocketChannel.class)
   .childHandler(initializer)
   .bind(initializer.configs().port());
 }

 public void shutdown() {
  tcpChildLoopGroup.shutdownGracefully();
  tcpParentLoopGroup.shutdownGracefully();
 }
}