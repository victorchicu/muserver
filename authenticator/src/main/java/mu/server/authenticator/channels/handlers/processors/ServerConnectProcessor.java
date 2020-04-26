package mu.server.authenticator.channels.handlers.processors;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import mu.server.authenticator.channels.handlers.processors.base.BasePacketProcessor;
import mu.server.authenticator.properties.AuthProperties;

public class ServerConnectProcessor extends BasePacketProcessor {
    private final AuthProperties authProperties;

    public ServerConnectProcessor(AuthProperties authProperties) {
        this.authProperties = authProperties;
    }

    @Override
    public void execute(ChannelHandlerContext ctx, ByteBuf byteBuf) {
        int serverCode = byteBuf.readUnsignedShort();

        AuthProperties.Server server = authProperties.getServers().get(serverCode);

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
