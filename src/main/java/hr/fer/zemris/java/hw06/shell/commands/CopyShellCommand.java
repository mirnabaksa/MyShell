package hr.fer.zemris.java.hw06.shell.commands;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Copies a file. 
 * <p>
 * The command expects two arguments: source file name and
 * destination file name. The source must be a file, whereas the destination can
 * either be a file or a directory. If a directory, the command copies the
 * source file to that directory. If the destination is a file that exists, the
 * user will be asked to allow the overwriting of the existing file. If the file
 * does not exist, the file will be created and the content of the source file
 * copied into it. The name of the copied file is the source file name.
 * 
 * @author Mirna Baksa
 *
 */
public class CopyShellCommand implements ShellCommand {
	/** Command name. **/
	private final String commandName = "copy";

	/** Command description. **/
	private List<String> description = new ArrayList<>(Arrays.asList("Copies one or more files to another location.",
			"copy [source] [dest]", "Source and dest must be valid file paths.",
			"If the dest path already exists, the user will be asked if the file should be overwriten.",
			"If the dest path is a directory, the source file will be copied into the directory using the original file name."));

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if (arguments == null) {
			env.writeln("The copy command expects exactly two arguments.");
			env.writeln("See help copy for more info.");
			return ShellStatus.CONTINUE;
		}

		String argList[] = arguments.split("\\s+");
		if (argList.length != 2) {
			env.writeln("The copy command expects exactly two arguments.");
			env.writeln("See help copy for more info.");
			return ShellStatus.CONTINUE;
		}

		Path input;
		Path output;	
		try{
			input =  Paths.get(argList[0]);
			output = Paths.get(argList[1]);
		}catch(InvalidPathException ex){
			env.writeln("Invalid path.");
			return ShellStatus.CONTINUE;
		}

		if (input.toFile().isDirectory()) {
			env.writeln("Invalid argument. First path must be a file path.");
			return ShellStatus.CONTINUE;
		}

		if (output.toFile().exists() && !output.toFile().isDirectory()) {
			env.writeln("Overwrite " + output.toString() + "? Input yes/no.");
			env.writeln(env.getPromptSymbol().toString());
			String userInput = env.readLine();
			if (userInput.toLowerCase().equals("no")) {
				env.writeln("No action was done.");
				return ShellStatus.CONTINUE;
			}
		}

		if (output.toFile().isDirectory()) {
			output = Paths.get(argList[1] + File.separator + input.getFileName().toString());
		}

		try (InputStream inputStream = Files.newInputStream(input, StandardOpenOption.READ);
				OutputStream outputStream = Files.newOutputStream(output);) {
			byte[] buffer = new byte[4096];

			while (true) {
				int readBytes = inputStream.read(buffer);
				if (readBytes < 1) {
					break;
				}

				outputStream.write(buffer, 0, readBytes);
			}
		} catch (IOException e) {
			env.writeln("An I/O error occured.");
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
