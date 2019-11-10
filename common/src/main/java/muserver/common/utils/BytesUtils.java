package muserver.common.utils;

public class BytesUtils {
 public static String toString(byte[] buffer) {
  StringBuilder hexBuilder = new StringBuilder();
  for (byte val : buffer) {
   hexBuilder.append(String.format("0x%X ", val));
  }
  return hexBuilder.toString();
 }
}
