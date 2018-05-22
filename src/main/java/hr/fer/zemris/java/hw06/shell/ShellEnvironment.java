package hr.fer.zemris.java.hw06.shell;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;

import hr.fer.zemris.java.hw06.shell.commands.CatShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.CharsetsShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.CopyShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.ExitShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.HelpShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.HexdumpShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.LsShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.MkdirShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.SymbolShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.TreeShellCommand;

/**
 * This class implements the communication process between the {@link MyShell} and the
 * user. All the communication is achieved through methods of this class.
 * 
 * @author Mirna Baksa
 *
 */
public class ShellEnvironment implements Environment {
	/** Stream from where the input is read. **/
	private BufferedReader inputStream;
	/** Stream from where the output is written. **/
	private BufferedWriter outputStream;
	/** Multi line symbol. **/
	private Character multiLineSymbol;
	/** More lines symbol **/
	private Character moreLinesSymbol;
	/** Prompt symbol. **/
	private Character promptSymbol;
	/** A map of supported commands. **/
	private SortedMap<String, ShellCommand> commands;

	/**
	 * Constructs a new {@link ShellEnvironment}. Sets the default values for
	 * the prompt, multi line and more lines symbols.
	 */
	public ShellEnvironment() {
		this.inputStream = new BufferedReader(new InputStreamReader(System.in));
		this.outputStream = new BufferedWriter(new OutputStreamWriter(System.out));
		multiLineSymbol = '|';
		moreLinesSymbol = '\\';
		promptSymbol = '>';

		initCommands();
	}

	/**
	 * Initializes the list of currently supported {@link MyShell} commands.
	 */
	private void initCommands() {
		commands = new TreeMap<String, ShellCommand>();
		commands.put("cat", new CatShellCommand());
		commands.put("charsets", new CharsetsShellCommand());
		commands.put("copy", new CopyShellCommand());
		commands.put("help", new HelpShellCommand());
		commands.put("hexdump", new HexdumpShellCommand());
		commands.put("ls", new LsShellCommand());
		commands.put("mkdir", new MkdirShellCommand());
		commands.put("tree", new TreeShellCommand());
		commands.put("exit", new ExitShellCommand());
		commands.put("symbol", new SymbolShellCommand());

	}

	@Override
	public String readLine() throws ShellIOException {
		String line;
		try {
			line = inputStream.readLine();
		} catch (IOException ex) {
			throw new ShellIOException("An I/O error occured.");
		}

		return line;
	}

	@Override
	public void write(String text) throws ShellIOException {
		try {
			outputStream.write(text);
			outputStream.flush();
		} catch (IOException ex) {
			throw new ShellIOException("An I/O error occured.");
		}

	}

	@Override
	public void writeln(String text) throws ShellIOException {
		try {
			outputStream.write(text + "\n");
			outputStream.flush();
		} catch (IOException ex) {
			throw new ShellIOException("An I/O error occured.");
		}
	}

	@Override
	public SortedMap<String, ShellCommand> commands() {
		return Collections.unmodifiableSortedMap(commands);
	}

	@Override
	public Character getMultilineSymbol() {
		return multiLineSymbol;
	}

	@Override
	public void setMultilineSymbol(Character symbol) {
		writeln("Symbol for MULTILINE changed from '" + multiLineSymbol + "' to '" + symbol + "'.");
		this.multiLineSymbol = symbol;

	}

	@Override
	public Character getPromptSymbol() {
		return promptSymbol;
	}

	@Override
	public void setPromptSymbol(Character symbol) {
		writeln("Symbol for PROMPT changed from '" + promptSymbol + "' to '" + symbol + "'.");
		this.promptSymbol = symbol;

	}

	@Override
	public Character getMorelinesSymbol() {
		return moreLinesSymbol;
	}

	@Override
	public void setMorelinesSymbol(Character symbol) {
		writeln("Symbol for MORELINES changed from '" + moreLinesSymbol + "' to '" + symbol + "'.");
		this.moreLinesSymbol = symbol;

	}

}
