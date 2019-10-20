package muserver.joinserver.common.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import muserver.common.utils.HexUtils;
import muserver.common.utils.SimpleModulus;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;

import static muserver.common.Globals.*;

public class SimpleModulusTest {
 @Test
 public void test() {
  if (-6 > -1) {
   System.out.println("Yes");
  } else {
   System.out.println("No");
  }
 }

 @Test
 public void testC3PacketDecryption() throws Exception {
  SimpleModulus simpleModulus = new SimpleModulus(
      new SimpleModulus.SimpleKeys(ENC2_MODULUS_KEY_TABLE, ENC2_KEY_TABLE, ENC2_XOR_KEY_TABLE),
      new SimpleModulus.SimpleKeys(DEC1_MODULUS_KEY_TABLE, DEC1_KEY_TABLE, DEC1_XOR_KEY_TABLE)
  );

  ByteBuf originalC3Packet = Unpooled.wrappedBuffer(
      new byte[]{
          (byte) 0xC3, 0x18, 0x28, 0x6F, 0x32, 0x33, (byte) 0x90, 0xA, 0x70, 0x35, 0x51, (byte) 0xFD, (byte) 0xC8, (byte) 0xFC, 0x6D, 0x13, (byte) 0xA9, 0x15, 0x2F, (byte) 0x92, 0x0, 0x0, 0x31, 0xF
      }
  );

//  ByteBuf originalC3Packet = Unpooled.wrappedBuffer(new byte[]{(byte) 0xC3, 0x18, 0x3E, (byte) 0x9F, 0x31, 0x4, (byte) 0xC4, 0x1C, (byte) 0xC1, 0x58, 0x48, 0x48, 0x7D, (byte) 0xE1, 0x59, 0x26, 0x31, 0x64, (byte) 0xE8, 0x66, (byte) 0xBE, (byte) 0xDC, (byte) 0xF2, (byte) 0xCC});

  ByteBuf decBuff = Unpooled.buffer(originalC3Packet.capacity());
  ByteBuf originalLpMsg = originalC3Packet.slice(2, originalC3Packet.capacity() - 2);
  System.out.println("Original C3 packet");
  System.out.println(HexUtils.toString(originalC3Packet.array()));

  Integer decSize = simpleModulus.decrypt(decBuff, originalLpMsg, originalC3Packet.capacity() - 2) + 1;
  System.out.println("Decrypted C3 packet");
  System.out.println(HexUtils.toString(decBuff.array()));

  decBuff.setByte(0, 0xC1).setByte(1, decSize);
  simpleModulus.extractPacket(decBuff);
  System.out.println("Extracted and XOR filtered C1 packet");
  System.out.println(HexUtils.toString(decBuff.array()));
 }

 @Test
 public void readKeys() throws Exception {
  //  Client
  File dec2Dat = new File("/home/briankernighan/Desktop/Dec2.dat");
  System.out.println(String.format("CLIENT: %s", dec2Dat.toString()));
//  Assertions.assertTrue(SimpleModulus.printKeys(dec2Dat));

  // Server
  File enc2Dat = new File("/home/briankernighan/Desktop/Enc2.dat");
  System.out.println(String.format("SERVER: %s", enc2Dat.toString()));
//  Assertions.assertTrue(SimpleModulus.printKeys(enc2Dat));

  // Client
  File enc1Dat = new File("/home/briankernighan/Desktop/Enc1.dat");
  System.out.println(String.format("CLIENT: %s", enc1Dat.toString()));
//  Assertions.assertTrue(SimpleModulus.printKeys(enc1Dat));

  //  Server
  File dec1Dat = new File("/home/briankernighan/Desktop/Dec1.dat");
  System.out.println(String.format("SERVER: %s", dec1Dat.toString()));
//  Assertions.assertTrue(SimpleModulus.printKeys(dec1Dat));
 }
}