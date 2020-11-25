package core.server.channels.handlers.processors;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import core.server.channels.handlers.processors.base.BasePacketProcessor;
import core.server.properties.CoreServerProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DataServerGetCharListRequestPacketProcessor extends BasePacketProcessor {
 private static final Logger logger = LogManager.getLogger(DataServerGetCharListRequestPacketProcessor.class);

 private final CoreServerProperties coreServerProperties;

 public DataServerGetCharListRequestPacketProcessor(CoreServerProperties coreServerProperties) {
  this.coreServerProperties = coreServerProperties;
 }

 @Override
 public void execute(ChannelHandlerContext ctx, ByteBuf byteBuf) {
  ByteBuf buffer = Unpooled.buffer(5);

  buffer.writeByte(0xC1);
  buffer.writeByte(0x10);
  buffer.writeByte(0x01);
  buffer.writeBytes("test".getBytes());
  buffer.writeByte(0x9000);

  ctx.writeAndFlush(buffer);
 }
}