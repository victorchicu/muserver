package muserver.connectserver.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import muserver.common.AbstractPacketHandler;
import muserver.common.objects.ConnectorConfigs;

public class SendServerConnectHandler extends AbstractPacketHandler {
    private final ConnectorConfigs configs;

    public SendServerConnectHandler(ConnectorConfigs configs) {
        this.configs = configs;
    }

    @Override
    public void send(ChannelHandlerContext ctx, ByteBuf byteBuf) {
        int serverCode = byteBuf.readUnsignedShort();

        ConnectorConfigs.Server server = configs.servers().get(serverCode);

        int size = 22;

        ByteBuf directBuffer = Unpooled.directBuffer(size);

        directBuffer.writeByte(0xC1);
        directBuffer.writeByte(size);
        directBuffer.writeByte(0xF4);
        directBuffer.writeByte(3);

        int i = 0;

        for (byte val : server.ip().getBytes()) {
            directBuffer.writeByte(val);
            i++;
        }

        directBuffer.writeByte(0);

        int n = 16 - i - 1;

        while (n > 0) {
            directBuffer.writeByte(0xFE);
            n--;
        }

        directBuffer.writeShortLE(server.port());

        super.send(ctx, directBuffer);
    }
}
