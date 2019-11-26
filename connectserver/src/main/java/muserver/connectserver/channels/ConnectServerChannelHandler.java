package muserver.connectserver.channels;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import muserver.connectserver.configs.ConnectServerProperties;
import muserver.baseserver.BasePacketHandler;
import muserver.connectserver.handlers.AcceptClientHandler;
import muserver.connectserver.handlers.ServerConnectHandler;
import muserver.connectserver.handlers.ServerListHandler;

import java.util.HashMap;
import java.util.Map;

public class ConnectServerChannelHandler extends SimpleChannelInboundHandler<ByteBuf> {
 private final Map<Integer, BasePacketHandler> packets;

 public ConnectServerChannelHandler(ConnectServerProperties props) {
  packets = new HashMap<>();
  packets.put(0xF403, new ServerConnectHandler(props));
  packets.put(0xF406, new ServerListHandler(props));
 }

 @Override
 public void channelActive(ChannelHandlerContext ctx) {
  if (ctx.channel().remoteAddress() != null) {
//   logger.info("Accepted a client connection from remote address: {}", ctx.channel().remoteAddress().toString());
  }
  new AcceptClientHandler().send(ctx, Unpooled.directBuffer(4));
 }

 @Override
 public void channelInactive(ChannelHandlerContext ctx) {
  if (ctx.channel().remoteAddress() != null) {
//   logger.info("Client from remote address: {} has interrupted connection", ctx.channel().remoteAddress().toString());
  }
 }

 @Override
 public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
//  logger.error(cause.getMessage(), cause);
  ctx.close();
 }

 @Override
 public void channelReadComplete(ChannelHandlerContext ctx) {
  ctx.flush();
 }

 @Override
 protected void channelRead0(ChannelHandlerContext ctx, ByteBuf byteBuf) {
//  logger.info("RECEIVED PACKET\n{}", ByteBufUtil.prettyHexDump(byteBuf));

  short type = byteBuf.readUnsignedByte();

  switch (type) {
   case 0xC1:
   case 0xC3: {
    short size = byteBuf.readUnsignedByte();
    if (size <= 0 || size > 255) {
     ctx.close();
     if (ctx.channel().remoteAddress() != null) {
//      logger.warn("Invalid protocol size: {} | from remote address: {}", size, ctx.channel().remoteAddress().toString());
     }
     return;
    }
   }
   break;
   case 0xC2:
   case 0xC4: {
    int size = byteBuf.readUnsignedShort();
    if (size <= 0 || size > 65535) {
     ctx.close();
     if (ctx.channel().remoteAddress() != null) {
//      logger.warn("Invalid protocol size: {} | from remote address: {}", size, ctx.channel().remoteAddress().toString());
     }
     return;
    }
   }
   break;
   default: {
    ctx.close();
    if (ctx.channel().remoteAddress() != null) {
//     logger.warn("Invalid protocol type: {} | from remote address: {}", type, ctx.channel().remoteAddress().toString());
    }
    return;
   }
  }

  int opCode = byteBuf.readUnsignedShort();

  BasePacketHandler packetHandler = packets.get(opCode);

  if (packetHandler == null) {
   ctx.close();
   if (ctx.channel().remoteAddress() != null) {
//    logger.warn("Invalid protocol number: {} | from remote address: {}", opCode, ctx.channel().remoteAddress().toString());
   }
   return;
  }

  packetHandler.send(ctx, byteBuf);
 }
}