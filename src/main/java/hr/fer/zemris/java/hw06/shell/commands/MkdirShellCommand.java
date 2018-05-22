package hr.fer.zemris.java.hw06.shell.commands;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Creates a directory.
 * <p>
 * Recieves a single argument - valid directory name, and creates the
 * appropriate directory structure.
 * 
 * @author Mirna Baksa
 *
 */
public class MkdirShellCommand implements ShellCommand {
	/** Command name **/
	private final String commandName = "mkdir";

	/** Command description. **/
	private List<String> description = new ArrayList<>(Arrays.asList("Creates a directory.", "mkdir [path]",
			"The directory name must be a valid directory path."));

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Path path;
		try{
			path = Paths.get(arguments);
		}catch(InvalidPathException ex){
			env.writeln("Invalid directory path.");
			return ShellStatus.CONTINUE;
		}
		
		try {
			Files.createDirectories(path);
		} catch (Exception ex) {
			env.writeln("There was an error in creating the directory: " + arguments);
			return ShellStatus.CONTINUE;
		}
		env.writeln("Directory " + arguments + " was created.");
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
