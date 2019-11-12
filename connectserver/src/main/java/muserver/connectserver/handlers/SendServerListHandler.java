package muserver.connectserver.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import muserver.common.AbstractPacketHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SendServerListHandler extends AbstractPacketHandler {
    private static final Logger logger = LogManager.getLogger(SendServerListHandler.class);

    @Override
    public void send(ChannelHandlerContext ctx, ByteBuf byteBuf) {
        byteBuf.writeByte(0xC2);
        byteBuf.writeShortLE(0);
        byteBuf.writeByte(0xF4);
        byteBuf.writeByte(0x6);
        super.send(ctx, byteBuf);
    }
}