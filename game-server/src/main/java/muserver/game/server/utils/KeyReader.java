package muserver.game.server.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;

public class KeyReader {
	private static long[] xorTabDatFile = new long[]{0x3F08A79B, 0xE25CC287, 0x93D27AB9, 0x20DEA7BF};
	private static long[] enc2KeySet = new long[12];
	private static long[] dec1KeySet = new long[12];

	public static long[] geEnc2KeySet() {
		return enc2KeySet;
	}

	public static long[] getDec1KeySet() {
		return dec1KeySet;
	}

	public static long[] readEnc2DatFile(Path path) throws IOException {
		readFile(path, enc2KeySet);
		return enc2KeySet;
	}

	public static long[] readDec1DatFile(Path path) throws IOException {
		readFile(path, dec1KeySet);
		return dec1KeySet;
	}

	private static void readFile(Path path, long[] out_dat) throws IOException {
		ByteBuf buff = Unpooled.buffer().order(ByteOrder.LITTLE_ENDIAN);

		byte[] bytes = Files.readAllBytes(path);

		try (InputStream stream = new ByteArrayInputStream(bytes)) {
			buff.writeBytes(stream, stream.available());
		} catch (IOException e) {
			e.printStackTrace();
		}

		buff.readerIndex(6);
		int pointer = 0;
		for (int i = 0; i < 3; i++) {
			long[] buf = new long[4];

			for (int j = 0; j < 4; j++) {
				buf[j] = buff.readUnsignedInt();
			}

			out_dat[pointer++] = buf[0] ^ (xorTabDatFile[0]);
			out_dat[pointer++] = buf[1] ^ (xorTabDatFile[1] & 0xFFFFFFFFL);
			out_dat[pointer++] = buf[2] ^ (xorTabDatFile[2] & 0xFFFFFFFFL);
			out_dat[pointer++] = buf[3] ^ (xorTabDatFile[3]);
		}
	}
}