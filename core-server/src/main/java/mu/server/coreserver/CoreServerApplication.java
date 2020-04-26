package mu.server.coreserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import mu.server.coreserver.channels.CoreServerChannelInitializer;
import mu.server.coreserver.channels.handlers.CoreServerChannelInboundHandler;
import mu.server.coreserver.properties.CoreServerProperties;
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
