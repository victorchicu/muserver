package muserver.common;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbstractPacketHandler {
 private static final Logger logger = LogManager.getLogger(AbstractPacketHandler.class);

 public void send(ChannelHandlerContext ctx, ByteBuf byteBuf) {
  logger.info("\n{}", ByteBufUtil.prettyHexDump(byteBuf));

  ctx.writeAndFlush(byteBuf);
 }
}