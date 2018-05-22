package hr.fer.zemris.java.hw06.shell.commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Displays a directory listing (not recursive.)
 * <p>
 * The command receives a single argument - a directory. The output is formatted
 * into 4 columns:
 * <p>
 * Column 1: (d) if object is directory, (r) is readable, (w) is writable, (e)
 * is executable. <br>
 * Column 2: size in bytes. <br>
 * Column 3: file creation date/time. <br>
 * Column 4: file name.
 * 
 * @author Mirna Baksa
 *
 */
public class LsShellCommand implements ShellCommand {
	/** Command name. **/
	private final String commandName = "ls";

	/** Command description. **/
	private List<String> description = new ArrayList<>(Arrays.asList("Displays a directory listing. (not recursively).",
			"ls [path]", "The output will be shown in 4 columns.",
			"Column 1: (d) if object is directory, (r) is readable, (w) is writable, (e) is executable.",
			"Column 2: size in bytes.", "Column 3: file creation date/time.", "Column 4: file name."));

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if (arguments == null) {
			env.writeln("The ls command expects exactly one argument - directory name.");
			env.writeln("See help ls for more info.");
			return ShellStatus.CONTINUE;
		}
		
		Path path;
		try{
			path = Paths.get(arguments);
		}catch(InvalidPathException ex){
			env.writeln("Invalid path.");
			return ShellStatus.CONTINUE;
		}
		
		if (!path.toFile().isDirectory()) {
			env.writeln("Invalid argument input. Expected directory path. Was: " + arguments);
			return ShellStatus.CONTINUE;
		}

		LSVisitor visitor = new LSVisitor(env, path);
		try {
			Files.walkFileTree(path, visitor);
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
	 * Implements a file visitor to execute the {@link LsShellCommand}.
	 * 
	 * @author Mirna Baksa
	 *
	 */
	private class LSVisitor implements FileVisitor<Path> {
		/**
		 * Environment to enable communication between the command and the user
		 **/
		private Environment env;
		/** Starting path **/
		private Path start;

		/**
		 * Default constructor.
		 * 
		 * @param env
		 *            environment to enable communication between the command
		 *            and the user
		 * @param start
		 *            starting path
		 */
		public LSVisitor(Environment env, Path start) {
			this.env = env;
			this.start = start;
		}

		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
			if (start.equals(dir))
				return FileVisitResult.CONTINUE;
			try {
				buildPrintInfo(dir, attrs);
			} catch (IOException e) {
				env.writeln("An error occured while retreving file info.");
				return FileVisitResult.TERMINATE;
			}

			return FileVisitResult.SKIP_SUBTREE;
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			try {
				buildPrintInfo(file, attrs);
			} catch (IOException e) {
				env.writeln("An error occured while retreving file info.");
				return FileVisitResult.TERMINATE;
			}

			return FileVisitResult.CONTINUE;

		}

		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
			return FileVisitResult.CONTINUE;
		}

		/**
		 * Returns the formatted creation date and time of the file/directory
		 * represented by the path given as an argument.
		 * 
		 * @param path
		 *            path to retrieve the date and time for
		 * @return formatted date and time
		 * @throws IOException
		 *             in case of file info retrieval error
		 */
		private String getDateTime(Path path) throws IOException {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			BasicFileAttributeView faView = Files.getFileAttributeView(path, BasicFileAttributeView.class,
					LinkOption.NOFOLLOW_LINKS);
			BasicFileAttributes attributes = faView.readAttributes();
			FileTime fileTime = attributes.creationTime();
			String formattedDateTime = sdf.format(new Date(fileTime.toMillis()));
			return formattedDateTime;
		}

		/**
		 * Gets the attributes of a file with the path given as an argument.
		 * <p>
		 * To the String representation of the file attributes, the method will
		 * append: (d) if object is directory, (r) is readable, (w) is writable,
		 * (e) is executable, or (-) if not.
		 * 
		 * @param p
		 *            path of the file.
		 * @return path attributes
		 */
		private String getAttributes(Path p) {
			StringBuilder sb = new StringBuilder();
			File object = p.toFile();

			sb.append(object.isDirectory() ? "d" : "-");
			sb.append(object.canRead() ? "r" : "-");
			sb.append(object.canWrite() ? "w" : "-");
			sb.append(object.canExecute() ? "x" : "-");

			return sb.toString();
		}

		/**
		 * Prints the file info to the console.
		 * 
		 * @param attrs
		 *            file attributes
		 * @param size
		 *            size of the file/directory
		 * @param formattedDateTime
		 *            date and time of the creation
		 * @param name
		 *            name of the file/directory
		 */
		private void printLine(String attrs, Long size, String formattedDateTime, String name) {
			String output = String.format("%s %10d %s %s", attrs, size, formattedDateTime, name);
			env.writeln(output);
		}

		/**
		 * Builds the print info of the file/directory represented with the path
		 * give as an argument. The print info is consisted of file attributes,
		 * file/directory size, date and time of the creation and file/directory
		 * name.
		 * 
		 * @param file
		 *            file to build the info for
		 * @param attrs
		 *            file attributes
		 * @throws IOException
		 *             in case of file info retrieval error
		 */
		private void buildPrintInfo(Path file, BasicFileAttributes attrs) throws IOException {
			String attributes = getAttributes(file);
			String formattedDateTime = null;
			formattedDateTime = getDateTime(file);
			
			long size = 0;
			if(file.toFile().isDirectory()){
				size = getDirectorySize(file.toFile());
			}else {
				size = attrs.size();
			}
			
			printLine(attributes, size, formattedDateTime, file.getFileName().toString());
		}

		/**
		 * Gets the directory size - the sizes of all files contained in the directory, including the
		 * sizes of the directories in the starting directory.
		 * @param file directory to determine the size of
		 * @return size of the directory
		 */
		private long getDirectorySize(File file) {
			File[] files = file.listFiles();
			if(files == null) return 0;
			
			long size = 0;
			
			for(File f : files){
				
				if(f.isDirectory()){
					size += getDirectorySize(f);
				}else{
					size += f.length();
				}
			}
			
			return size;
		}
	}

}
