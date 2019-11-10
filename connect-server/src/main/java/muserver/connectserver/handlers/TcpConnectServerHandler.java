package muserver.connectserver.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import muserver.common.Globals;
import muserver.common.messages.PBMSG_HEAD;
import muserver.common.utils.BytesUtils;
import muserver.connectserver.messages.PMSG_HANDSHAKE;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.util.Map;

public class TcpConnectServerHandler extends SimpleChannelInboundHandler<ByteBuf> {
 private final static Logger logger = LogManager.getLogger(TcpConnectServerHandler.class);

 private final Map<String, Object> props;

 public TcpConnectServerHandler(Map<String, Object> props) {
  this.props = props;
 }

 @Override
 public void channelActive(ChannelHandlerContext ctx) throws Exception {
  if (ctx.channel().remoteAddress() != null) {
   logger.info("Connection accepted: {}", ctx.channel().remoteAddress().toString());
  }

  PMSG_HANDSHAKE handshake = PMSG_HANDSHAKE.create(
    PBMSG_HEAD.create(Globals.PMHC_BYTE, (byte) PMSG_HANDSHAKE.sizeOf(), (byte) 0),
    (byte) 1
  );
  byte[] buffer = handshake.serialize(new ByteArrayOutputStream());
  ctx.writeAndFlush(Unpooled.wrappedBuffer(buffer));
 }

 @Override
 public void channelInactive(ChannelHandlerContext ctx) {
  if (ctx.channel().remoteAddress() != null) {
   logger.info("Connection interrupted: {}", ctx.channel().remoteAddress().toString());
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
 protected void channelRead0(ChannelHandlerContext ctx, ByteBuf byteBuf) throws Exception {
  if (byteBuf.readableBytes() > 0) {
   byte[] buffer = new byte[byteBuf.readableBytes()];
   byteBuf.readBytes(buffer);
   logger.info(BytesUtils.toString(buffer));
  }

//  0xC1 0x4 0xF4 0x6
 }
}