package muserver.common.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import muserver.common.Globals;

import java.io.*;
import java.nio.file.Files;

public class SimpleModulus {
 public static final Integer FILE_HEADER = 4370;
 public static final Integer ENCRYPTED_BLOCK_SIZE = 11;
 public static final Integer ENCRYPTION_KEY_SIZE = 4;
 public static final Integer ENCRYPTION_BLOCK_SIZE = 8;

 public static final Long[] READWRITE_XOR_KEY_TABLE = {Long.valueOf(0x3F08A79B), Long.valueOf(0xE25CC287), Long.valueOf(0x93D27AB9), Long.valueOf(0x20DEA7BF)};

 public static final byte[] XOR_FILTER_TABLE = new byte[]{
     (byte) 0xAB, 0x11, (byte) 0xCD, (byte) 0xFE, 0x18, 0x23, (byte) 0xC5, (byte) 0xA3, (byte) 0xCA, 0x33, (byte) 0xC1, (byte) 0xCC, 0x66, 0x67, 0x21, (byte) 0xF3, 0x32, 0x12, 0x15, 0x35, 0x29, (byte) 0xFF, (byte) 0xFE, 0x1D, 0x44, (byte) 0xEF, (byte) 0xCD, 0x41, 0x26, 0x3C, 0x4E, 0x4D,
 };

 private final SimpleKeys encKeys, decKeys;

 public SimpleModulus(SimpleKeys encKeys, SimpleKeys decKeys) {
  this.encKeys = encKeys;
  this.decKeys = decKeys;
 }

 public static boolean printKeys(File file) throws Exception {
  byte[] fileBuf = Files.readAllBytes(file.toPath());

  ByteArrayInputStream stream = new ByteArrayInputStream(fileBuf);

  byte[] content = EndianUtils.readBytes(stream, fileBuf.length);

  ByteBuf byteBuf = Unpooled.wrappedBuffer(content);

  int fileHeader = byteBuf.readUnsignedShortLE();

  if (fileHeader != FILE_HEADER) {
   throw new Exception("Invalid file header");
  }

  long fileLength = byteBuf.readUnsignedIntLE();

  if (fileLength != fileBuf.length) {
   throw new Exception("Invalid file length");
  }

  System.out.println("MODULUS");
  Long[] modulusKeys = new Long[]{
      byteBuf.readUnsignedIntLE(),
      byteBuf.readUnsignedIntLE(),
      byteBuf.readUnsignedIntLE(),
      byteBuf.readUnsignedIntLE()
  };
  for (int n = 0; n < 4; n++) {
   System.out.println(String.format("0x%X", (READWRITE_XOR_KEY_TABLE[n] ^ modulusKeys[n])));
  }

  System.out.println("KEY");
  Long[] encryptionKeys = new Long[]{
      byteBuf.readUnsignedIntLE(),
      byteBuf.readUnsignedIntLE(),
      byteBuf.readUnsignedIntLE(),
      byteBuf.readUnsignedIntLE()
  };
  for (int n = 0; n < 4; n++) {
   System.out.println(String.format("0x%X", (READWRITE_XOR_KEY_TABLE[n] ^ encryptionKeys[n])));
  }


  System.out.println("XOR");
  Long[] decryptionKeys = new Long[]{
      byteBuf.readUnsignedIntLE(),
      byteBuf.readUnsignedIntLE(),
      byteBuf.readUnsignedIntLE(),
      byteBuf.readUnsignedIntLE()
  };
  for (int n = 0; n < 4; n++) {
   System.out.println(String.format("0x%X", (READWRITE_XOR_KEY_TABLE[n] ^ decryptionKeys[n])));
  }

  return true;
 }

 public int encrypt(ByteBuf lpDest, ByteBuf lpSource, int iSize) {
  int iTempSize = iSize, iTempSize2, iOriSize;

  int iDec = ((iSize + 7) / 8);

  iSize = (iDec + iDec * 4) * 2 + iDec;

  if (lpDest != null) {
   iOriSize = iTempSize;
   for (int i = 0; i < iTempSize; i += 8, iOriSize -= 8, lpDest = lpDest.slice(11, lpDest.capacity() - 11)) {
    iTempSize2 = iOriSize;
    if (iOriSize >= 8) {
     iTempSize2 = 8;
    }
    encryptBlock(lpDest, lpSource.slice(i, lpSource.capacity() - i), iTempSize2);
   }
  }

  return iSize;
 }

