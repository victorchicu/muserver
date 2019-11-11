package muserver.common.utils;

public class PacketUtils {
 public static String toHex(byte[] buffer) {
  String result = "";
  for (int i = 0; i < buffer.length; i++) {
   result += "-" + Integer.toString((buffer[i] & 0xff) + 0x100, 16).substring(1);
  }
  return result.toUpperCase().substring(1);
 }

 public static byte[] fromHex(String hex) {
  hex = hex.trim().replace(" ", "");
  int length = hex.length();
  if (length % 2 == 1)
   return null;
  byte[] buffer = new byte[length / 2];
  for (int i = 0; i < length; i += 2) {
   buffer[i >> 1] = (byte) Integer.parseInt(hex.trim().substring(i, i + 2), 16);
  }
  return buffer;
 }
}
