package muserver.connectserver.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import muserver.common.AbstractPacketHandler;

public class SendAcceptClientHandler extends AbstractPacketHandler {
 @Override
 public void send(ChannelHandlerContext ctx, ByteBuf byteBuf) {
  byteBuf.writeBytes(new byte[]{(byte) 0xC1, 0x4, 0x0, 0x1});
  super.send(ctx, byteBuf);
 }
}
