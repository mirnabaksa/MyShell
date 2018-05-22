package hr.fer.zemris.java.hw06.shell;

import java.util.List;

/**
 * Implementors of this interface represent a shell command.
 * 
 * @author Mirna Baksa
 *
 */
public interface ShellCommand {

	/**
	 * Executes the command.
	 * 
	 * @param env
	 *            environment for communication between the command and the user
	 * @param arguments
	 *            arguments for the command
	 * @return {@link ShellStatus#TERMINATE} if the shell is to be terminated,
	 *         {@link ShellStatus#CONTINUE} otherwise.
	 */
	public ShellStatus executeCommand(Environment env, String arguments);

	/**
	 * Gets the command name.
	 * 
	 * @return name of the command
	 */
	public String getCommandName();

	/**
	 * Gets the description of the command. The first list element is the
	 * concise description, whereas the elements that follow offer more info
	 * about the command.
	 * 
	 * @return command description
	 */
	public List<String> getCommandDescription();
}
