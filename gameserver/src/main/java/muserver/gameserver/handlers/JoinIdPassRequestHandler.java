package muserver.gameserver.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import muserver.common.handlers.BasePacketHandler;
import muserver.common.objects.GameServerConfigs;
import muserver.common.utils.MuCryptUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JoinIdPassRequestHandler extends BasePacketHandler {
 private final GameServerConfigs configs;

 public JoinIdPassRequestHandler(GameServerConfigs configs) {
  this.configs = configs;
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

  long tickCount = byteBuf.readUnsignedInt();

  Date date = new Date(tickCount);

  byte[] cliVersionBytes = new byte[5];
  byteBuf.readBytes(cliVersionBytes, 0, 5);
  String cliVersion = new String(cliVersionBytes, utf8).trim();

  byte[] cliSerialBytes = new byte[16];
  byteBuf.readBytes(cliSerialBytes, 0, 16);
  String cliSerial = new String(cliSerialBytes, utf8).trim();

//  PMSG_RESULT pResult; // [sp+4Ch] [bp-8h]@1
//
//  PHeadSubSetB(&pResult.h.c, 0xF1, 1, 5);
//  pResult.result = result;
//  DataSend(aIndex, &pResult.h.c, (unsigned __int8)pResult.h.size);

  super.send(ctx, byteBuf);
 }
}