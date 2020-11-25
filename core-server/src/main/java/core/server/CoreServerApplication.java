package core.server;

import core.server.channels.CoreServerChannelInitializer;
import core.server.channels.handlers.CoreServerChannelInboundHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import core.server.properties.CoreServerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PreDestroy;

@SpringBootApplication
public class CoreServerApplication implements CommandLineRunner {
    private final EventLoopGroup parentGroup, childGroup;
    private final CoreServerProperties coreServerProperties;

    @Autowired
    public CoreServerApplication(CoreServerProperties coreServerProperties) {
        parentGroup = childGroup = new NioEventLoopGroup(1);
        this.coreServerProperties = coreServerProperties;
    }

    public static void main(String[] args) {
        SpringApplication.run(CoreServerApplication.class, args);
    }

    @Override
    public void run(String... args) {
        new ServerBootstrap().group(parentGroup, childGroup).channel(NioServerSocketChannel.class).childHandler(
                new CoreServerChannelInitializer(
                        new CoreServerChannelInboundHandler(coreServerProperties)
                )
        ).bind(coreServerProperties.getPort());
    }

    @PreDestroy
    public void onShutdown() {
        childGroup.shutdownGracefully();
        parentGroup.shutdownGracefully();
    }
}
