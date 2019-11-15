package muserver.connectserver.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import muserver.common.AbstractPacketHandler;
import muserver.common.objects.ConnectorConfigs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sun.text.normalizer.UBiDiProps;

public class SendServerConnectHandler extends AbstractPacketHandler {
    private final ConnectorConfigs configs;

    public SendServerConnectHandler(ConnectorConfigs configs) {
        this.configs = configs;
    }

    @Override
    public void send(ChannelHandlerContext ctx, ByteBuf byteBuf) {
        int serverCode = byteBuf.readUnsignedShort();

        ConnectorConfigs.Server server = configs.servers().get(serverCode);

        ByteBuf directBuffer = Unpooled.directBuffer(22);

        directBuffer.writeByte(0xC1);
        directBuffer.writeByte(22);
        directBuffer.writeByte(0xF4);
        directBuffer.writeByte(3);

        int i = 0;

        for (byte val : server.ip().getBytes()) {
            directBuffer.writeByte(val);
            i++;
        }

        int n = 16 - i;

        while (n > 0) {
            directBuffer.writeByte(0);
            n--;
        }

        directBuffer.writeShort(server.port());

        super.send(ctx, directBuffer);
    }
}
