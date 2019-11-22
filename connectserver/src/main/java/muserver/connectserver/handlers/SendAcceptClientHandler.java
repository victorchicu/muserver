package muserver.connectserver.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import muserver.common.handlers.BasePacketHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SendAcceptClientHandler extends BasePacketHandler {
 private static final Logger logger = LogManager.getLogger(SendAcceptClientHandler.class);

 @Override
 public void send(ChannelHandlerContext ctx, ByteBuf byteBuf) {
  byteBuf.writeByte(0xC1).writeByte(0x4).writeByte(0x0).writeByte(0x1);
  super.send(ctx, byteBuf);
 }
}
