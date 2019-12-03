package muserver.dataserver.channels;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import muserver.dataserver.configs.DataServerProperties;
import muserver.serverbase.BasePacketHandler;

import java.util.HashMap;
import java.util.Map;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

public class DataServerChannelHandler extends SimpleChannelInboundHandler<ByteBuf> {
// private static final Logger logger = LogManager.getLogger(DataServerChannelHandler.class);

 private final Map<Integer, BasePacketHandler> packets;

 public DataServerChannelHandler(DataServerProperties props) {
  packets = new HashMap<Integer, BasePacketHandler>();
 }

 @Override
 public void channelActive(ChannelHandlerContext ctx) {
  if (ctx.channel().remoteAddress() != null) {
//   logger.info("Accepted a client connection from remote address: {}", ctx.channel().remoteAddress().toString());
  }
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
 }
}