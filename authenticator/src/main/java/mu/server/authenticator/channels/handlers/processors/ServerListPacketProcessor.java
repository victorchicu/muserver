package mu.server.authenticator.channels.handlers.processors;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import mu.server.authenticator.channels.handlers.processors.base.BasePacketProcessor;
import mu.server.authenticator.properties.AuthenticatorProperties;

public class ServerListPacketProcessor extends BasePacketProcessor {
    private final AuthenticatorProperties authenticatorProperties;

    public ServerListPacketProcessor(AuthenticatorProperties authenticatorProperties) {
        this.authenticatorProperties = authenticatorProperties;
    }

    @Override
    public void execute(ChannelHandlerContext ctx, ByteBuf byteBuf) {
        int size = (7 + (authenticatorProperties.getServers().size() * 4));

        ByteBuf directBuffer = Unpooled.directBuffer(size);

        directBuffer.writeByte(0xC2);
        directBuffer.writeShort(size);
        directBuffer.writeByte(0xF4);
        directBuffer.writeByte(0x06);
        directBuffer.writeShort(authenticatorProperties.getServers().size());

        for (Integer key : authenticatorProperties.getServers().keySet()) {
            directBuffer.writeByte(key).writeByte(65 / 100 * 100).writeByte(50).writeByte(0xCC);
        }

        ctx.writeAndFlush(directBuffer);
    }
}