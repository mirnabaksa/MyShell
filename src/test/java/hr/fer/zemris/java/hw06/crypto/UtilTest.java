package hr.fer.zemris.java.hw06.crypto;

import static org.junit.Assert.*;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class UtilTest {

	@Test(expected=IllegalArgumentException.class)
	public void testhexToByte1() {
		String hex = "abcdefg";
		Util.hexToByte(hex);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testhexToByte2() {
		String hex = "ae2";
		Util.hexToByte(hex);
	}
	
	@Test
	public void testhexToByte3() {
		String hex = "01ae22";
		byte[] result = Util.hexToByte(hex);
		assertEquals(3, result.length);
		assertEquals(1, result[0]);
		assertEquals(-82, result[1]);
		assertEquals(34, result[2]);
	}
	
	@Test
	public void testhexToByte4() {
		String hex = "3F2504E0";
		byte[] result = Util.hexToByte(hex);
		assertEquals(4, result.length);
		assertEquals(63, result[0]);
		assertEquals(37, result[1]);
		assertEquals(4, result[2]);
		assertEquals(-32, result[3]);
	}
	
	@Test
	public void testhexToByte5() {
		String hex = "";
		byte[] start = Util.hexToByte(hex);
		assertEquals(true, start.length == 0);
	}
	
	@Test
	public void testbyteToHex() {
		byte[] start = new byte[]{1, -82, 34};
		String hex = Util.byteToHex(start);
		assertEquals(true, hex.equals("01ae22"));
	}

	@Test
	public void testbyteToHex1() {
		byte[] start = new byte[]{63, 37, 4, -32};
		String hex = Util.byteToHex(start);
		assertEquals(true, hex.equals("3f2504e0"));
	}
	
	@Test
	public void testbyteToHex2() {
		byte[] start = new byte[]{};
		String hex = Util.byteToHex(start);
		assertEquals(true, hex.equals(""));
	}
	
}
