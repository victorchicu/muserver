package muserver.gameserver.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import muserver.common.handlers.BasePacketHandler;
import muserver.common.objects.GameServerConfigs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DataServerGetCharListRequestHandler extends BasePacketHandler {
 private static final Logger logger = LogManager.getLogger(DataServerGetCharListRequestHandler.class);

 private final GameServerConfigs configs;

 public DataServerGetCharListRequestHandler(GameServerConfigs configs) {
  this.configs = configs;
 }

 @Override
 public void send(ChannelHandlerContext ctx, ByteBuf byteBuf) {

  super.send(ctx, byteBuf);
 }
}