package core.server.channels.handlers.processors;

import core.server.channels.handlers.processors.base.BasePacketProcessor;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import core.server.properties.CoreServerProperties;

public class AcceptClientPacketProcessor extends BasePacketProcessor {
 private final CoreServerProperties coreServerProperties;

 public AcceptClientPacketProcessor(CoreServerProperties coreServerProperties) {
  this.coreServerProperties = coreServerProperties;
 }

 @Override
 public void execute(ChannelHandlerContext ctx, ByteBuf byteBuf) {
  //  c1 0c f1 00 01 23 28 31 30 34 30 35 cc cccccc

  byteBuf.writeByte(0xC1);
  byteBuf.writeByte(0x0C);
  byteBuf.writeByte(0xF1);
  byteBuf.writeByte(0x00);
  byteBuf.writeByte(0x01);
  byteBuf.writeShort(9000);
  byteBuf.writeBytes(coreServerProperties.getVersion().getBytes());

  ctx.writeAndFlush(byteBuf);
 }
}