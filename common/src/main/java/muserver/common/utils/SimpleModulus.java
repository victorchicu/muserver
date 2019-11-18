package muserver.common.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SimpleModulus {
 private static final long[] XOR_KEYS = new long[]{0x3F08A79B, 0xE25CC287, 0x93D27AB9, 0x20DEA7BF};
 private static long[] clientToServerPacketDecKeys = new long[12];

 public static Decoder readKeysFromFile(String path) {
  return null;
 }

 public static class Decoder {

  public ByteBuf decode(ByteBuf byteBuf) {
   throw new UnsupportedOperationException();
  }
 }

 private static void readFile(String path, long[] buffer) throws IOException {
  byte[] fileBuf = new byte[0];
  try {
   fileBuf = Files.readAllBytes(Paths.get(path));
  } catch (IOException e) {
   e.printStackTrace();
  }

  ByteArrayInputStream stream = new ByteArrayInputStream(fileBuf);

  byte[] content = new byte[fileBuf.length];

  int offset = 0;
  while (offset < content.length) {
   int read = stream.read(content, offset, content.length - offset);
   if (read < 0) {
    throw new RuntimeException();
   }
   if (read > 0) {
    offset += read;
   } else {
    Thread.yield();
   }
  }

  ByteBuf buff = Unpooled.buffer().order(ByteOrder.LITTLE_ENDIAN);

  try (InputStream is = new ByteArrayInputStream(content)) {
   buff.writeBytes(is, is.available());
  }

  buff.readerIndex(6);

  int index = 0;
  for (int i = 0; i < 3; i++) {
   long[] buf = new long[4];

   for (int j = 0; j < 4; j++) {
    buf[j] = buff.readUnsignedInt();
   }

   buffer[index++] = buf[0] ^ (XOR_KEYS[0]);
   buffer[index++] = buf[1] ^ (XOR_KEYS[1] & 0xFFFFFFFFL);
   buffer[index++] = buf[2] ^ (XOR_KEYS[2] & 0xFFFFFFFFL);
   buffer[index++] = buf[3] ^ (XOR_KEYS[3]);
  }
 }


 public static void main(String... args) {


  Decoder decoder = SimpleModulus.readKeysFromFile("");

  ByteBuf byteBuf = decoder.decode(null);
 }
}
