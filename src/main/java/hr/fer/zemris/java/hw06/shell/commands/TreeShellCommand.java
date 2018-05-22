package hr.fer.zemris.java.hw06.shell.commands;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Prints a directory tree.
 * <p>
 * The command expects a single argument - a valid directory name. Each
 * directory level shifts output two characters to the right.
 * 
 * @author Mirna Baksa
 *
 */
public class TreeShellCommand implements ShellCommand {
	/** Command name. **/
	private final String commandName = "tree";

	/** Command description. **/
	private List<String> description = new ArrayList<>(
			Arrays.asList("Prints the directory tree.", "tree [path]", "Path must be valid directory name/path."));

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Path dir;
		try{
			dir = Paths.get(arguments);
		}catch(InvalidPathException ex){
			env.writeln("Invalid directory path.");
			return ShellStatus.CONTINUE;
		}
		
		if (!dir.toFile().isDirectory()) {
			env.writeln("Invalid directory path.");
			return ShellStatus.CONTINUE;
		}

		TreeVisitor visitor = new TreeVisitor(env);
		try {
			Files.walkFileTree(dir, visitor);
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

	/**
	 * Implements a file visitor to print out the tree of a directory.
	 * @author Mirna Baksa
	 *
	 */
	private class TreeVisitor implements FileVisitor<Path> {
		/** Enables communication between the command and the user **/
		private Environment env;
		/** Current directory level. **/
		private int level;

		/**
		 * Constructs a new visitor.
		 * 
		 * @param env
		 *            enables communication between the command and the user.
		 */
		public TreeVisitor(Environment env) {
			this.env = env;
			this.level = 1;
		}

		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
			env.writeln(String.format("%" + (level * 2) + "s", dir.getFileName().toString()));
			level++;
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
			level--;
			return FileVisitResult.CONTINUE;
		}

	}

}
