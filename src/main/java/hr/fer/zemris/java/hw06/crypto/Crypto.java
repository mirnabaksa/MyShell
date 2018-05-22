package hr.fer.zemris.java.hw06.crypto;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * This class allows the user to encrypt/decrypt a file using the AES
 * crypto-algorithm and the 128 - bit encryption key or calculate an check the
 * SHA-256 file digest.
 * <p>
 * A message digest is a fixed-size binary digest which is calculated from
 * arbitrary long data. Digests are used to verify if the data recieved (for
 * example, when downloading the data from the Internet) arrived unchanged. The
 * original document can not be created from the digest.
 * <p>
 * Encryption is the conversion of data into a form, called a ciphertext, that
 * can not be easily understood by unauthorized people. Decryption is the
 * reverse process: it is a transformation of ciphertext back into its original
 * form.
 * 
 * @author Mirna Baksa
 *
 */
public class Crypto {

	/**
	 * Main method of the program. The program receives multiple command line
	 * arguments from the user.
	 * <p>
	 * The programs first receives a keyword - checksha, encrypt or decrypt -
	 * according to the action that needs to be done. If the keyword is
	 * "checksha", one additional argument is expected - the String
	 * representation of the path of the document that needs to be checked. If
	 * the keyword is "encrypt" or "decrypt", two more arguments are needed -
	 * the String representation of the path of the original document and the
	 * String representation of the path of the document that will be created.
	 * 
	 * @param args
	 *            command line arguments
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("Invalid input!");
			return;
		}

		if (args[0].equals("checksha")) {
			if (args.length != 2) {
				System.out.println("Invalid input! Expected the path of the file to check.");
				return;
			}
			checkSHA(args[1]);
		} else if (args[0].equals("encrypt") || args[0].equals("decrypt")) {
			if (args.length != 3) {
				System.out.println("Invalid input! Expected the path of the ciphertext and the recreated file.");
				return;
			}
			crypt(args[1], args[2], args[0]);
		} else {
			System.out.println(
					"Invalid keyword. Only checksha, encrypt and decrypt actions are supported. Input was: " + args[0]);
		}

	}

	/**
	 * Checks if the SHA digest of the file denoted by the String representation
	 * of the path given as an argument is in accordance to the expected digest.
	 * <p>
	 * The expected digest is entered through the console by the user. The
	 * digest is calculated by the SHA-256 algorithm.
	 * <p>
	 * The result of the check is printed out through the standard output.
	 * 
	 * @param fileName
	 *            String representation of the path to be checked
	 */
	private static void checkSHA(String fileName) {
		Scanner sc = new Scanner(System.in);
		String expDigest = getUserInput("Please provide expected sha-256 digest for " + fileName + ": ", sc);
		sc.close();

		MessageDigest sha = null;
		try {
			sha = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Digest generation error.");
		}

		try (InputStream inputStream = Files.newInputStream(Paths.get(fileName), StandardOpenOption.READ)) {
			byte[] buffer = new byte[4096];

			while (true) {
				int readBytes = inputStream.read(buffer);
				if (readBytes < 1) {
					break;
				}

				sha.update(buffer, 0, readBytes);
			}
		} catch (IOException | InvalidPathException ex) {
			System.out.println("An error occured while reading the file.");
			System.exit(0);
		}

		byte[] digest = sha.digest();
		System.out.print("Digesting completed. ");

		String calculatedDigest = null;
		try{
			calculatedDigest = Util.byteToHex(digest);
		}catch(IllegalArgumentException ex){
			System.out.println(ex.getMessage());
			return;
		}
		
		if (calculatedDigest.equals(expDigest)) {
			System.out.println("Digest of " + fileName + " matches expected digest.");
		} else {
			System.out.println(
					"Digest of " + fileName + " does not match the expected digest. Digest was: " + calculatedDigest);
		}

	}

	/**
	 * Encrypts or decrypts the original using the AES crypto-algorithm,
	 * resulting with the file with the path output, according to the mode given
	 * as an argument. The mode can be "encrypt" or "decrypt", according to the
	 * action that needs to be done over the original document.
	 * <p>
	 * Original and output arguments are String representations of the paths to
	 * the documents needed in this method.
	 * 
	 * @param original
	 *            String representation of the path of the original
	 * @param output
	 *            String representation of the path of the output
	 * @param mode
	 *            mode of crypting
	 */
	private static void crypt(String original, String output, String mode) {
		Cipher cipher = initCipher(mode);
		try (InputStream inputStream = Files.newInputStream(Paths.get(original), StandardOpenOption.READ);
				OutputStream outputStream = Files.newOutputStream(Paths.get(output))) {
			byte[] inputBuffer = new byte[4096];

			while (true) {
				int readBytes = inputStream.read(inputBuffer);
				if (readBytes < 1) {
					break;
				}

				byte[] outputBuffer = new byte[4096];
				int updatedBytes = cipher.update(inputBuffer, 0, readBytes, outputBuffer);
				outputStream.write(outputBuffer, 0, updatedBytes);
			}

			byte[] outputBuffer = new byte[4096];
			int updatedBytes = cipher.doFinal(outputBuffer, 0);
			outputStream.write(outputBuffer, 0, updatedBytes);

		} catch (IOException | IllegalBlockSizeException | ShortBufferException | BadPaddingException | InvalidPathException e ) {
			System.out.println("An error occured while crypting the file.");
			return;
		}

		if (mode.equals("encrypt"))
			System.out.print("Encryption ");
		else
			System.out.print("Decryption ");
		System.out.println("completed. Generated file " + output + " based on file " + original + " .");
	}

	/**
	 * Gets the input from the console. Prints out the message with detailed
	 * information about the input needed.
	 * 
	 * @param message
	 *            message about the input needed
	 * @param sc
	 *            scanner from where the input is received
	 * @return user input
	 */
	private static String getUserInput(String message, Scanner sc) {
		System.out.println(message);
		String input = sc.next();
		return input;

	}

	/**
	 * Initializes the cipher. The method retrieves the password and
	 * initialization vector from the user and acts accordingly. The argument
	 * gives information about the mode of the cipher - "encrypt" marks the
	 * {@link Cipher#ENCRYPT_MODE}, and "decrypt" marks the
	 * {@link Cipher#DECRYPT_MODE}.
	 * 
	 * @param mode
	 *            mode of the cipher
	 * @return initialized cipher
	 */
	private static Cipher initCipher(String mode) {
		Scanner sc = new Scanner(System.in);
		String password = getUserInput("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits): ",
				sc);
		String initVector = getUserInput("Please provide initialization vector as hex-encoded text (32 hex-digits): ",
				sc);
		sc.close();

		SecretKeySpec keySpec = null;
		AlgorithmParameterSpec paramSpec = null;
		try{
			 keySpec = new SecretKeySpec(Util.hexToByte(password), "AES");
			 paramSpec = new IvParameterSpec(Util.hexToByte(initVector));
		}catch(IllegalArgumentException ex){
			System.out.println(ex.getMessage());
			System.exit(0);
		}
		
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException ex) {
			System.out.println("Error occured while getting cipher instance.");
		}
		try {
			cipher.init(mode.equals("encrypt") ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, keySpec, paramSpec);
		} catch (InvalidKeyException | InvalidAlgorithmParameterException ex2) {
			System.out.println("Cipher initialization error occured.");
		}

		return cipher;
	}

}
