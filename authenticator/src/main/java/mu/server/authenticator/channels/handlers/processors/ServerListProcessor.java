package mu.server.authenticator.channels.handlers.processors;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import mu.server.authenticator.channels.handlers.processors.base.BasePacketProcessor;
import mu.server.authenticator.properties.AuthProperties;

public class ServerListProcessor extends BasePacketProcessor {
    private final AuthProperties authProperties;

    public ServerListProcessor(AuthProperties authProperties) {
        this.authProperties = authProperties;
    }

    @Override
    public void execute(ChannelHandlerContext ctx, ByteBuf byteBuf) {
        int size = (7 + (authProperties.getServers().size() * 4));

        ByteBuf directBuffer = Unpooled.directBuffer(size);

        directBuffer.writeByte(0xC2);
        directBuffer.writeShort(size);
        directBuffer.writeByte(0xF4);
        directBuffer.writeByte(0x06);
        directBuffer.writeShort(authProperties.getServers().size());

        for (Integer key : authProperties.getServers().keySet()) {
            directBuffer.writeByte(key).writeByte(65 / 100 * 100).writeByte(50).writeByte(0xCC);
        }

        ctx.writeAndFlush(directBuffer);
    }
}