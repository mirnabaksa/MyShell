package hr.fer.zemris.java.hw06.shell;

import hr.fer.zemris.java.hw06.shell.commands.*;

/**
 * Implements a shell. A shell is a command language interpreter that takes your
 * commands from the user and performs the operation defined by the command.
 * <p>
 * This shell offers 10 different commands: cat, charsets, copy, exit, help,
 * hexdump, ls, mkdir, symbol and tree. For detailed info about each command,
 * see documentation of each command (listed below).
 * <p>
 * A command can be one-lined or can stretch through multiple lines - in case of
 * multiple line input, each line must end with a multi line character defined
 * by the shell. Quotes are allowed in the arguments representing paths.
 * 
 * @see CatShellCommand
 * @see CharsetsShellCommand
 * @see CopyShellCommand
 * @see ExitShellCommand
 * @see HelpShellCommand
 * @see HexdumpShellCommand
 * @see LsShellCommand
 * @see MkdirShellCommand
 * @see SymbolShellCommand
 * @see TreeShellCommand
 * 
 * @author Mirna Baksa
 *
 */
public class MyShell {
	/**
	 * Main method from which the program execution starts.
	 * <p>
	 * The method receives input from the console. For detailed info about the
	 * input call the "help" command. The shell is terminated by the "exit"
	 * command.
	 * 
	 * @param args
	 *            command line arguments - not used
	 */
	public static void main(String[] args) {
		Environment env = new ShellEnvironment();
		env.writeln("Welcome to MyShell v 1.0");

		ShellStatus status = null;
		do {
			env.write(env.getPromptSymbol().toString());
			String input = getCommandArguments(env);
			String inputParts[] = input.split("\\s+", 2);
			ShellCommand command = env.commands().get(inputParts[0]);
			if (command == null) {
				env.writeln("Invalid command name.");
				status = ShellStatus.CONTINUE;
				continue;
			}

			try {
				status = command.executeCommand(env,
						inputParts.length == 1 ? null : inputParts[1].replaceAll("\"", ""));
			} catch (ShellIOException ex ) {
				status = ShellStatus.TERMINATE;
			} 
		} while (!status.equals(ShellStatus.TERMINATE));

	}

	/**
	 * Gets the command arguments entered by the user.
	 * <p>
	 * The input can be one-lined or can stretch through multiple lines - in
	 * case of multiple line input, each line must end with a multi line
	 * character defined in the shell.
	 * 
	 * @param env
	 *            environment for communicating with the user
	 * @return String representation of all the arguments the user entered
	 */
	private static String getCommandArguments(Environment env) {
		StringBuilder sb = new StringBuilder();
		while (true) {
			String line = env.readLine();

			if (line.endsWith(env.getMorelinesSymbol().toString())) {
				sb.append(line.substring(0, line.length() - 1) + " ");
				env.write(env.getMultilineSymbol().toString() + "");
				continue;
			}

			sb.append(line);
			break;
		}

		return sb.toString();
	}

}
