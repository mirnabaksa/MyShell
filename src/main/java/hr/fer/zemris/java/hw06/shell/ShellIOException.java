package hr.fer.zemris.java.hw06.shell;
/**
* Thrown to indicate an error that occured while working with shell.
* 
* @author Mirna Baksa
*/
public class ShellIOException extends RuntimeException {
	/**
	 * Default serialization version.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Construct a ShellIOException with no message.
	 */
	public ShellIOException() {
		super();
	}

	/**
	 * Constructs a ShellIOException with a message.
	 * 
	 * @param message
	 *            the detail message
	 */
	public ShellIOException(String message) {
		super(message);
	}
}
