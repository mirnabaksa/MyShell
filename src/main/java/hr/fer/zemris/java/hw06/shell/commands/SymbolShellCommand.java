package hr.fer.zemris.java.hw06.shell.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Enables the retrieval of current shell symbols (prompt, multiple line, more
 * line) and changing the symbols to a given symbol.
 * <p>
 * The command receives one or two arguments. Provided with only one argument,
 * prints out the current character for that name. Provided with two arguments,
 * changes the current character of that name to the given new char.
 * 
 * @author Mirna Baksa
 *
 */
public class SymbolShellCommand implements ShellCommand {
	/** Command name. **/
	private final String commandName = "symbol";

	/** Command description. **/
	private List<String> description = new ArrayList<>(
			Arrays.asList("Manipulates the shell symbols.", "symbol [NAME]", "symbol [NAME] [newchar]",
					"Provided with only one argument, prints out the current character for that name. The argument must be upper-case.",
					"Provided with two arguments, changes the current character of that name to the given new char."));

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if (arguments == null) {
			env.writeln("The symbol command must receive at least one argument.");
			return ShellStatus.CONTINUE;
		}
		String parts[] = arguments.split("\\s+");
		if (parts.length == 1) {
			switch (parts[0].toUpperCase()) {
			case "PROMPT": {
				env.writeln("Symbol for PROMPT is : '" + env.getPromptSymbol().toString() + "'.");
				break;
			}
			case "MULTILINE": {
				env.writeln("Symbol for MULTILINE is : '" + env.getMultilineSymbol().toString() + "'.");
				break;
			}
			case "MORELINE": {
				env.writeln("Symbol for MORELINE is : '" + env.getMorelinesSymbol().toString() + "'.");
				break;
			}
			default: {
				env.writeln("Invalid symbol command input. Was: " + parts[0]);
			}
			}
		} else if (parts.length == 2) {
			switch (parts[0].toUpperCase()) {
			case "PROMPT": {
				env.setPromptSymbol(parts[1].charAt(0));
				break;
			}
			case "MULTILINE": {
				env.setMultilineSymbol(parts[1].charAt(0));
				break;
			}
			case "MORELINE": {
				env.setMorelinesSymbol(parts[1].charAt(0));
				break;
			}
			default: {
				env.writeln("Invalid symbol command input.");
				break;
			}
			}
		} else {
			env.writeln("Invalid number of arguments. Arguments were: " + arguments);
			env.writeln("See help " + commandName + " for more info.");
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
