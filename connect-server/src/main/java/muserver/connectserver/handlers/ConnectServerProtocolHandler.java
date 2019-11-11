package muserver.connectserver.handlers;

import com.google.common.collect.ImmutableMap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import muserver.common.AbstractPacketHandler;
import muserver.common.Globals;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class ConnectServerProtocolHandler extends SimpleChannelInboundHandler<ByteBuf> {
 private static final Logger logger = LogManager.getLogger(ConnectServerProtocolHandler.class);

 private static final Map<Integer, AbstractPacketHandler> packets = ImmutableMap.of(
  0xF403, new SendServerConnectHandler(),
  0xF406, new SendServerListHandler()
 );

 private final Map<String, Object> props;

 public ConnectServerProtocolHandler(Map<String, Object> props) {
  this.props = props;
 }

 @Override
 public void channelActive(ChannelHandlerContext ctx) {
  if (ctx.channel().remoteAddress() != null) {
   logger.info("Accepted a client connection from remote address: {}", ctx.channel().remoteAddress().toString());
  }
  new SendAcceptClientHandler().sendRequest(ctx, Unpooled.buffer());
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
  logger.info(ByteBufUtil.hexDump(byteBuf));

  short type = byteBuf.getUnsignedByte(0);

  if (type < 0xC1 || type > 0xC4) {
   ctx.close();
   if (ctx.channel().remoteAddress() != null) {
    logger.warn("Invalid packet type: {} | from: {}", type, ctx.channel().remoteAddress().toString());
   }
   return;
  }

  switch (type) {
   case 0xC1:
   case 0xC3: {
    short size = byteBuf.getUnsignedByte(0);
    if (size <= 0 || size > Globals.UNSIGNED_BYTE_MAX_VALUE) {
     ctx.close();
     if (ctx.channel().remoteAddress() != null) {
      logger.warn("Invalid packet size: {} for type: {} | from: {}", size, type, ctx.channel().remoteAddress().toString());
     }
     return;
    }
   }
   break;
   case 0xC2:
   case 0xC4: {
    int size = byteBuf.getUnsignedShort(0);
    if (size <= 0 || size > Globals.UNSIGNED_SHORT_MAX_VALUE) {
     ctx.close();
     if (ctx.channel().remoteAddress() != null) {
      logger.warn("Invalid packet size: {} for type: {} | from: {}", size, type, ctx.channel().remoteAddress().toString());
     }
     return;
    }
   }
   break;
  }

  int opcode = byteBuf.readerIndex(2).readUnsignedShort();

  AbstractPacketHandler packetHandler = packets.getOrDefault(opcode, null);

  if (packetHandler == null) {
   ctx.close();
   if (ctx.channel().remoteAddress() != null) {
    logger.warn("Invalid packet opcode: {} | from: {}", opcode, ctx.channel().remoteAddress().toString());
   }
   return;
  }

  packetHandler.sendRequest(ctx, byteBuf);
 }
}