package muserver.connectserver.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import muserver.common.AbstractPacketHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SendServerConnectHandler extends AbstractPacketHandler {
    private static final Logger logger = LogManager.getLogger(SendServerConnectHandler.class);

    @Override
    public void send(ChannelHandlerContext ctx, ByteBuf byteBuf) {
        super.send(ctx, byteBuf);
    }
}
