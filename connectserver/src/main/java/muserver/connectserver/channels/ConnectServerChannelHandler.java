package muserver.connectserver.channels;

import com.google.common.collect.ImmutableMap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import muserver.common.handlers.BasePacketHandler;
import muserver.common.Globals;
import muserver.common.objects.ConnectServerConfigs;
import muserver.connectserver.handlers.AcceptClientHandler;
import muserver.connectserver.handlers.ServerConnectHandler;
import muserver.connectserver.handlers.ServerListHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class ConnectServerChannelHandler extends SimpleChannelInboundHandler<ByteBuf> {
 private static final Logger logger = LogManager.getLogger(ConnectServerChannelHandler.class);

 private final Map<Integer, BasePacketHandler> packets;

 ConnectServerChannelHandler(ConnectServerConfigs configs) {
  packets = ImmutableMap.of(
    0xF403, new ServerConnectHandler(configs),
    0xF406, new ServerListHandler(configs)
  );
 }

 @Override
 public void channelActive(ChannelHandlerContext ctx) {
  if (ctx.channel().remoteAddress() != null) {
   logger.info("Accepted a client connection from remote address: {}", ctx.channel().remoteAddress().toString());
  }
  new AcceptClientHandler().send(ctx, Unpooled.directBuffer(4));
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
  logger.info("RECEIVED PACKET\n{}", ByteBufUtil.prettyHexDump(byteBuf));

  short type = byteBuf.readUnsignedByte();

  switch (type) {
   case 0xC1:
   case 0xC3: {
    short size = byteBuf.readUnsignedByte();
    if (size <= 0 || size > Globals.getUnsignedByteMaxValue()) {
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
    int size = byteBuf.readUnsignedShort();
    if (size <= 0 || size > Globals.getUnsignedShortMaxValue()) {
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

  int opCode = byteBuf.readUnsignedShort();

  BasePacketHandler packetHandler = packets.get(opCode);

  if (packetHandler == null) {
   ctx.close();
   if (ctx.channel().remoteAddress() != null) {
    logger.warn("Invalid protocol number: {} | from remote address: {}", opCode, ctx.channel().remoteAddress().toString());
   }
   return;
  }

  packetHandler.send(ctx, byteBuf);
 }
}