package muserver.common.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MuKeyFactory {
	private static long[] xor_tab_datfile = new long[] { 0x3F08A79B, 0xE25CC287, 0x93D27AB9, 0x20DEA7BF };
	private static long[] clientToServerPacketEncKeys;
	private static long[] serverToClientPacketEncKeys = new long[12];
	private static long[] clientToServerPacketDecKeys = new long[12];
	private static long[] serverToClientPacketDecKeys;

	public static void parse(String path) throws IOException {
		readFile(String.format("%s/Enc2.dat",path), serverToClientPacketEncKeys);
		readFile(String.format("%s/Dec1.dat", path), clientToServerPacketDecKeys);
	}

	private static void readFile(String path, long[] out_dat) throws IOException {
		byte[] fileBuf = Files.readAllBytes(Paths.get(path));

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
		int pointer = 0;
		for (int i = 0; i < 3; i++) {
			long[] buf = new long[4];

			for (int j = 0; j < 4; j++) {
				buf[j] = buff.readUnsignedInt();
			}

			out_dat[pointer++] = buf[0] ^ (xor_tab_datfile[0]);
			out_dat[pointer++] = buf[1] ^ (xor_tab_datfile[1] & 0xFFFFFFFFL);
			out_dat[pointer++] = buf[2] ^ (xor_tab_datfile[2] & 0xFFFFFFFFL);
			out_dat[pointer++] = buf[3] ^ (xor_tab_datfile[3]);
		}
	}

	public static long[] getServerToClientPacketEncKeys() {
		return serverToClientPacketEncKeys;
	}

	public static long[] getClientToServerPacketDecKeys() {
		return clientToServerPacketDecKeys;
	}

	public static long[] getClientToServerPacketEncKeys() throws IOException {
		if(clientToServerPacketEncKeys == null) {
			readFile("static_data/keys/Enc1.dat", clientToServerPacketEncKeys = new long[12]);
		}
		return clientToServerPacketEncKeys;
	}

	public static long[] getServerToClientPacketDecKeys() throws IOException {
		if(serverToClientPacketDecKeys == null) {
			readFile("static_data/keys/Dec2.dat", serverToClientPacketDecKeys = new long[12]);
		}
		return serverToClientPacketDecKeys;
	}
}
