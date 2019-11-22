package muserver.common.utils;

import io.netty.buffer.*;
import muserver.common.handlers.BasePacketHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.ByteOrder;

import static muserver.common.utils.MuCryptUtils.*;

public final class MuDecoder {
	private static final Logger logger = LogManager.getLogger(MuDecoder.class);

	// Internal use
	private static ByteBufAllocator alloc = //UnpooledByteBufAllocator.DEFAULT;
			PooledByteBufAllocator.DEFAULT;

	// GMO KEYS
	private static short[] Xor32Keys = new short[]{
			0xAB, 0x11, 0xCD, 0xFE, 0x18, 0x23, 0xC5, 0xA3,
			0xCA, 0x33, 0xC1, 0xCC, 0x66, 0x67, 0x21, 0xF3,
			0x32, 0x12, 0x15, 0x35, 0x29, 0xFF, 0xFE, 0x1D,
			0x44, 0xEF, 0xCD, 0x41, 0x26, 0x3C, 0x4E, 0x4D
	};

	public static ByteBuf decodeXor32(ByteBuf buff) {

		if (buff.readerIndex() != 0) {
			logger.warn("Buffer must be at index 0!");
			buff.readerIndex(0);
		}

		int header = GetHeaderSize(buff);
		int decodedSize = GetDecodedSize(buff);

		buff.readerIndex(header);
		decXor32(buff, header, decodedSize - header);
		buff.readerIndex(0);
		return buff;
	}

	public static ByteBuf decodePacket(ByteBuf buff) {
		if (buff.writerIndex() <= 2) {
			logger.fatal("Ambiguous buffer! " + ByteBufUtil.hexDump(buff));
			return null;
		}
		if (buff.readerIndex() != 0) {
			logger.warn("Buffer must be at index 0!");
			buff.readerIndex(0);
		}

		int header = GetHeaderSize(buff);
		int packetSize = GetPacketSize(buff);
		int decodedSize = GetDecodedSize(buff);

		int contentSize = packetSize - header;

		//System.out.println("Header[0x"+PrintData.fillHex(buff.getUnsignedByte(0), 2)+"] size: "+GetHeaderSize(buff)+" Packet Size: "+GetPacketSize(buff) +" Content Size: "+contentSize +" Decoded: "+ GetDecodedSize(buff));

		ByteBuf out = alloc.heapBuffer(decodedSize, decodedSize);

		int originalHead = buff.getUnsignedByte(0);

		buff.readerIndex(header);
		int size = decodeBlock(buff, out, header, contentSize);
		//buff.clear();

		size += header - 1;

		out.writerIndex(0);
		out.writeByte(originalHead);

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
		out.readerIndex(header);

		decXor32(out, header, size);

		out.readerIndex(0);
		return out;
	}

	private static int decodeBlock(ByteBuf inBuff, ByteBuf outBuff, int offset, int size) {
		// decripted size
		int index = 0;

		if ((size % 11) != 0) {
			logger.warn("Cannot decrypt packet, it's already decrypted!: Size " + size + " = " + ((size % 11)));
//									logger.warn(PrintData.printData(buff.nioBuffer()));
			return -1;
		}

		ByteBuf encrypted = alloc.heapBuffer(11, 11).order(ByteOrder.LITTLE_ENDIAN);
		short[] uByteArray = new short[encrypted.capacity()];

		ByteBuf decrypted = alloc.heapBuffer(8, 8).order(ByteOrder.LITTLE_ENDIAN);
		ByteBuf converter = alloc.heapBuffer(4).order(ByteOrder.LITTLE_ENDIAN);

		for (int i = 0; i < size; i += 11) {
			inBuff.readBytes(encrypted);

			//System.out.println("ENC: "+PrintData.printData(encrypted.nioBuffer()));
			int Result = decodeBlock(decrypted, getAsUByteArray(encrypted, uByteArray), converter, MuKeyFactory.getClientToServerPacketDecKeys());
			if (Result != -1) {
				//Buffer.BlockCopy(Decrypted, 0, m_DecryptResult, (OffSet - 1) + DecSize, Result);

				outBuff.writerIndex((offset - 1) + index);
				outBuff.writeBytes(decrypted);


				//outBuff.writeBytes(decrypted);

				decrypted.clear();
				encrypted.clear();
				converter.clear();
				//System.arraycopy(Decrypted, 0, m_DecryptResult, (OffSet - 1) + DecSize, Result);

				index += Result;
			}
		}

		return index;
	}

