package mu.server.authenticator;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import mu.server.authenticator.channels.AuthChannelInitializer;
import mu.server.authenticator.channels.handlers.AuthServerChannelInboundHandler;
import mu.server.authenticator.properties.AuthProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PreDestroy;

@SpringBootApplication
public class AuthServerApplication implements CommandLineRunner {
    private final AuthProperties authProperties;
    private final EventLoopGroup parentGroup, childGroup;

    @Autowired
    public AuthServerApplication(AuthProperties authProperties) {
        this.authProperties = authProperties;
        parentGroup = childGroup = new NioEventLoopGroup(1);
    }

    public static void main(String[] args) {
        SpringApplication.run(AuthServerApplication.class, args);
    }

    @Override
    public void run(String... args) {
        new ServerBootstrap().group(parentGroup, childGroup).channel(NioServerSocketChannel.class).childHandler(
                new AuthChannelInitializer(
                        new AuthServerChannelInboundHandler(authProperties)
                )
        ).bind(authProperties.getPort());
    }

    @PreDestroy
    public void onShutdown() {
        childGroup.shutdownGracefully();
        parentGroup.shutdownGracefully();
    }
}
