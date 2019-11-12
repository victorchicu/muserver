package muserver.connectserver.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import muserver.common.AbstractPacketHandler;
import muserver.connectserver.channels.ConnectServerChannelHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SendAcceptClientHandler extends AbstractPacketHandler {
 private static final Logger logger = LogManager.getLogger(SendAcceptClientHandler.class);

 @Override
 public void send(ChannelHandlerContext ctx, ByteBuf byteBuf) {
  byteBuf.writeBytes(new byte[]{(byte) 0xC1, 0x4, 0x0, 0x1});
  super.send(ctx, byteBuf);
 }
}
