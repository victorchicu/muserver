package auth.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import auth.server.channels.AuthServerChannelInitializer;
import auth.server.channels.handlers.AuthServerChannelInboundHandler;
import auth.server.properties.AuthServerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PreDestroy;

@SpringBootApplication
public class AuthServerApplication implements CommandLineRunner {
    private final EventLoopGroup parentGroup, childGroup;
    private final AuthServerProperties authServerProperties;

    @Autowired
    public AuthServerApplication(AuthServerProperties authServerProperties) {
        parentGroup = childGroup = new NioEventLoopGroup(1);
        this.authServerProperties = authServerProperties;
    }

    public static void main(String[] args) {
        SpringApplication.run(AuthServerApplication.class, args);
    }

    @Override
    public void run(String... args) {
        new ServerBootstrap().group(parentGroup, childGroup).channel(NioServerSocketChannel.class).childHandler(
                new AuthServerChannelInitializer(
                        new AuthServerChannelInboundHandler(authServerProperties)
                )
        ).bind(authServerProperties.getPort());
    }

    @PreDestroy
    public void onShutdown() {
        childGroup.shutdownGracefully();
        parentGroup.shutdownGracefully();
    }
}
