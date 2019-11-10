package muserver.common;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import muserver.common.channels.AbstractChannelInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public abstract class AbstractServer {
    private final static Logger logger = LogManager.getLogger(AbstractServer.class);
    private final ObjectMapper mapper = new ObjectMapper();
    private final EventLoopGroup tcpParentLoopGroup, tcpChildLoopGroup;
    private final AbstractChannelInitializer initializer;

    public AbstractServer(AbstractChannelInitializer initializer) {
        this.initializer = initializer;
        tcpChildLoopGroup = new NioEventLoopGroup(1);
        tcpParentLoopGroup = new NioEventLoopGroup(1);
        mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
    }

    public ChannelFuture start() {
        ChannelFuture serverBootstrap = new ServerBootstrap()
          .group(tcpParentLoopGroup, tcpChildLoopGroup)
          .channel(NioServerSocketChannel.class)
          .childHandler(initializer)
          .bind((Integer) initializer.props().get("port"));
        return serverBootstrap;
    }

    public void shutdown() {
        tcpChildLoopGroup.shutdownGracefully();
        tcpParentLoopGroup.shutdownGracefully();
    }
}