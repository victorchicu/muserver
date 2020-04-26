package mu.server.authenticator.channels.handlers.processors;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import mu.server.authenticator.channels.handlers.processors.base.BasePacketProcessor;

public class AcceptClientPacketProcessor extends BasePacketProcessor {
 @Override
 public void execute(ChannelHandlerContext ctx, ByteBuf byteBuf) {
  byteBuf.writeByte(0xC1).writeByte(0x4).writeByte(0x0).writeByte(0x1);
  ctx.writeAndFlush(byteBuf);
 }
}
