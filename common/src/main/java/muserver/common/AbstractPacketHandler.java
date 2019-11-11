package muserver.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public abstract class AbstractPacketHandler {
 public void send(ChannelHandlerContext ctx, ByteBuf byteBuf) {
  ctx.writeAndFlush(byteBuf);
 }
}