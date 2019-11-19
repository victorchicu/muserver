/**
 * Copyright (C) 2013-2014 Project-Vethrfolnir
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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

/**
 * @author PsychoJr
 * 
 */
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
	
	/**
	 * @return the serverToClientPacketEncKeys
	 */
	public static long[] getServerToClientPacketEncKeys() {
		return serverToClientPacketEncKeys;
	}
	
	/**
	 * @return the clientToServerPacketDecKeys
	 */
	public static long[] getClientToServerPacketDecKeys() {
		return clientToServerPacketDecKeys;
	}
	
	/**
	 * @return the clientToServerPacketEncKeys
	 */
	public static long[] getClientToServerPacketEncKeys() throws IOException {
		if(clientToServerPacketEncKeys == null) {
			readFile("static_data/keys/Enc1.dat", clientToServerPacketEncKeys = new long[12]);
		}
		return clientToServerPacketEncKeys;
	}
	
	/**
	 * @return the serverToClientPacketDecKeys
	 */
	public static long[] getServerToClientPacketDecKeys() throws IOException {
		if(serverToClientPacketDecKeys == null) {
			readFile("static_data/keys/Dec2.dat", serverToClientPacketDecKeys = new long[12]);
		}
		return serverToClientPacketDecKeys;
	}
		
}
