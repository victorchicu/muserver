package muserver.common.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;

import java.nio.ByteOrder;

import static muserver.common.utils.MuCryptUtils.*;

public final class MuEncoder {
 private static ByteBufAllocator alloc = //UnpooledByteBufAllocator.DEFAULT;
   PooledByteBufAllocator.DEFAULT;

 public static ByteBuf encodePacket(ByteBuf buff, int serial) {
  int header = GetHeaderSize(buff);
  int packetSize = GetPacketSize(buff);
  int contentSize = packetSize - header;

  int encodedSize = (((contentSize / 8) + (((contentSize % 8) > 0) ? 1 : 0)) * 11) + header;

  int size = header;
  int originalHead = buff.getUnsignedByte(0);

  ByteBuf out = alloc.heapBuffer(encodedSize, encodedSize);

  //buff.writerIndex(buff.writerIndex() + 1);
  short[] Contents = new short[contentSize + 1];
  Contents[0] = (short) serial; // XXX:  Check this

  buff.readerIndex(header - 1);

  buff.setByte(header - 1, serial);

  MuCryptUtils.readAsUByteArray(buff, Contents);

  //System.out.println("Encoding: "+PrintData.printData(Contents));

  size += encodeBuffer(Contents, out, header, (contentSize + 1));

  out.writerIndex(0);

  // Header
  out.writeByte(originalHead);

  // Size write
  switch (originalHead) {
   case 0xc3:
    out.writeByte(size);
    break;
   case 0xC4:
    out.writeByte(size >> 8);
    out.writeByte(size & 0xFF);
    break;
  }

  out.writerIndex(size);

  return out;
 }

 private static int encodeBuffer(short[] contents, ByteBuf out, int header, int Size) {
  int i = 0;
  int EncSize = 0;

  while (i < Size) {
   short[] Encrypted = new short[11];

   if (i + 8 < Size) {
    short[] Decrypted = new short[8];
    System.arraycopy(contents, i, Decrypted, 0, 8);
    encodeBlock(Encrypted, Decrypted, 8, MuKeyFactory.getServerToClientPacketEncKeys());
   } else {
    short[] Decrypted = new short[Size - i];
    System.arraycopy(contents, i, Decrypted, 0, Decrypted.length);

    encodeBlock(Encrypted, Decrypted, (Size - i), MuKeyFactory.getServerToClientPacketEncKeys());
   }

   out.writerIndex(header + EncSize);
   for (int j = 0; j < Encrypted.length; j++) {
    out.writeByte(Encrypted[j]);
   }

   i += 8;
   EncSize += 11;
  }

  return EncSize;
 }

 private static void encodeBlock(short[] OutBuf, short[] InBuf, int Size, long[] Keys) {
  short[] Finale = new short[2];
  Finale[0] = (short) Size;
  Finale[0] ^= 0x3D;
  Finale[1] = 0xF8;

  for (int i = 0; i < Size; i++)
   Finale[1] ^= InBuf[i];

  Finale[0] ^= Finale[1];

  shiftBytes(OutBuf, 0x48, Finale, 0x00, 0x10);

  //System.err.println("Encode block: ------------------ Size: "+Size);
  //System.err.println(PrintData.printData(InBuf));
  //System.err.println();

  long[] Ring = new long[4];
  char[] CryptBuf = new char[4];

  // Here was a block copy, that wasent really needed
  CryptBuf[0] = convertShort(InBuf[0], InBuf.length > 1 ? InBuf[1] : 0x00);
  CryptBuf[1] = convertShort(InBuf.length > 2 ? InBuf[2] : 0x00, InBuf.length > 3 ? InBuf[3] : 0x00);
  CryptBuf[2] = convertShort(InBuf.length > 4 ? InBuf[4] : 0x00, InBuf.length > 5 ? InBuf[5] : 0x00);
  CryptBuf[3] = convertShort(InBuf.length > 6 ? InBuf[6] : 0x00, InBuf.length > 7 ? InBuf[7] : 0x00);

  Ring[0] = ((Keys[8] ^ (CryptBuf[0])) * Keys[4]) % Keys[0];
  Ring[1] = ((Keys[9] ^ (CryptBuf[1] ^ (Ring[0] & 0xFFFF))) * Keys[5]) % Keys[1];
  Ring[2] = ((Keys[10] ^ (CryptBuf[2] ^ (Ring[1] & 0xFFFF))) * Keys[6]) % Keys[2];
  Ring[3] = ((Keys[11] ^ (CryptBuf[3] ^ (Ring[2] & 0xFFFF))) * Keys[7]) % Keys[3];

  //Buffer.BlockCopy(CryptBuf, 0, InBuf, 0, (int)Size);

  Ring[0] = Ring[0] ^ Keys[8] ^ (Ring[1] & 0xFFFF);
  Ring[1] = Ring[1] ^ Keys[9] ^ (Ring[2] & 0xFFFF);
  Ring[2] = Ring[2] ^ Keys[10] ^ (Ring[3] & 0xFFFF);

  //Current ring here 1d4b 57ae 74ce 8866
  short[] Shift = new short[4];

  // 4B 1D 00 00 - after block copy must achive this
  //Buffer.BlockCopy(Ring, 0, Shift, 0, 4);
  copyRingArray(Ring[0], Shift);
  shiftBytes(OutBuf, 0x00, Shift, 0x00, 0x10);
  shiftBytes(OutBuf, 0x10, Shift, 0x16, 0x02);

  //Buffer.BlockCopy(Ring, 4, Shift, 0, 4);
  copyRingArray(Ring[1], Shift);
  shiftBytes(OutBuf, 0x12, Shift, 0x00, 0x10);
  shiftBytes(OutBuf, 0x22, Shift, 0x16, 0x02);

  //Buffer.BlockCopy(Ring, 8, Shift, 0, 4);
  copyRingArray(Ring[2], Shift);
  shiftBytes(OutBuf, 0x24, Shift, 0x00, 0x10);
  shiftBytes(OutBuf, 0x34, Shift, 0x16, 0x02);

  //Buffer.BlockCopy(Ring, 12, Shift, 0, 4);
  copyRingArray(Ring[3], Shift);
  shiftBytes(OutBuf, 0x36, Shift, 0x00, 0x10);
  shiftBytes(OutBuf, 0x46, Shift, 0x16, 0x02);
 }

