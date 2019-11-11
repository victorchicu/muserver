package muserver.connectserver.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import muserver.common.AbstractPacketHandler;

public class SendServerListHandler extends AbstractPacketHandler {
    @Override
    public void send(ChannelHandlerContext ctx, ByteBuf byteBuf) {

        super.send(ctx, byteBuf);
    }
}