	private static int decodeBlock(ByteBuf decrypted, short[] inBuf, ByteBuf converter, long[] keys) {

		long[] Ring = new long[4];
		short[] Shift = new short[4];

		shiftBytes(Shift, 0x00, inBuf, 0x00, 0x10);
		shiftBytes(Shift, 0x16, inBuf, 0x10, 0x02);

		writeByteArray(converter, Shift);
		flushArray(Shift, 0, 4);

		shiftBytes(Shift, 0x00, inBuf, 0x12, 0x10);
		shiftBytes(Shift, 0x16, inBuf, 0x22, 0x02);

		writeByteArray(converter, Shift);
		flushArray(Shift, 0, 4);

		shiftBytes(Shift, 0x00, inBuf, 0x24, 0x10);
		shiftBytes(Shift, 0x16, inBuf, 0x34, 0x02);

		writeByteArray(converter, Shift);
		flushArray(Shift, 0, 4);

		shiftBytes(Shift, 0x00, inBuf, 0x36, 0x10);
		shiftBytes(Shift, 0x16, inBuf, 0x46, 0x02);

		writeByteArray(converter, Shift);
		flushArray(Shift, 0, 4);

		// for (int i = 0; i < Ring.length; i++) {
		// System.err.print(Integer.toHexString((int) Ring[i])+" ");;
		// }

		// System.err.println();

		for (int i = 0; i < Ring.length; i++) {
			Ring[i] = converter.readInt();
		}
		converter.clear();

		Ring[2] = Ring[2] ^ keys[10] ^ (Ring[3] & 0xFFFF);
		Ring[1] = Ring[1] ^ keys[9] ^ (Ring[2] & 0xFFFF);
		Ring[0] = Ring[0] ^ keys[8] ^ (Ring[1] & 0xFFFF);


//		 System.err.println("Finished Ring: ");
//		 for (int i = 0; i < Ring.length; i++) {
//		 System.err.print(Integer.toHexString((int) Ring[i])+" ");;
//		 }
//		 System.err.println();
		int[] CryptBuf = new int[4];

		// Had ushort cast here.
		CryptBuf[0] = (int) (keys[8] ^ ((Ring[0] * keys[4]) % keys[0]));

		CryptBuf[1] = (int) (keys[9] ^ ((Ring[1] * keys[5]) % keys[1]) ^ (Ring[0] & 0xFFFF));
		CryptBuf[2] = (int) (keys[10] ^ ((Ring[2] * keys[6]) % keys[2]) ^ (Ring[1] & 0xFFFF));
		CryptBuf[3] = (int) (keys[11] ^ ((Ring[3] * keys[7]) % keys[3]) ^ (Ring[2] & 0xFFFF));

//		System.err.println("Pre done: " + PrintData.printData(CryptBuf));

		short[] Finale = new short[2];
		shiftBytes(Finale, 0x00, inBuf, 0x48, 0x10);
		Finale[0] ^= Finale[1];
		Finale[0] ^= 0x3D;

		converter.clear();
		for (int i = 0; i < CryptBuf.length; i++) {
			converter.writeShort(CryptBuf[i]);
		}

		decrypted.writeBytes(converter, Finale[0]);
		converter.clear();

		//System.out.println(PrintData.printData(decrypted.nioBuffer()));

		// System.err.println("Finale: "+ Finale[0]);

		short Check = 0xF8;
		for (int i = 0; i < Finale[0]; ++i)
			Check = (short) (Check ^ decrypted.getUnsignedByte(i));

		if (Finale[1] == Check)
			return Finale[0];

		//System.err.println("Finale["+Finale[0]+"] And done: "+PrintData.printData(decrypted.nioBuffer()));

		return Finale[0];
	}

