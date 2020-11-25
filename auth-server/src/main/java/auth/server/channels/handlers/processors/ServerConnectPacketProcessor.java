package auth.server.channels.handlers.processors;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import auth.server.channels.handlers.processors.base.BasePacketProcessor;
import auth.server.properties.AuthServerProperties;

public class ServerConnectPacketProcessor extends BasePacketProcessor {
    private final AuthServerProperties authServerProperties;

    public ServerConnectPacketProcessor(AuthServerProperties authServerProperties) {
        this.authServerProperties = authServerProperties;
    }

    @Override
    public void execute(ChannelHandlerContext ctx, ByteBuf byteBuf) {
        int serverCode = byteBuf.readUnsignedShort();

        AuthServerProperties.Server server = authServerProperties.getServers().get(serverCode);

        int size = 22;

        ByteBuf directBuffer = Unpooled.directBuffer(size);

        directBuffer.writeByte(0xC1);
        directBuffer.writeByte(size);
        directBuffer.writeByte(0xF4);
        directBuffer.writeByte(3);

        for (byte val : server.getIp().getBytes()) {
            directBuffer.writeByte(val);
        }

        directBuffer.writeByte(0);

        int n = 16 - server.getIp().getBytes().length - 1;

        while (n > 0) {
            directBuffer.writeByte(0xFE);
            n--;
        }

        directBuffer.writeShortLE(server.getPort());

        ctx.writeAndFlush(directBuffer);
    }
}
