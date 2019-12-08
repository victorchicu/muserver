package muserver.gameserver.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import muserver.gameserver.configs.GameServerProperties;
import base.BasePacketHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DataServerGetCharListRequestHandler extends BasePacketHandler {
 private static final Logger logger = LogManager.getLogger(DataServerGetCharListRequestHandler.class);

 private final GameServerProperties props;

 public DataServerGetCharListRequestHandler(GameServerProperties props) {
  this.props = props;
 }

 @Override
 public void send(ChannelHandlerContext ctx, ByteBuf byteBuf) {
  ByteBuf buffer = Unpooled.buffer(5);

  buffer.writeByte(0xC1);
  buffer.writeByte(0x10);
  buffer.writeByte(0x01);
  buffer.writeBytes("test".getBytes());
  buffer.writeByte(0x9000);

  super.send(ctx, buffer);
 }
}