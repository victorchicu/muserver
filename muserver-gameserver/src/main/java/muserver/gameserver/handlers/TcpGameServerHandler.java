package muserver.gameserver.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import muserver.common.Globals;
import muserver.common.utils.HexUtils;
import muserver.common.utils.SimpleModulus;
import muserver.gameserver.contexts.GameServerContext;
import muserver.gameserver.exceptions.GameServerException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TcpGameServerHandler extends SimpleChannelInboundHandler<ByteBuf> {
 private final static Logger logger = LogManager.getLogger(TcpGameServerHandler.class);

 private final SimpleModulus simpleModulus = new SimpleModulus(
     new SimpleModulus.SimpleKeys(
         Globals.DEC1_MODULUS_KEY_TABLE, Globals.DEC1_KEY_TABLE, Globals.DEC1_XOR_KEY_TABLE
     ),
     new SimpleModulus.SimpleKeys(
         Globals.ENC2_MODULUS_KEY_TABLE, Globals.ENC2_KEY_TABLE, Globals.ENC2_XOR_KEY_TABLE
     )
 );

 public TcpGameServerHandler(GameServerContext gameServerContext) {
 }

 @Override
 public void channelActive(ChannelHandlerContext ctx) throws Exception {
  if (ctx.channel().remoteAddress() != null) {
   logger.info("Connection accepted: {}", ctx.channel().remoteAddress().toString());
  }
 }

 @Override
 public void channelInactive(ChannelHandlerContext ctx) throws Exception {
  if (ctx.channel().remoteAddress() != null) {
   logger.info("Connection interrupted: {}", ctx.channel().remoteAddress().toString());
  }
 }

 @Override
 public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
  logger.error(cause.getMessage(), cause);
  ctx.close();
 }

 @Override
 public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
  ctx.flush();
 }

 @Override
 protected void channelRead0(ChannelHandlerContext ctx, ByteBuf byteBuf) throws Exception {
  if (byteBuf.readableBytes() < 4) {
   throw new GameServerException(String.format("Invalid buffer length: %d", byteBuf.readableBytes()));
  }

  while (byteBuf.readableBytes() > 0) {
   ByteBuf newBuff = Unpooled.buffer(byteBuf.capacity()), encryptedPacket = byteBuf.slice(2, byteBuf.capacity() - 2);
   System.out.println(HexUtils.toString(byteBuf.array()));

   Integer decryptedSize = simpleModulus.decrypt(newBuff, encryptedPacket, byteBuf.capacity() - 2) + 1;
   System.out.println(HexUtils.toString(newBuff.array()));

   newBuff.setByte(0, 0xC1).setByte(1, decryptedSize);
   simpleModulus.extractPacket(newBuff);
   System.out.println(HexUtils.toString(newBuff.array()));
  }
 }
}