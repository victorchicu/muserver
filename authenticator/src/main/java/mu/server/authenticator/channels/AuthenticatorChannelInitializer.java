package mu.server.authenticator.channels;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;

public class AuthenticatorChannelInitializer extends ChannelInitializer<SocketChannel> {
    private SimpleChannelInboundHandler<ByteBuf> authenticatorChannelInboundHandler;

    public AuthenticatorChannelInitializer(SimpleChannelInboundHandler<ByteBuf> authenticatorChannelInboundHandler) {
        this.authenticatorChannelInboundHandler = authenticatorChannelInboundHandler;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        socketChannel.pipeline().addLast(authenticatorChannelInboundHandler);
    }
}
