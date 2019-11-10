package muserver.common.utils;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class EndianUtils {
 public static void writeByte(byte[] data, int offset, byte value) {
  data[offset + 0] = (byte) (value >> 0 & 0xFF);
 }

 public static void writeByte(OutputStream output, byte value) throws IOException {
  output.write((byte) (value >> 0 & 0xFF));
 }

 public static void writeBytes(OutputStream output, byte[] value) throws IOException {
  output.write(value);
 }

 public static void writeShortBE(byte[] data, int offset, short value) {
  data[offset + 0] = (byte) (value >> 0 & 0xFF);
  data[offset + 1] = (byte) (value >> 8 & 0xFF);
 }

 public static void writeShortBE(OutputStream output, short value) throws IOException {
  output.write((byte) (value >> 0 & 0xFF));
  output.write((byte) (value >> 8 & 0xFF));
 }

 public static void writeShortLE(byte[] data, int offset, short value) {
  data[offset + 0] = (byte) (value >> 8 & 0xFF);
  data[offset + 1] = (byte) (value >> 0 & 0xFF);
 }

 public static void writeShortLE(OutputStream output, short value) throws IOException {
  output.write((byte) (value >> 8 & 0xFF));
  output.write((byte) (value >> 0 & 0xFF));
 }

 public static void writeIntegerBE(byte[] data, int offset, int value) {
  data[offset + 0] = (byte) (value >> 0 & 0xFF);
  data[offset + 1] = (byte) (value >> 8 & 0xFF);
  data[offset + 2] = (byte) (value >> 16 & 0xFF);
  data[offset + 3] = (byte) (value >> 24 & 0xFF);
 }

 public static void writeIntegerBE(OutputStream output, int value) throws IOException {
  output.write((byte) (value >> 0 & 0xFF));
  output.write((byte) (value >> 8 & 0xFF));
  output.write((byte) (value >> 16 & 0xFF));
  output.write((byte) (value >> 24 & 0xFF));
 }

 public static void writeIntegerLE(byte[] data, int offset, int value) {
  data[offset + 0] = (byte) (value >> 24 & 0xFF);
  data[offset + 1] = (byte) (value >> 16 & 0xFF);
  data[offset + 2] = (byte) (value >> 8 & 0xFF);
  data[offset + 3] = (byte) (value >> 0 & 0xFF);
 }

 public static void writeIntegerLE(OutputStream output, int value) throws IOException {
  output.write((byte) (value >> 24 & 0xFF));
  output.write((byte) (value >> 16 & 0xFF));
  output.write((byte) (value >> 8 & 0xFF));
  output.write((byte) (value >> 0 & 0xFF));
 }

 public static void writeLongBE(byte[] data, int offset, long value) {
  data[offset + 0] = (byte) ((int) (value >> 0 & 0xFF));
  data[offset + 1] = (byte) ((int) (value >> 8 & 0xFF));
  data[offset + 2] = (byte) ((int) (value >> 16 & 0xFF));
  data[offset + 3] = (byte) ((int) (value >> 24 & 0xFF));
  data[offset + 4] = (byte) ((int) (value >> 32 & 0xFF));
  data[offset + 5] = (byte) ((int) (value >> 40 & 0xFF));
  data[offset + 6] = (byte) ((int) (value >> 48 & 0xFF));
  data[offset + 7] = (byte) ((int) (value >> 56 & 0xFF));
 }

 public static void writeLongBE(OutputStream output, long value) throws IOException {
  output.write((byte) ((int) (value >> 0 & 0xFF)));
  output.write((byte) ((int) (value >> 8 & 0xFF)));
  output.write((byte) ((int) (value >> 16 & 0xFF)));
  output.write((byte) ((int) (value >> 24 & 0xFF)));
  output.write((byte) ((int) (value >> 32 & 0xFF)));
  output.write((byte) ((int) (value >> 40 & 0xFF)));
  output.write((byte) ((int) (value >> 48 & 0xFF)));
  output.write((byte) ((int) (value >> 56 & 0xFF)));
 }

 public static void writeLongLE(byte[] data, int offset, long value) {
  data[offset + 0] = (byte) ((int) (value >> 56 & 0xFF));
  data[offset + 1] = (byte) ((int) (value >> 48 & 0xFF));
  data[offset + 2] = (byte) ((int) (value >> 40 & 0xFF));
  data[offset + 3] = (byte) ((int) (value >> 32 & 0xFF));
  data[offset + 4] = (byte) ((int) (value >> 24 & 0xFF));
  data[offset + 5] = (byte) ((int) (value >> 16 & 0xFF));
  data[offset + 6] = (byte) ((int) (value >> 8 & 0xFF));
  data[offset + 7] = (byte) ((int) (value >> 0 & 0xFF));
 }

 public static void writeLongLE(OutputStream output, long value) throws IOException {
  output.write((byte) ((int) (value >> 56 & 0xFF)));
  output.write((byte) ((int) (value >> 48 & 0xFF)));
  output.write((byte) ((int) (value >> 40 & 0xFF)));
  output.write((byte) ((int) (value >> 32 & 0xFF)));
  output.write((byte) ((int) (value >> 24 & 0xFF)));
  output.write((byte) ((int) (value >> 16 & 0xFF)));
  output.write((byte) ((int) (value >> 8 & 0xFF)));
  output.write((byte) ((int) (value >> 0 & 0xFF)));
 }

 public static void writeFloatBE(byte[] data, int offset, float value) {
  writeIntegerBE(data, offset, Float.floatToIntBits(value));
 }

 public static void writeFloatBE(OutputStream output, float value) throws IOException {
  writeIntegerBE(output, Float.floatToIntBits(value));
 }

 public static void writeFloatLE(byte[] data, int offset, float value) {
  writeIntegerLE(data, offset, Float.floatToIntBits(value));
 }

 public static void writeFloatLE(OutputStream output, float value) throws IOException {
  writeIntegerLE(output, Float.floatToIntBits(value));
 }

 public static void writeDoubleBE(byte[] data, int offset, double value) {
  writeLongBE(data, offset, Double.doubleToLongBits(value));
 }

 public static void writeDoubleBE(OutputStream output, double value) throws IOException {
  writeLongBE(output, Double.doubleToLongBits(value));
 }

 public static void writeDoubleLE(byte[] data, int offset, double value) {
  writeLongLE(data, offset, Double.doubleToLongBits(value));
 }

 public static void writeDoubleLE(OutputStream output, double value) throws IOException {
  writeLongLE(output, Double.doubleToLongBits(value));
 }

 public static void writeString(OutputStream output, String value, int count) throws IOException {
  byte[] buffer = new byte[count];

  int index = 0;

  for (byte val : value.getBytes()) {
   buffer[index] = val;
   index++;
  }

  output.write(buffer);
 }

 public static byte readByte(byte[] data, int offset) {
  return (byte) ((data[offset + 0] & 0xFF) << 0);
 }

 public static byte readByte(InputStream input) throws IOException {
  return (byte) ((read(input) & 0xFF) << 0);
 }

 public static byte[] readBytes(InputStream stream, int count) throws IOException {
  byte[] buffer = new byte[count];
  int offset = 0;
  while (offset < buffer.length) {
   int read = stream.read(buffer, offset, buffer.length - offset);
   if (read < 0) {
    throw new IOException();
   }
   if (read > 0) {
    offset += read;
   } else {
    Thread.yield();
   }
  }
  return buffer;
 }

 public static short readShortBE(byte[] data, int offset) {
  return (short) (((data[offset + 1] & 0xFF) << 0) | ((data[offset + 0] & 0xFF) << 8));
 }

 public static short readShortBE(InputStream input) throws IOException {
  return (short) (((read(input) & 0xFF) << 8) | ((read(input) & 0xFF) << 0));
 }

 public static short readShortLE(byte[] data, int offset) {
  return (short) (((data[offset + 1] & 0xFF) << 8) | ((data[offset + 0] & 0xFF) << 0));
 }

 public static short readShortLE(InputStream input) throws IOException {
  return (short) (((read(input) & 0xFF) << 0) | ((read(input) & 0xFF) << 8));
 }

 public static int readIntegerBE(byte[] data, int offset) {
  return ((data[offset + 3] & 0xFF) << 0) | ((data[offset + 2] & 0xFF) << 8) | ((data[offset + 1] & 0xFF) << 16) | ((data[offset + 0] & 0xFF) << 24);
 }

 public static int readIntegerBE(InputStream input) throws IOException {
  return ((read(input) & 0xFF) << 24) | ((read(input) & 0xFF) << 16) | ((read(input) & 0xFF) << 8) | ((read(input) & 0xFF) << 0);
 }

 public static int readIntegerLE(byte[] data, int offset) {
  return ((data[offset + 3] & 0xFF) << 24) | ((data[offset + 2] & 0xFF) << 16) | ((data[offset + 1] & 0xFF) << 8) | ((data[offset + 0] & 0xFF) << 0);
 }

 public static int readIntegerLE(InputStream input) throws IOException {
  return ((read(input) & 0xFF) << 0) | ((read(input) & 0xFF) << 8) | ((read(input) & 0xFF) << 16) | ((read(input) & 0xFF) << 24);
 }

 public static long readLongBE(byte[] data, int offset) {
  return (((data[offset + 7] & 0xFF) << 0) | ((data[offset + 6] & 0xFF) << 8) | ((data[offset + 5] & 0xFF) << 16) | ((data[offset + 4] & 0xFF) << 24) | ((data[offset + 3] & 0xFF) << 32) | ((data[offset + 2] & 0xFF) << 40) | ((data[offset + 1] & 0xFF) << 48) | ((data[offset + 0] & 0xFF) << 56));
 }

 public static long readLongBE(InputStream input) throws IOException {
  return (((read(input) & 0xFF) << 56) | ((read(input) & 0xFF) << 48) | ((read(input) & 0xFF) << 40) | ((read(input) & 0xFF) << 32) | ((read(input) & 0xFF) << 24) | ((read(input) & 0xFF) << 16) | ((read(input) & 0xFF) << 8) | ((read(input) & 0xFF) << 0));
 }

 public static long readLongLE(byte[] data, int offset) {
  return (((data[offset + 7] & 0xFF) << 56) | ((data[offset + 6] & 0xFF) << 48) | ((data[offset + 5] & 0xFF) << 40) | ((data[offset + 4] & 0xFF) << 32) | ((data[offset + 3] & 0xFF) << 24) | ((data[offset + 2] & 0xFF) << 16) | ((data[offset + 1] & 0xFF) << 8) | ((data[offset + 0] & 0xFF) << 0));
 }

 public static long readLongLE(InputStream input) throws IOException {
  return (((read(input) & 0xFF) << 0) | ((read(input) & 0xFF) << 8) | ((read(input) & 0xFF) << 16) | ((read(input) & 0xFF) << 24) | ((read(input) & 0xFF) << 32) | ((read(input) & 0xFF) << 40) | ((read(input) & 0xFF) << 48) | ((read(input) & 0xFF) << 56));
 }

 public static float readFloatBE(byte[] data, int offset) {
  return Float.intBitsToFloat(readIntegerBE(data, offset));
 }

 public static float readFloatBE(InputStream input) throws IOException {
  return Float.intBitsToFloat(readIntegerBE(input));
 }

 public static float readFloatLE(byte[] data, int offset) {
  return Float.intBitsToFloat(readIntegerLE(data, offset));
 }

 public static float readFloatLE(InputStream input) throws IOException {
  return Float.intBitsToFloat(readIntegerLE(input));
 }

 public static double readDoubleBE(byte[] data, int offset) {
  return Double.longBitsToDouble(readLongBE(data, offset));
 }

 public static double readDoubleBE(InputStream input) throws IOException {
  return Double.longBitsToDouble(readLongBE(input));
 }

 public static double readDoubleLE(byte[] data, int offset) {
  return Double.longBitsToDouble(readLongLE(data, offset));
 }

 public static double readDoubleLE(InputStream input) throws IOException {
  return Double.longBitsToDouble(readLongLE(input));
 }

 private static int read(InputStream input) throws IOException {
  int value = input.read();
  if (value == -1) {
   throw new EOFException("Unexpected EOF reached");
  } else {
   return value;
  }
 }
}