package muserver.connectserver.channels;

import com.google.common.collect.ImmutableMap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import muserver.common.AbstractPacketHandler;
import muserver.common.Globals;
import muserver.common.objects.ConnectorConfigs;
import muserver.connectserver.handlers.SendAcceptClientHandler;
import muserver.connectserver.handlers.SendServerConnectHandler;
import muserver.connectserver.handlers.SendServerListHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.ByteOrder;
import java.util.Map;

public class ConnectServerChannelHandler extends SimpleChannelInboundHandler<ByteBuf> {
 private static final Logger logger = LogManager.getLogger(ConnectServerChannelHandler.class);

 private final Map<Integer, AbstractPacketHandler> packets;

 ConnectServerChannelHandler(ConnectorConfigs configs) {
  packets = ImmutableMap.of(
   0xF403, new SendServerConnectHandler(configs),
   0xF406, new SendServerListHandler(configs)
  );
 }

 @Override
 public void channelActive(ChannelHandlerContext ctx) {
  if (ctx.channel().remoteAddress() != null) {
   logger.info("Accepted a client connection from remote address: {}", ctx.channel().remoteAddress().toString());
  }
  new SendAcceptClientHandler().send(ctx, Unpooled.buffer());
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

  packetHandler.send(ctx, Unpooled.buffer());
 }
}