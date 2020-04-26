package mu.server.authenticator;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import mu.server.authenticator.channels.AuthenticatorChannelInitializer;
import mu.server.authenticator.channels.handlers.AuthenticatorChannelInboundHandler;
import mu.server.authenticator.properties.AuthenticatorProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PreDestroy;

@SpringBootApplication
public class AuthenticatorApplication implements CommandLineRunner {
    private final EventLoopGroup parentGroup, childGroup;
    private final AuthenticatorProperties authenticatorProperties;

    @Autowired
    public AuthenticatorApplication(AuthenticatorProperties authenticatorProperties) {
        parentGroup = childGroup = new NioEventLoopGroup(1);
        this.authenticatorProperties = authenticatorProperties;
    }

    public static void main(String[] args) {
        SpringApplication.run(AuthenticatorApplication.class, args);
    }

    @Override
    public void run(String... args) {
        new ServerBootstrap().group(parentGroup, childGroup).channel(NioServerSocketChannel.class).childHandler(
                new AuthenticatorChannelInitializer(
                        new AuthenticatorChannelInboundHandler(authenticatorProperties)
                )
        ).bind(authenticatorProperties.getPort());
    }

    @PreDestroy
    public void onShutdown() {
        childGroup.shutdownGracefully();
        parentGroup.shutdownGracefully();
    }
}
