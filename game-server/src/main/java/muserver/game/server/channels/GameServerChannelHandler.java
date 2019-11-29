package muserver.game.server.channels;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import muserver.base.BasePacketHandler;
import muserver.game.server.utils.MuDecoder;
import muserver.game.server.handlers.AcceptClientHandler;
import muserver.game.server.handlers.DataServerGetCharListRequestHandler;
import muserver.game.server.handlers.JoinIdPassRequestHandler;
import muserver.game.server.handlers.LiveClientHandler;
import muserver.game.server.configs.GameServerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class GameServerChannelHandler extends SimpleChannelInboundHandler<ByteBuf> {
 private static final Logger logger = LoggerFactory.getLogger(GameServerChannelHandler.class);

 private final GameServerProperties props;
 private final Map<Integer, BasePacketHandler> packets;

 public GameServerChannelHandler(GameServerProperties props) {
  packets = new HashMap<>();
  packets.put(0x0E00, new LiveClientHandler(props));
  packets.put(0xF101, new JoinIdPassRequestHandler(props));
  packets.put(0xF300, new DataServerGetCharListRequestHandler(props));
  this.props = props;
 }

 @Override
 public void channelActive(ChannelHandlerContext ctx) {
  if (ctx.channel().remoteAddress() != null) {
   logger.info("Accepted a client connection from remote address: {}", ctx.channel().remoteAddress().toString());
  }
  new AcceptClientHandler(props).send(ctx, Unpooled.directBuffer(0x0C));
 }

 @Override
 public void channelInactive(ChannelHandlerContext ctx) {
  if (ctx.channel().remoteAddress() != null) {
   logger.info("Client from remote address: {} has interrupted connection", ctx.channel().remoteAddress().toString());
  }
 }

 @Override
 public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) {
  logger.error(e.getMessage(), e);
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

    if (size <= 0 || size > 255) {
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

    if (size <= 0 || size > 65535) {
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

    if (size <= 0 || size > 255) {
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

    if (size <= 0 || size > 65535) {
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