	private static long shiftBytes(short[] outBuf, long arg4, short[] inBuf, long argC, long arg10) {
		long Size = ((((arg10 + argC) - 1) / 8) + (1 - (argC / 8)));

		short[] Tmp = new short[20];
		System.arraycopy(inBuf, (int) (argC / 8), Tmp, 0, (int) Size);

		long Var_4 = (arg10 + argC) & 0x7;

		if (Var_4 != 0) // Pay attention, too many casts, look for negatives
			Tmp[(int) (Size - 1)] = (short) (Tmp[(int) (Size - 1)] & 0xFF << (int) (8 - Var_4));

		argC &= 0x7;

		shiftRight(Tmp, (int) Size, (int) argC);
		shiftLeft(Tmp, (int) Size + 1, (int) (arg4 & 0x7));

		if ((arg4 & 0x7) > argC) ++Size;
		if (Size != 0)
			for (int i = 0; i < Size; ++i) {
				outBuf[(int) (i + (arg4 / 8))] = (short) ((byte) (outBuf[(int) (i + (arg4 / 8))] | Tmp[i]) & 0xFF);
			}

		return arg10 + arg4;
	}

	private static void shiftLeft(short[] data, int size, int shift) {
		if (shift == 0) return;
		for (int i = 1; i < size; i++)
			data[size - i] = (byte) ((data[size - i] >> shift) | ((data[size - i - 1]) << (8 - shift)));

		data[0] = (short) ((byte) (data[0] >> shift) & 0xFF);
	}

	private static void shiftRight(short[] data, int size, int shift) {

		if (shift == 0) return;
		for (int i = 1; i < size; i++)
			data[i - 1] = (byte) ((data[i - 1] << shift) | (data[i] >> (8 - shift)));

		data[size - 1] = (short) ((byte) (data[size - 1] << shift) & 0xFF);
	}

	private static void decXor32(ByteBuf buff, int sizeOfHeader, int len) {
		for (int i = len - 1; i > 0; i--) {
			int Buff = buff.getUnsignedByte(buff.readerIndex() + i);
			Buff ^= (Xor32Keys[(i + sizeOfHeader) & 31] ^ buff.getUnsignedByte((buff.readerIndex() + i) - 1));
			buff.setByte(buff.readerIndex() + i, Buff);
		}
	}

	public static void main(String[] args) throws Exception {
		MuKeyFactory.parse(System.getProperty("user.dir"));

		byte[] data = PacketUtils.hex2Bytes("C3 5A 9E 4D 18 56 28 FB 20 E5 2D A7 92 5A 01 33 CB 50 BA F0 10 69 76 43 16 D1 65 36 64 13 F1 45 CC 1A 2F B1 B7 4E 24 49 1F F2 2F 54 A7 92 F8 04 7E 8A A7 A7 73 EF 00 C9 FC E2 CF 35 E4 58 21 A0 5C 89 16 23 B2 8C 5C 4C 62 23 B2 8B 90 27 12 61 07 5D 00 4D C3 F3 5A 31 99 A7");

		ByteBuf buff = Unpooled.buffer().order(ByteOrder.LITTLE_ENDIAN);

		ByteBuf out = null;
		for (int i = 0; i < 1; i++) {
			buff.writeBytes(data);

			long t1 = System.currentTimeMillis();
			//DecodeXor32(buff);
			out = decodePacket(buff);
			long t2 = System.currentTimeMillis();

			System.out.println("Process time: " + (t2 - t1) + " milis");

		}
		System.out.println(PrintData.printData(out.nioBuffer()));
	}
}
