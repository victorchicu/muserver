package mu.server.coreserver.channels.handlers.processors;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import mu.server.coreserver.channels.handlers.processors.base.BasePacketProcessor;
import mu.server.coreserver.properties.CoreServerProperties;

public class LiveClientPacketProcessor extends BasePacketProcessor {
 private final CoreServerProperties coreServerProperties;

 public LiveClientPacketProcessor(CoreServerProperties coreServerProperties) {
  this.coreServerProperties = coreServerProperties;
 }

 @Override
 public void execute(ChannelHandlerContext ctx, ByteBuf byteBuf) {
  byteBuf.readerIndex(3);
  long time = byteBuf.readUnsignedInt();
  int attackSpeed = byteBuf.readUnsignedShort(), magicSpeed = byteBuf.readUnsignedShort();
//  logger.info("Live client -> Time: {} | Attack speed: {} | Magic speed {}", time, attackSpeed, magicSpeed);
 }
}