 private static void copyRingArray(long ring, short[] shift) {
  short[] ss = intToByteArray((int) ring);
  for (int i = 0; i < ss.length; i++) {
   shift[i] = (short) (ss[i]);
  }
 }

 private static final short[] intToByteArray(int value) { // little endiness
  return new short[]{(short) (value & 0xff), (short) (value >> 8 & 0xff), (short) (value >> 16 & 0xff), (short) (value >>> 24)};
 }

 private static char convertShort(int b1, int b2) {
  char result = (char) (b1 & 0xff);
  result |= b2 << 8 & 0xff00;
  return result;
 }

 private static long shiftBytes(short[] OutBuf, long Arg_4, short[] InBuf, long Arg_C, long Arg_10) {
  long Size = ((((Arg_10 + Arg_C) - 1) / 8) + (1 - (Arg_C / 8)));
  short[] Tmp = new short[20];
  System.arraycopy(InBuf, (int) (Arg_C / 8), Tmp, 0, (int) Size);
  long Var_4 = (Arg_10 + Arg_C) & 0x7;

  if (Var_4 != 0) // Pay attention, too many casts, look for negatives
   Tmp[(int) (Size - 1)] = (short) (Tmp[(int) (Size - 1)] & 0xFF << (int) (8 - Var_4));

  Arg_C &= 0x7;

  shiftRight(Tmp, (int) Size, (int) Arg_C);
  shiftLeft(Tmp, (int) Size + 1, (int) (Arg_4 & 0x7));

  if ((Arg_4 & 0x7) > Arg_C) ++Size;
  if (Size != 0)
   for (int i = 0; i < Size; ++i)
    OutBuf[(int) (i + (Arg_4 / 8))] = (short) ((byte) (OutBuf[(int) (i + (Arg_4 / 8))] | Tmp[i]) & 0xFF);

  return Arg_10 + Arg_4;
 }

 private static void shiftLeft(short[] Data, int Size, int Shift) {
  if (Shift == 0) return;
  for (int i = 1; i < Size; i++)
   Data[Size - i] = (byte) ((Data[Size - i] >> Shift) | ((Data[Size - i - 1]) << (8 - Shift)));

  Data[0] = (short) ((byte) (Data[0] >> Shift) & 0xFF);
 }

 private static void shiftRight(short[] Data, int Size, int Shift) {
  if (Shift == 0) return;
  for (int i = 1; i < Size; i++)
   Data[i - 1] = (byte) ((Data[i - 1] << Shift) | (Data[i] >> (8 - Shift)));

  Data[Size - 1] = (short) ((byte) (Data[Size - 1] << Shift) & 0xFF);
 }

 public static void main(String[] args) throws Exception {
  MuKeyFactory.parse(System.getProperty("user.dir"));

  byte[] data = PacketUtils.hex2Bytes("C4 00 2D 01 F3 10 03 00 00 00 12 00 00 10 00 FF FF FF FF FF 0C 14 08 1E 00 00 D0 00 FF FF FF FF FF 0D 14 10 1E 00 00 D0 00 FF FF FF FF");
  ByteBuf buff = Unpooled.buffer().order(ByteOrder.LITTLE_ENDIAN);
  buff.writeBytes(data);

  ByteBuf out = encodePacket(buff, 0x02);

  byte[] arr = new byte[out.readableBytes()];
  out.readBytes(arr);

  System.out.println("C3-0D-FE-53-65-66-18-AB-51-01-C1-4D-77");
  System.out.println(PacketUtils.bytesToHex(arr).toUpperCase());
 }
}