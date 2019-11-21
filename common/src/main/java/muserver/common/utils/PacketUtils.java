package muserver.common.utils;

public class PacketUtils {
	public static byte[] hex2Bytes(String hex) {
		hex = hex.trim().replace(" ", "");
		int length = hex.length();
		if (length % 2 == 1)
			return null;
		byte[] data = new byte[length / 2];
		for (int i = 0; i < length; i += 2) {
			data[i >> 1] = (byte) Integer.parseInt(hex.trim().substring(i, i + 2), 16);
		}
		return data;
	}

	public static String bytesToHex(byte[] bytes) {
		String result = "";
		for (int i = 0; i < bytes.length; i++) {
			result += Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1) + " ";
		}
		return result.toUpperCase();
	}
}
