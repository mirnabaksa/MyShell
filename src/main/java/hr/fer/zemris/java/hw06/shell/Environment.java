package hr.fer.zemris.java.hw06.shell;

import java.util.SortedMap;

/**
 * Implementors of this interface offer communication between the user and the
 * {@link MyShell}. The shell reads user input and writes response exclusively
 * through this interface.
 * 
 * @author Mirna Baksa
 */
public interface Environment {
	/**
	 * Reads the next line from the console.
	 * 
	 * @return read line
	 * @throws ShellIOException
	 *             in case of an I/O error
	 */
	public String readLine() throws ShellIOException;

	/**
	 * Writes the text to the console.
	 * 
	 * @param text
	 *            text to be written
	 * @throws ShellIOException
	 *             in case of an I/O error
	 */
	public void write(String text) throws ShellIOException;

	/**
	 * Writes the text to the console, ending with a new line character.
	 * 
	 * @param text
	 *            text to be written
	 * @throws ShellIOException
	 *             in case of an I/O error
	 */
	public void writeln(String text) throws ShellIOException;

	/**
	 * Returns an unmodifiable map of supported commands.
	 * 
	 * @return map of commands
	 */
	public SortedMap<String, ShellCommand> commands();

	/**
	 * Returns the multi line symbol for the shell.
	 * <p>
	 * The multi line symbol is printed out at the beggining of each line when
	 * the input is stretched through multiple lines.
	 * 
	 * @return multi line symbol
	 */
	public Character getMultilineSymbol();

	/**
	 * Sets the multi line symbol for the shell to the character given as an
	 * argument.
	 * <p>
	 * The multi line symbol is printed out at the beggining of each line when
	 * the input is stretched through multiple lines.
	 * @param symbol symbol to set the multi line symbol to
	 */
	public void setMultilineSymbol(Character symbol);

	/**
	 * Returns the current prompt symbol.
	 * 
	 * @return prompt symbol
	 */
	public Character getPromptSymbol();

	/**
	 * Sets the prompt symbol to the character given as an argument.
	 * @param symbol symbol to set the prompt symbol to
	 */
	public void setPromptSymbol(Character symbol);

	/**
	 * Returns the more lines symbol.
	 * <p>
	 * Each line that is not the last line of a command must end with this
	 * symbol to inform the shell that more lines are expected.
	 * 
	 * @return more lines symbol
	 */
	public Character getMorelinesSymbol();

	/**
	 * Sets the more line symbol to the character given as an argument.
	 * <p>
	 * Each line that is not the last line of a command must end with this
	 * symbol to inform the shell that more lines are expected.
	 * @param symbol symbol to set the more line symbol to
	 */
	public void setMorelinesSymbol(Character symbol);
}
