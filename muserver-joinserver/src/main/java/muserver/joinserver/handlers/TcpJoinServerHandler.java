package muserver.joinserver.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import muserver.common.Globals;
import muserver.common.messages.PBMSG_HEAD;
import muserver.common.utils.HexUtils;
import muserver.joinserver.common.JoinIdPassRequestResult;
import muserver.joinserver.contexts.JoinServerContext;
import muserver.joinserver.exceptions.JoinServerException;
import muserver.joinserver.messages.SDHP_IDPASS;
import muserver.joinserver.messages.SDHP_IDPASSRESULT;
import muserver.joinserver.messages.SDHP_RESULT;
import muserver.joinserver.messages.SDHP_SERVERINFO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TcpJoinServerHandler extends SimpleChannelInboundHandler<ByteBuf> {
 private final static Logger logger = LogManager.getLogger(TcpJoinServerHandler.class);

 public TcpJoinServerHandler(JoinServerContext joinServerContext) {
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
   throw new JoinServerException(String.format("Invalid buffer length: %d", byteBuf.readableBytes()));
  }

  byte[] buffer = new byte[byteBuf.readableBytes()];

  byteBuf.getBytes(0, buffer);

  if (buffer[0] != Globals.PMHC_BYTE) {
   throw new JoinServerException(String.format("Invalid protocol type: %d", buffer[0]));
  }

  logger.info(HexUtils.toString(buffer));

  switch (buffer[2]) {
   case 0: {
    GJServerInfoRecv(ctx, buffer);
   }
   break;

   case 1: {
    GJConnectAccountRecv(ctx, buffer);
   }
   break;

   default:
    //throw new JoinServerException(String.format("Unsupported header code: %s", buffer[2]));
  }
 }


 private void GJServerInfoRecv(ChannelHandlerContext ctx, byte[] buffer) throws IOException {
  SDHP_SERVERINFO serverInfo = SDHP_SERVERINFO.deserialize(
      new ByteArrayInputStream(buffer)
  );
  logger.info("Server info -> name: {} port: {} type: {}", serverInfo.serverName(), serverInfo.port(), serverInfo.type());
  SDHP_RESULT result = SDHP_RESULT.create(
      PBMSG_HEAD.create(Globals.PMHC_BYTE, (byte) SDHP_RESULT.sizeOf(), (byte) 0), (byte) 1, 0
  );
  ctx.writeAndFlush(Unpooled.wrappedBuffer(result.serialize(new ByteArrayOutputStream())));
 }

 private void GJConnectAccountRecv(ChannelHandlerContext ctx, byte[] buffer) throws IOException {
  SDHP_IDPASS idPass = SDHP_IDPASS.deserialize(new ByteArrayInputStream(buffer));

  String joominNumber = "";
  Integer userNumber = -1, dbNumber = 0;

  //byte result
  //2: Incorrect password

  SDHP_IDPASSRESULT idPassResult = SDHP_IDPASSRESULT.create(
      PBMSG_HEAD.create(Globals.PMHC_BYTE, (byte) SDHP_IDPASSRESULT.sizeOf(), (byte) 1),
      JoinIdPassRequestResult.UNKNOWN3,
      idPass.number(),
      idPass.id(),
      userNumber,
      dbNumber,
      joominNumber
  );

  ctx.writeAndFlush(Unpooled.wrappedBuffer(idPassResult.serialize(new ByteArrayOutputStream())));
 }
}