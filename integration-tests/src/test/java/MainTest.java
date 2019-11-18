import com.google.common.collect.ImmutableMap;
import muserver.common.AbstractServer;
import muserver.common.objects.ConnectorServerConfigs;
import muserver.common.objects.GameServerConfigs;
import muserver.connectserver.ConnectServer;
import muserver.connectserver.channels.ConnectServerChannelInitializer;
import muserver.gameserver.GameServer;
import muserver.gameserver.channels.GameServerChannelInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;

public class MainTest {
 private final static Logger logger = LogManager.getLogger(MainTest.class);

 public static void main(String... args) {
  byte[] data = hex2Bytes("C3 5A 9E 4D 18 56 28 FB 20 E5 2D A7 92 5A 01 33 CB 50 BA F0 10 69 76 43 16 D1 65 36 64 13 F1 45 CC 1A 2F B1 B7 4E 24 49 1F F2 2F 54 A7 92 F8 04 7E 8A A7 A7 73 EF 00 C9 FC E2 CF 35 E4 58 21 A0 5C 89 16 23 B2 8C 5C 4C 62 23 B2 8B 90 27 12 61 07 5D 00 4D C3 F3 5A 31 99 A7");

  List<AbstractServer> servers = Arrays.asList(
    new ConnectServer(
      new ConnectServerChannelInitializer(ConnectorServerConfigs.create(44405, ImmutableMap.of(
        0, ConnectorServerConfigs.Server.create("192.168.0.50", 55901, "GS-1", true)
      )))
    ),
    new GameServer(
      new GameServerChannelInitializer(GameServerConfigs.create(55901, "1.04.05"))
    )
  );
  try {
   servers.forEach(AbstractServer::start);
   Thread.sleep(Long.MAX_VALUE);
  } catch (Exception e) {
   logger.fatal(e.getMessage(), e);
  } finally {
   servers.forEach(AbstractServer::shutdown);
  }
 }

 private static byte[] hex2Bytes(String hex) {
  hex = hex.trim().replace(" ", "");
  int len = hex.length();
  if (len % 2 == 1)
   return null;
  byte[] b = new byte[len / 2];
  for (int i = 0; i < len; i += 2) {
   b[i >> 1] = (byte) Integer.parseInt(hex.trim().substring(i, i + 2), 16);
  }

  return b;
 }
}