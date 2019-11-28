package muserver.gameserver.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import muserver.baseserver.BasePacketHandler;
import muserver.baseserver.types.JoinResult;
import muserver.baseserver.utils.MuCryptUtils;
import muserver.gameserver.configs.GameServerProperties;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class JoinIdPassRequestHandler extends BasePacketHandler {
 private final GameServerProperties props;

 public JoinIdPassRequestHandler(GameServerProperties props) {
  this.props = props;
 }

 @Override
 public void send(ChannelHandlerContext ctx, ByteBuf byteBuf) {
  Charset utf8 = StandardCharsets.UTF_8;

  byteBuf.readerIndex(4);

  byte[] idBytes = new byte[10];
  byteBuf.readBytes(idBytes, 0, 10);
  MuCryptUtils.Dec3bit(idBytes, 0, 10);
  String idString = new String(idBytes, utf8).trim();

  byte[] passBytes = new byte[20];
  byteBuf.readBytes(passBytes, 0, 20);
  MuCryptUtils.Dec3bit(passBytes, 0, 20);
  String passString = new String(passBytes, utf8).trim();

  //Number of milliseconds elapsed since the client started.
  long tickCount = byteBuf.readUnsignedInt();
//  logger.info("Tick count: {}", new Date(tickCount));

  byte[] cliVersionBytes = new byte[5];
  byteBuf.readBytes(cliVersionBytes, 0, 5);
  String cliVersion = new String(cliVersionBytes, utf8).trim();

  byte[] cliSerialBytes = new byte[16];
  byteBuf.readBytes(cliSerialBytes, 0, 16);
  String cliSerial = new String(cliSerialBytes, utf8).trim();

  if (!cliVersion.equalsIgnoreCase(props.getVersion())) {
//   logger.warn("Invalid client version: {}", cliVersion);
   sendJoinResult(ctx, JoinResult.YOUR_ACCOUNT_IS_INVALID);
   ctx.close();
   return;
  }

  if (!cliSerial.equalsIgnoreCase(props.getSerial())) {
//   logger.warn("Invalid client serial: {}", cliSerial);
   sendJoinResult(ctx, JoinResult.NEW_VERSION_OF_GAME_IS_REQUIRED);
   ctx.close();
   return;
  }

  //todo: Validate account

  sendJoinResult(ctx, JoinResult.LOGIN_SUCCEED);
 }

 private void sendJoinResult(ChannelHandlerContext ctx, JoinResult result) {
  ByteBuf buffer = Unpooled.buffer(5);
  buffer.writeByte(0xC1);
  buffer.writeByte(0x05);
  buffer.writeByte(0xF1);
  buffer.writeByte(0x01);
  buffer.writeByte(result.type());
  super.send(ctx, buffer);
 }
}