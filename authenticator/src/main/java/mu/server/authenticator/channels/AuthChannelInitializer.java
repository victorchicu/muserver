package mu.server.authenticator.channels;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;

public class AuthChannelInitializer extends ChannelInitializer<SocketChannel> {
    private SimpleChannelInboundHandler<ByteBuf> authChannelInboundHandler;

    public AuthChannelInitializer(SimpleChannelInboundHandler<ByteBuf> authChannelInboundHandler) {
        this.authChannelInboundHandler = authChannelInboundHandler;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        socketChannel.pipeline().addLast(authChannelInboundHandler);
    }
}
