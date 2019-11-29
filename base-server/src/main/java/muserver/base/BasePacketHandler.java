package muserver.base;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public abstract class BasePacketHandler {
 public void send(ChannelHandlerContext ctx, ByteBuf byteBuf) {
  ctx.writeAndFlush(byteBuf);
 }
}