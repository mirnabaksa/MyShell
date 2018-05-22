package hr.fer.zemris.java.hw06.shell.commands;

import java.io.IOException;
import java.io.InputStream;
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
 * Produces hexadecimal view of data read from a file.
 * <p>
 * The command expects a single argument: a valid file name. 
 * @author Mirna Baksa
 *
 */
public class HexdumpShellCommand  implements ShellCommand {
	/** Command name. **/
	private final String commandName = "hexdump";

	/** Command description. **/
	private List<String> description = new ArrayList<>(
			Arrays.asList("Produces hex-output of the file.",
					"hexdump [path]",
					"Path must be valid file path."));
	

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Path path;
		try{
			path = Paths.get(arguments);
		}catch(InvalidPathException ex){
			env.writeln("Invalid path.");
			return ShellStatus.CONTINUE;
		}
		
		if(!path.toFile().isFile()){
			env.writeln("The file path is invalid.");
			return ShellStatus.CONTINUE;
		}
		
		try (InputStream inputStream = Files.newInputStream(path, StandardOpenOption.READ); ) {
			byte[] buffer = new byte[16];
			
			while (true) {
				int readByte = inputStream.read(buffer);
				if (readByte == -1) {
					break;
				}
				
				for(int i = 0; i < 16; i++){
					if(i == 7) {
						env.write("| ");
					}
					
					if(i >= readByte){
						env.write(String.format("%2s ", " "));
					}else{
						env.write(String.format("%02X ", buffer[i]));
					}
				}
				
				env.write("| ");
				
				for(int i = 0; i < readByte; i++){
					if(buffer[i] < 32 || buffer[i] > 127){
						env.write(". ");
					}else{
						env.write(String.format("%c ", buffer[i]));
					}
				}
				env.write("\n");
				
			}
		}catch(IOException e){
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
