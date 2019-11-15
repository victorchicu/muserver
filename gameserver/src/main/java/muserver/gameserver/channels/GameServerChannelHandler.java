package muserver.gameserver.channels;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import muserver.common.AbstractPacketHandler;
import muserver.common.Globals;
import muserver.common.objects.GameConfigs;
import muserver.gameserver.handlers.SendAcceptClientHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class GameServerChannelHandler extends SimpleChannelInboundHandler<ByteBuf> {
 private final static Logger logger = LogManager.getLogger(GameServerChannelHandler.class);

 private final GameConfigs gameConfigs;
 private final Map<Integer, AbstractPacketHandler> packets = new HashMap<>();

 GameServerChannelHandler(GameConfigs gameConfigs) {
  this.gameConfigs = gameConfigs;

 }

 @Override
 public void channelActive(ChannelHandlerContext ctx) {
  if (ctx.channel().remoteAddress() != null) {
   logger.info("Accepted a client connection from remote address: {}", ctx.channel().remoteAddress().toString());
  }
  new SendAcceptClientHandler(gameConfigs).send(ctx, Unpooled.directBuffer(0x0C));
 }

 @Override
 public void channelInactive(ChannelHandlerContext ctx) {
  if (ctx.channel().remoteAddress() != null) {
   logger.info("Client from remote address: {} has interrupted connection", ctx.channel().remoteAddress().toString());
  }
 }

 @Override
 public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
  logger.error(cause.getMessage(), cause);
  ctx.close();
 }

 @Override
 public void channelReadComplete(ChannelHandlerContext ctx) {
  ctx.flush();
 }

 @Override
 protected void channelRead0(ChannelHandlerContext ctx, ByteBuf byteBuf) {
  logger.info("\n{}", ByteBufUtil.prettyHexDump(byteBuf));

  short type = byteBuf.getUnsignedByte(0);

  switch (type) {
   case 0xC1:
   case 0xC3: {
    short size = byteBuf.getUnsignedByte(1);
    if (size <= 0 || size > Globals.UNSIGNED_BYTE_MAX_VALUE) {
     ctx.close();
     if (ctx.channel().remoteAddress() != null) {
      logger.warn("Invalid protocol size: {} | from remote address: {}", size, ctx.channel().remoteAddress().toString());
     }
     return;
    }
   }
   break;
   case 0xC2:
   case 0xC4: {
    int size = byteBuf.getUnsignedShort(1);
    if (size <= 0 || size > Globals.UNSIGNED_SHORT_MAX_VALUE) {
     ctx.close();
     if (ctx.channel().remoteAddress() != null) {
      logger.warn("Invalid protocol size: {} | from remote address: {}", size, ctx.channel().remoteAddress().toString());
     }
     return;
    }
   }
   break;
   default: {
    ctx.close();
    if (ctx.channel().remoteAddress() != null) {
     logger.warn("Invalid protocol type: {} | from remote address: {}", type, ctx.channel().remoteAddress().toString());
    }
    return;
   }
  }

  int protoNum = byteBuf.readerIndex(2).readUnsignedShort();

  AbstractPacketHandler packetHandler = packets.get(protoNum);

  if (packetHandler == null) {
   ctx.close();
   if (ctx.channel().remoteAddress() != null) {
    logger.warn("Invalid protocol number: {} | from remote address: {}", protoNum, ctx.channel().remoteAddress().toString());
   }
   return;
  }

  packetHandler.send(ctx, byteBuf);
 }
}