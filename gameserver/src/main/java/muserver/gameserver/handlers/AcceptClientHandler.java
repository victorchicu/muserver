package muserver.gameserver.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import muserver.common.handlers.BasePacketHandler;
import muserver.common.objects.GameServerConfigs;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class AcceptClientHandler extends BasePacketHandler {
 private final GameServerConfigs gameServerConfigs;

 public AcceptClientHandler(GameServerConfigs gameServerConfigs) {
  this.gameServerConfigs = gameServerConfigs;
 }

 @Override
 public void send(ChannelHandlerContext ctx, ByteBuf byteBuf) {
  //  c1 0c f1 00 01 23 28 31 30 34 30 35 cc cccccc

  byteBuf.writeByte(0xC1);
  byteBuf.writeByte(0x0C);
  byteBuf.writeByte(0xF1);
  byteBuf.writeByte(0x00);
  byteBuf.writeByte(0x01);
  byteBuf.writeShort(9000);
  byteBuf.writeBytes(gameServerConfigs.version().getBytes());

  super.send(ctx, byteBuf);
 }
}