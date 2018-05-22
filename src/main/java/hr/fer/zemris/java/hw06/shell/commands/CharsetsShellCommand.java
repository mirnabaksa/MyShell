package hr.fer.zemris.java.hw06.shell.commands;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellIOException;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Lists names of all supported charsets for the curren Java platform.
 * <p>
 * No arguments are taken. A single charset name is written per line.
 * 
 * @author Mirna Baksa
 *
 */
public class CharsetsShellCommand implements ShellCommand {
	/** Command name. **/
	private final String commandName = "charsets";

	/** Command description. **/
	private List<String> description = new ArrayList<>(
			Arrays.asList("Lists names of supported charsets for users Java platform.", "charsets []",
					"No arguments are taken. A single charset is written per line."));

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		SortedMap<String, Charset> charsets = Charset.availableCharsets();
		try {
			charsets.forEach((k, v) -> env.writeln(k));
		} catch (ShellIOException ex) {
			return ShellStatus.TERMINATE;
		}

		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return commandName;
	}

	@Override
	public List<String> getCommandDescription() {
		return Collections.unmodifiableList(description);
	}

}
