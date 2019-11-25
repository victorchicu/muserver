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

 private final GameServerConfigs gameServerConfigs;

 public LiveClientHandler(GameServerConfigs gameServerConfigs) {
  this.gameServerConfigs = gameServerConfigs;
 }

 @Override
 public void send(ChannelHandlerContext ctx, ByteBuf byteBuf) {
  logger.info("Client ping at {}", new Date());
//  super.send(ctx, byteBuf);
 }
}
