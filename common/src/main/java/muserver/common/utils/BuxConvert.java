package muserver.common.utils;

public class BuxConvert {
 static byte[] buxCode = {(byte) 0xFC, (byte) 0xCF, (byte) 0xAB};

 public static String convert(byte[] buffer) {
  for (int n = 0; n < buffer.length; n++) {
   buffer[n] ^= buxCode[n % 3];
  }
  return new String(buffer);
 }
}
