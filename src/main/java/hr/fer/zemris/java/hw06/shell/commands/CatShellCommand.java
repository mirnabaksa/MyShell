package hr.fer.zemris.java.hw06.shell.commands;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
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
 * Writes the content of a file to the console.
 * <p>
 * The command takes one or two arguments. The first argument is path to a
 * file and is mandatory. The second argument is charset name that should be
 * used to interpret chars from bytes. If not provided, a default platform
 * charset should be used. This command opens the given file and writes its content
 * to console.
 * 
 * @author Mirna Baksa
 *
 */
public class CatShellCommand implements ShellCommand {
	/** Command name. **/
	private final String commandName = "cat";

	/** Command description. **/
	private final List<String> description = new ArrayList<>(Arrays.asList(
			"Opens the given file and writes its content to console.", "cat [path]", "cat [path] [charset]",
			"Path must be a valid file path.",
			"Defined charset is used to interpret chars from bytes. If not provided, a default platform charset is used."));

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if (arguments == null) {
			env.writeln("At least one argument is expected when using the cat command.");
			env.writeln("See help cat for more info.");
			return ShellStatus.CONTINUE;
		}

		String[] argList = arguments.split("\\s+");

		Charset charset = Charset.defaultCharset();
		if (argList.length == 2) {
			try {
				charset = Charset.forName(argList[1]);
			} catch (IllegalCharsetNameException ex) {
				env.writeln("The given charset name " + argList[1] + "is not valid.");
				return ShellStatus.CONTINUE;
			} catch (UnsupportedCharsetException ex2) {
				env.writeln("The given charset name " + argList[1] + "is not supported.");
				return ShellStatus.CONTINUE;
			}
		}

		Path path;
		try{
			path = Paths.get(arguments);
		}catch(InvalidPathException ex){
			env.writeln("Invalid path.");
			return ShellStatus.CONTINUE;
		}
		
		if(!path.toFile().isFile()){
			env.writeln("The given path must be a file.");
			return ShellStatus.CONTINUE;
		}

		try (InputStream inputStream = Files.newInputStream(path, StandardOpenOption.READ);) {
			byte[] buffer = new byte[4096];

			while (true) {
				int readBytes = inputStream.read(buffer);
				if (readBytes < 1) {
					break;
				}

				env.write(new String(buffer, 0, readBytes, charset));
			}
		} catch (IOException e) {
			env.writeln("An I/O error occured.");
			return ShellStatus.CONTINUE;
		}
		env.writeln("");
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
