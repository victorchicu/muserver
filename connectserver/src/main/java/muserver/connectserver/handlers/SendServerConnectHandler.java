package muserver.connectserver.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import muserver.common.AbstractPacketHandler;
import muserver.common.objects.ConnectorConfigs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SendServerConnectHandler extends AbstractPacketHandler {
    private final ConnectorConfigs configs;

    public SendServerConnectHandler(ConnectorConfigs configs) {
        this.configs = configs;
    }

    @Override
    public void send(ChannelHandlerContext ctx, ByteBuf byteBuf) {
//        int size = (7 + (configs.servers().size() * 4));
//
//        byteBuf.writeByte(0xC2);
//        byteBuf.writeShort(size);
//        byteBuf.writeByte(0xF4);
//        byteBuf.writeByte(0x06);
//        byteBuf.writeShort(configs.servers().size());
//
//        for (Integer key : configs.servers().keySet()) {
//            byteBuf.writeByte(key).writeByte(0).writeByte(50).writeByte(0xCC);
//        }

        super.send(ctx, byteBuf);
    }
}