 public int encryptBlock(ByteBuf lpDest, ByteBuf lpSource, int iSize) {
  ByteBuf dwEncBuffer = Unpooled.buffer(Integer.BYTES * 4);

  long dwEncValue = 0;

  for (int i = 0; i < 4; i++) {
   int position = i * Integer.BYTES;
   long val = (((encKeys.modulusKeys()[i] ^ lpSource.getUnsignedShortLE(position)) ^ dwEncValue) * decKeys.encryptionKeys()[i]) % decKeys.modulusKeys()[i];
   dwEncBuffer.setIntLE(position, (int) val);
   dwEncValue = dwEncBuffer.getUnsignedIntLE(position);
  }

  for (int i = 0; i < 3; i++) {
   int position = i * Integer.BYTES;
   long val = dwEncBuffer.getIntLE(position) ^ encKeys.xorKeys()[i] ^ (dwEncBuffer.getIntLE(position + 4));
   dwEncBuffer.setIntLE(position, (int) val);
  }

  int iBitPos = 0;

  for (int i = 0; i < 4; i++) {
   iBitPos = addBits(lpDest, iBitPos, dwEncBuffer.slice(i * 4, 4), 0, 16);
   iBitPos = addBits(lpDest, iBitPos, dwEncBuffer.slice(i * 4, 4), 22, 2);
  }

  short btCheckSum = 0xF8;

  for (int i = 0; i < 8; i++) {
   btCheckSum ^= lpSource.getUnsignedByte(i);
  }

  ByteBuf dwEncValueBuf = Unpooled.copyLong(dwEncValue);

  dwEncValueBuf.setByte(1, btCheckSum);
  dwEncValueBuf.setByte(0, btCheckSum ^ iSize ^ 0x3D);

  return addBits(lpDest, iBitPos, dwEncValueBuf, 0, 16);
 }

 public int decrypt(ByteBuf lpDest, ByteBuf lpSrc, int iSize) throws IOException {
  if (lpDest == null) {
   return (iSize * 8) / 11;
  }

  int result = 0;
  int decLen = 0;

  if (iSize > 0) {
   while (decLen < iSize) {
    int tempResult = decryptBlock(lpDest, lpSrc);

    if (result < 0) {
     return result;
    }

    result += tempResult;
    decLen += 11;

    lpSrc = lpSrc.slice(11, lpSrc.capacity() - 11);
    lpDest = lpDest.slice(8, lpDest.capacity() - 8);
   }
  }

  return result;
 }

 public int decryptBlock(ByteBuf lpDest, ByteBuf lpSource) throws IOException {
  ByteBuf dwDecBuffer = Unpooled.buffer(Integer.BYTES * 4);

  int iBitPosition = 0;

  for (int i = 0; i < 4; i++) {
   addBits(dwDecBuffer.slice(i * 4, 4), 0, lpSource, iBitPosition, 16);
   iBitPosition += 16;
   addBits(dwDecBuffer.slice(i * 4, 4), 22, lpSource, iBitPosition, 2);
   iBitPosition += 2;
  }

  for (int i = 2; i >= 0; i--) {
   int position = i * 4;
   long val = (dwDecBuffer.getUnsignedIntLE(position) ^ decKeys.xorKeys()[i]) ^ dwDecBuffer.getUnsignedShortLE(position + 4);
   dwDecBuffer.setIntLE(position, (int) val);
  }

  int temp = 0;

  for (int i = 0; i < 4; i++) {
   int position = i * Integer.BYTES;
   short val = (short) (((decKeys.encryptionKeys()[i] * (dwDecBuffer.getUnsignedIntLE(position))) % (decKeys.modulusKeys()[i])) ^ decKeys.xorKeys()[i] ^ temp);
   lpDest.setShortLE(i * Short.BYTES, val);
   temp = (short) dwDecBuffer.getUnsignedIntLE(position);
  }


  dwDecBuffer.setZero(0, 4);
  addBits(dwDecBuffer, 0, lpSource, iBitPosition, 16);
  dwDecBuffer.setByte(0, (byte) (dwDecBuffer.getUnsignedByte(1) ^ dwDecBuffer.getUnsignedByte(0) ^ 0x3D));

  short btCheckSum = 0xF8;

  for (int i = 0; i < 8; i++) {
   btCheckSum ^= lpDest.getUnsignedByte(i);
  }

  if (btCheckSum != dwDecBuffer.getUnsignedByte(1)) {
   return -1;
  }

  return dwDecBuffer.getUnsignedByte(0);
 }

