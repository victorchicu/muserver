package muserver.connectserver.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import muserver.common.AbstractPacketHandler;
import muserver.common.objects.ConnectorConfigs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class SendServerListHandler extends AbstractPacketHandler {
    private static final Logger logger = LogManager.getLogger(SendServerListHandler.class);

    private final ConnectorConfigs configs;

    public SendServerListHandler(ConnectorConfigs configs) {
        this.configs = configs;
    }

    @Override
    public void send(ChannelHandlerContext ctx, ByteBuf byteBuf) {
        //[C2] [00 0F] [F4] [06] [00 02] [21 00] [00] [CC] [22 00] [00] [CC]
        int size = (7 + (configs.servers().size() * 4));
        //type
        byteBuf.writeByte(0xC2);
        //size
        byteBuf.writeShortLE(size);
        //head code
        byteBuf.writeByte(0xF4);
        //sub code
        byteBuf.writeByte(0x06);
        //servers count
        byteBuf.writeShortLE(configs.servers().size());
        //server code
        for (Integer key : configs.servers().keySet()) {
            byteBuf.writeShortLE(key).writeByte(0x0).writeByte(0xCC);
        }

        super.send(ctx, byteBuf);
    }
}