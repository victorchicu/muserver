package core.server.channels;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;

public class CoreServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    private SimpleChannelInboundHandler<ByteBuf> byteBufSimpleChannelInboundHandler;

    public CoreServerChannelInitializer(SimpleChannelInboundHandler<ByteBuf> byteBufSimpleChannelInboundHandler) {
        this.byteBufSimpleChannelInboundHandler = byteBufSimpleChannelInboundHandler;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        socketChannel.pipeline().addLast(byteBufSimpleChannelInboundHandler);
    }
}
