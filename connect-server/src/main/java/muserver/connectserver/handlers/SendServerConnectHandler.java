package muserver.connectserver.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import muserver.common.AbstractPacketHandler;

public class SendServerConnectHandler extends AbstractPacketHandler {
    @Override
    public void sendRequest(ChannelHandlerContext ctx, ByteBuf byteBuf) {

    }
}
