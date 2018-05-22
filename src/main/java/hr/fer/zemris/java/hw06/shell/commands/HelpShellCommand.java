package hr.fer.zemris.java.hw06.shell.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Help is used to access the information about the shell.
 * <p>
 * If started with no arguments, the command lists names of all supported
 * commands and their short description. If started with single argument, prints
 * the name and the description of selected command. If the command does not
 * exist, an error message will be printed.
 * 
 * @author Mirna Baksa
 *
 */
public class HelpShellCommand implements ShellCommand {
	/** Command name. **/
	private final String commandName = "help";

	/** Command description. **/
	private List<String> description = new ArrayList<>(Arrays.asList("Provides help information for commands.",
			"help [] :lists all the commands and their short description.",
			"help [command] :provides information about the specific command."));

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		SortedMap<String, ShellCommand> commands = env.commands();
		if (arguments == null) {
			commands.forEach((k, v) -> env.writeln(k + "  " + v.getCommandDescription().get(0)));
		} else {
			ShellCommand command = commands.get(arguments);
			if(command == null){
				env.writeln("Invalid input.");
				return ShellStatus.CONTINUE;
			}
			List<String> commandInfo = command.getCommandDescription();
			for (String info : commandInfo) {
				env.writeln(info);
			}
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
