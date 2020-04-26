package mu.server.coreserver.channels;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;

public class CoreServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    private SimpleChannelInboundHandler<ByteBuf> coreServerChannelInboundHandler;

    public CoreServerChannelInitializer(SimpleChannelInboundHandler<ByteBuf> coreServerChannelInboundHandler) {
        this.coreServerChannelInboundHandler = coreServerChannelInboundHandler;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        socketChannel.pipeline().addLast(coreServerChannelInboundHandler);
    }
}
