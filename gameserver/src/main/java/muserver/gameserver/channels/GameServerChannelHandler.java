package muserver.gameserver.channels;

import com.google.common.collect.ImmutableMap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import muserver.common.Globals;
import muserver.common.handlers.BasePacketHandler;
import muserver.common.objects.GameServerConfigs;
import muserver.common.utils.MuDecoder;
import muserver.gameserver.handlers.DataServerGetCharListRequestHandler;
import muserver.gameserver.handlers.JoinIdPassRequestHandler;
import muserver.gameserver.handlers.LiveClientHandler;
import muserver.gameserver.handlers.AcceptClientHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class GameServerChannelHandler extends SimpleChannelInboundHandler<ByteBuf> {
 private static final Logger logger = LogManager.getLogger(GameServerChannelHandler.class);

 private final GameServerConfigs configs;
 private final Map<Integer, BasePacketHandler> packets;

 GameServerChannelHandler(GameServerConfigs configs) {
  packets = ImmutableMap.of(
    0x0E00, new LiveClientHandler(configs),
    0xF101, new JoinIdPassRequestHandler(configs),
    0xF300, new DataServerGetCharListRequestHandler(configs)
  );
  this.configs = configs;
 }

 @Override
 public void channelActive(ChannelHandlerContext ctx) {
  if (ctx.channel().remoteAddress() != null) {
   logger.info("Accepted a client connection from remote address: {}", ctx.channel().remoteAddress().toString());
  }
  new AcceptClientHandler(configs).send(ctx, Unpooled.directBuffer(0x0C));
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
  short type = byteBuf.getUnsignedByte(0);

  switch (type) {
   case 0xC1: {
    short size = byteBuf.getUnsignedByte(1);

    if (size <= 0 || size > Globals.getUnsignedByteMaxValue()) {
     ctx.close();
     if (ctx.channel().remoteAddress() != null) {
      logger.warn("Invalid protocol size: {} | from remote address: {}", size, ctx.channel().remoteAddress().toString());
     }
     return;
    }

    logger.info("RECEIVED PACKET\n{}", ByteBufUtil.prettyHexDump(byteBuf));

    ByteBuf decodedBuff = MuDecoder.DecodeXor32(byteBuf);

    logger.info("DECODED PACKET\n{}", ByteBufUtil.prettyHexDump(decodedBuff));

    int opCode = byteBuf.getUnsignedShort(2);

    BasePacketHandler packetHandler = packets.get(opCode);

    if (packetHandler != null) {
     packetHandler.send(ctx, byteBuf);
    }
   }
   break;

   case 0xC2: {
    int size = byteBuf.getUnsignedShort(1);

    if (size <= 0 || size > Globals.getUnsignedShortMaxValue()) {
     ctx.close();
     if (ctx.channel().remoteAddress() != null) {
      logger.warn("Invalid protocol size: {} | from remote address: {}", size, ctx.channel().remoteAddress().toString());
     }
     return;
    }

    logger.info("RECEIVED PACKET\n{}", ByteBufUtil.prettyHexDump(byteBuf));

    ByteBuf decodedBuff = MuDecoder.DecodeXor32(byteBuf);

    logger.info("DECODED PACKET\n{}", ByteBufUtil.prettyHexDump(decodedBuff));

    int opCode = byteBuf.getUnsignedShort(3);

    BasePacketHandler packetHandler = packets.get(opCode);

    if (packetHandler != null) {
     packetHandler.send(ctx, byteBuf);
    }
   }
   break;

   case 0xC3: {
    short size = byteBuf.getUnsignedByte(1);

    if (size <= 0 || size > Globals.getUnsignedByteMaxValue()) {
     ctx.close();
     if (ctx.channel().remoteAddress() != null) {
      logger.warn("Invalid protocol size: {} | from remote address: {}", size, ctx.channel().remoteAddress().toString());
     }
     return;
    }

    logger.info("RECEIVED PACKET\n{}", ByteBufUtil.prettyHexDump(byteBuf));

    ByteBuf decodedBuff = MuDecoder.DecodePacket(byteBuf);

    logger.info("DECODED PACKET\n{}", ByteBufUtil.prettyHexDump(decodedBuff));

    int opCode = decodedBuff.getUnsignedShort(2);

    BasePacketHandler packetHandler = packets.get(opCode);

    if (packetHandler != null) {
     packetHandler.send(ctx, decodedBuff);
    }
   }
   break;

   case 0xC4: {
    int size = byteBuf.getUnsignedShort(1);

    if (size <= 0 || size > Globals.getUnsignedShortMaxValue()) {
     ctx.close();
     if (ctx.channel().remoteAddress() != null) {
      logger.warn("Invalid protocol size: {} | from remote address: {}", size, ctx.channel().remoteAddress().toString());
     }
     return;
    }

    logger.info("RECEIVED PACKET\n{}", ByteBufUtil.prettyHexDump(byteBuf));

    ByteBuf decodedBuff = MuDecoder.DecodePacket(byteBuf);

    logger.info("DECODED PACKET\n{}", ByteBufUtil.prettyHexDump(decodedBuff));

    int opCode = decodedBuff.getUnsignedShort(3);

    BasePacketHandler packetHandler = packets.get(opCode);

    if (packetHandler != null) {
     packetHandler.send(ctx, decodedBuff);
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
 }
}