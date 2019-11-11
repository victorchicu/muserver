package muserver.connectserver.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import muserver.common.AbstractPacket;
import muserver.common.pairs.Pair;
import muserver.common.utils.PacketUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class TcpConnectServerHandler extends SimpleChannelInboundHandler<ByteBuf> {
 private final static Logger logger = LogManager.getLogger(TcpConnectServerHandler.class);

 private final Map<String, Object> props;
 private final Map<Pair<Short, Short>, AbstractPacket> packets;

 public TcpConnectServerHandler(Map<String, Object> props) {
  this.props = props;
  this.packets = new HashMap<>();
 }

 @Override
 public void channelActive(ChannelHandlerContext ctx) throws Exception {
  if (ctx.channel().remoteAddress() != null) {
   logger.info("Connection accepted: {}", ctx.channel().remoteAddress().toString());
  }

  //0xC1-0x4-0x0-0x1
//  ctx.writeAndFlush(Unpooled.wrappedBuffer(0xC1, 0x4, 0x0, 0x1));
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
   logger.info(PacketUtils.toHex(buffer));
  }

//  Pair

  //  0xC1 0x4 0xF4 0x6

  short opcode = byteBuf.getUnsignedByte(2), subCode = byteBuf.getUnsignedByte(3);



 }
}