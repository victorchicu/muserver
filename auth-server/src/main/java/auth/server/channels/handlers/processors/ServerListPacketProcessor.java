package auth.server.channels.handlers.processors;

import auth.server.channels.handlers.processors.base.BasePacketProcessor;
import auth.server.properties.AuthServerProperties;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

public class ServerListPacketProcessor extends BasePacketProcessor {
    private final AuthServerProperties authServerProperties;

    public ServerListPacketProcessor(AuthServerProperties authServerProperties) {
        this.authServerProperties = authServerProperties;
    }

    @Override
    public void execute(ChannelHandlerContext ctx, ByteBuf byteBuf) {
        int size = (7 + (authServerProperties.getServers().size() * 4));

        ByteBuf directBuffer = Unpooled.directBuffer(size);

        directBuffer.writeByte(0xC2);
        directBuffer.writeShort(size);
        directBuffer.writeByte(0xF4);
        directBuffer.writeByte(0x06);
        directBuffer.writeShort(authServerProperties.getServers().size());

        for (Integer key : authServerProperties.getServers().keySet()) {
            directBuffer.writeByte(key).writeByte(65 / 100 * 100).writeByte(50).writeByte(0xCC);
        }

        ctx.writeAndFlush(directBuffer);
    }
}