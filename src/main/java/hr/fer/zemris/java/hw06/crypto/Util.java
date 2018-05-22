package hr.fer.zemris.java.hw06.crypto;

/**
 * Class with utility methods for converting byte arrays to hexadecimal
 * representations and reverse.
 * <p>
 * {@link Util#byteToHex(byte[])} <br>
 * {@link Util#hexToByte(String)}
 * @author Mirna Baksa
 *
 */
public class Util {
	/**Alphabet supported for the hexadecimal number system.*/
	private static final char[] hexAlphabet = "0123456789abcdef".toCharArray();

	/**
	 * Converts a String representing hexadecimal values into an array of bytes
	 * of those same values. Each byte in the array will represent two
	 * characters in the given string. Characters are considered to be in big -
	 * endian notation.
	 * <p>
	 * In case of invalid input, an {@link IllegalArgumentException} will be
	 * thrown. Valid input is even - sized and does not contain characters that
	 * are not in the hexadecimal alphabet (digits 0-9 and letters a-e, both
	 * upper and lower case).
	 * 
	 * @param keyText
	 *            input to convert to byte array
	 * @return byte array of values represented in the string
	 */
	public static byte[] hexToByte(String keyText) {
		char[] data = keyText.toCharArray();
		int n = data.length;
		if ((n & 0x01) != 0) {
			throw new IllegalArgumentException("The input was odd - sized. Input was: " + keyText);
		}

		if (!checkValidHexInput(keyText)) {
			throw new IllegalArgumentException("The input had invalid characters. Input was: " + keyText);
		}

		byte[] result = new byte[n / 2];

		for (int i = 0; i < n; i += 2) {
			int digit = Integer.parseInt(keyText.substring(i, i + 2), 16);
			result[i / 2] = (byte) digit;
		}

		return result;
	}

	/**
	 * Converts an array of bytes into a String representing the hexadecimal
	 * value of each byte in the array. For each byte in the given array, two
	 * characters are created in the string, in big - endian notation.
	 * 
	 * @param byteArray
	 *            byte array to convert to hex
	 * @return hexadecimal representation (as a string) of the byte array
	 */
	public static String byteToHex(byte[] byteArray) {
		char[] hex = new char[byteArray.length * 2];

		for (int i = 0, n = byteArray.length; i < n; i++) {
			hex[i * 2] = hexAlphabet[(byteArray[i] & 0xF0) >>> 4];
			hex[i * 2 + 1] = hexAlphabet[byteArray[i] & 0x0F];
		}
		return new String(hex);
	}

	/**
	 * Checks if the input is consisted only of valid hexadecimal characters -
	 * digits <code>0-9</code> and letters <code>a-f</code>. Both upper and
	 * lower case letters are allowed.
	 * 
	 * @param input
	 *            given input to be checked
	 * @return <code>true</code> if the input was valid, <code>false</code>
	 *         otherwise.
	 */
	private static boolean checkValidHexInput(String input) {
		char data[] = input.toLowerCase().toCharArray();
		String hexChar = new String(hexAlphabet);

		for (int i = 0, n = data.length; i < n; i++) {
			if (hexChar.indexOf(data[i]) == -1) {
				return false;
			}
		}
		return true;
	}

}
