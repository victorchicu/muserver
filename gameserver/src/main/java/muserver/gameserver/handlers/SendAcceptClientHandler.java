package muserver.gameserver.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import muserver.common.AbstractPacketHandler;
import muserver.common.objects.GameServerConfigs;

public class SendAcceptClientHandler extends AbstractPacketHandler {
 private final GameServerConfigs gameServerConfigs;

 public SendAcceptClientHandler(GameServerConfigs gameServerConfigs) {
  this.gameServerConfigs = gameServerConfigs;
 }

 @Override
 public void send(ChannelHandlerContext ctx, ByteBuf byteBuf) {
  //  c1 0c f1 00 01 23 28 31 30 34 30 35 cc cccccc

  byteBuf.writeByte(0xC1);
  byteBuf.writeByte(0x0C);
  byteBuf.writeByte(0);
  byteBuf.writeByte(1);
  byteBuf.writeShort(9000);
  byteBuf.writeBytes(gameServerConfigs.version().replace(".", "").getBytes());

  super.send(ctx, byteBuf);
 }
}