 public int addBits(ByteBuf lpDest, int iDestBitPos, ByteBuf lpSource, int iBitSourcePos, int iBitLen) {
  int iSourceBufferBitLen = iBitSourcePos + iBitLen;
  int iTempBufferLen = getByteOfBit(iSourceBufferBitLen - 1) + (1 - getByteOfBit(iBitSourcePos));

  ByteBuf pTempBuffer = Unpooled.buffer(iTempBufferLen + 1);
  pTempBuffer.setZero(0, iTempBufferLen + 1);

  ByteBuf copyBuff = lpSource.copy(getByteOfBit(iBitSourcePos), iTempBufferLen);
  pTempBuffer.writeBytes(copyBuff);

  if ((iSourceBufferBitLen % 8) != 0) {
   byte val = (byte) (pTempBuffer.getUnsignedByte(iTempBufferLen - 1) & 0xFF << (8 - (iSourceBufferBitLen % 8)));
   pTempBuffer.setByte(iTempBufferLen - 1, val);
  }

  int iShiftLeft = (iBitSourcePos % 8);
  int iShiftRight = (iDestBitPos % 8);

  shift(pTempBuffer, iTempBufferLen, -iShiftLeft);
  shift(pTempBuffer, iTempBufferLen + 1, iShiftRight);

  int iNewTempBufferLen = ((iShiftRight <= iShiftLeft) ? 0 : 1) + iTempBufferLen;
  ByteBuf tempDist = lpDest.slice(getByteOfBit(iDestBitPos), iNewTempBufferLen);

  for (int i = 0; i < iNewTempBufferLen; i++) {
   byte val = (byte) (tempDist.getUnsignedByte(i) | pTempBuffer.getUnsignedByte(i));
   tempDist.setByte(i, val);
  }

  return iDestBitPos + iBitLen;
 }

 public byte getByteOfBit(int btByte) {
  byte val = (byte) (btByte >> 3);
  return val;
 }

 public void shift(ByteBuf lpBuff, int iSize, int shiftLen) {
  if (shiftLen != 0) {
   if (shiftLen > 0) {
    if ((iSize - 1) > 0) {
     for (int i = (iSize - 1); i > 0; i--) {
      byte val = (byte) ((lpBuff.getByte(i - 1) << ((8 - shiftLen))) | (lpBuff.getByte(i) >> shiftLen));
      lpBuff.setByte(i, val);
     }
    }
    byte val = (byte) (lpBuff.getByte(0) >> shiftLen);
    lpBuff.setByte(0, val);
   } else {
    shiftLen = -shiftLen;

    if ((iSize - 1) > 0) {
     for (int i = 0; i < (iSize - 1); i++) {
      int val = (lpBuff.getUnsignedByte(i + 1) >> (8 - shiftLen));
      val |= (lpBuff.getUnsignedByte(i) << shiftLen);
      lpBuff.setByte(i, val);
     }
    }

    byte val = (byte) (lpBuff.getByte(iSize - 1) << shiftLen);
    lpBuff.setByte(iSize - 1, val);
   }
  }
 }

 public void extractPacket(ByteBuf buffer) {
  switch ((byte) buffer.getUnsignedByte(0)) {
   case Globals.PMHC_BYTE: {
    int size = buffer.getUnsignedByte(1);
    xorData(buffer, size - 1, 2);
   }
   break;
   case Globals.PMHC_WORD: {
    int size = buffer.getUnsignedByte(2) | buffer.getUnsignedByte(1) << 8;
    xorData(buffer, size - 1, 3);
   }
   break;
   default:
    return;
  }
 }

 private void xorData(ByteBuf buffer, int start, int end) {
  for (int i = start; i > end; i--) {
   short val = buffer.getUnsignedByte(i);
   val ^= buffer.getUnsignedByte(i - 1) ^ XOR_FILTER_TABLE[i % 32];
   buffer.setByte(i, val);
  }
 }

 public static class SimpleKeys {
  Long[] modulusKeys, encryptionKeys, xorKeys;

  public SimpleKeys(Long[] modulusKeys, Long[] encryptionKeys, Long[] xorKeys) {
   this.modulusKeys = modulusKeys;
   this.encryptionKeys = encryptionKeys;
   this.xorKeys = xorKeys;
  }

  public Long[] modulusKeys() {
   return modulusKeys;
  }

  public Long[] encryptionKeys() {
   return encryptionKeys;
  }

  public Long[] xorKeys() {
   return xorKeys;
  }
 }
}