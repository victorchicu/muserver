package muserver.gameserver.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import muserver.common.handlers.BasePacketHandler;
import muserver.common.objects.GameServerConfigs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

public class LiveClientHandler extends BasePacketHandler {
 private static final Logger logger = LogManager.getLogger(LiveClientHandler.class);

 private final GameServerConfigs configs;

 public LiveClientHandler(GameServerConfigs configs) {
  this.configs = configs;
 }

 @Override
 public void send(ChannelHandlerContext ctx, ByteBuf byteBuf) {
  byteBuf.readerIndex(3);
  long time = byteBuf.readUnsignedInt();
  int attackSpeed = byteBuf.readUnsignedShort(), magicSpeed = byteBuf.readUnsignedShort();
  logger.info("Live client -> Time: {} | Attack speed: {} | Magic speed {}", time, attackSpeed, magicSpeed);
 }
}
