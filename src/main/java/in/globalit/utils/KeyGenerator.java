package in.globalit.utils;

import java.security.SecureRandom;

public class KeyGenerator {

	private KeyGenerator() {

	}

	private static final SecureRandom random = new SecureRandom();

	public static String generateHexKey(int length) {
		byte[] keyBytes = new byte[length / 2];
		random.nextBytes(keyBytes);
		return bytesToHex(keyBytes);
	}

	private static String bytesToHex(byte[] bytes) {
		StringBuilder hexStringBuilder = new StringBuilder();
		for (byte b : bytes) {
			hexStringBuilder.append(String.format("%02x", b));
		}
		return hexStringBuilder.toString();
	}
}