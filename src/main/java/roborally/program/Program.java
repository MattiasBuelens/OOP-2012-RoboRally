package roborally.program;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.text.ParseException;

import be.kuleuven.cs.som.annotate.Basic;

import roborally.program.command.Command;

/**
 * A program which can be executed by a robot in a game of RoboRally.
 * 
 * @author	Mattias Buelens
 * @author	Thomas Goossens
 * @version	3.0
 * 
 * @note	This class is part of the 2012 project for
 * 			the course Object Oriented Programming in
 * 			the second phase of the Bachelor of Engineering
 * 			at KU Leuven, Belgium.
 */
public class Program {

	/**
	 * Create a new program.
	 * 
	 * @param command
	 * 			The main command for this new program.
	 * 
	 * @post	The new program's main command is set
	 * 			to the given command.
	 * 			| new.getCommand() == command
	 * 
	 * @throws	IllegalArgumentException
	 * 			If the given command is not effective
	 * 			or is not properly constructed.
	 * 			| command == null || !command.isConstructed()
	 */
	public Program(Command command) {
		if (command == null || !command.isConstructed())
			throw new IllegalArgumentException("Command must be effective and properly constructed.");
		this.command = command;
	}

	/**
	 * Get the main command of this program.
	 */
	@Basic
	public Command getCommand() {
		return command;
	}

	/**
	 * Variable representing the main command of this program.
	 * 
	 * @invar	The command is effective and properly constructed.
	 * 			| command != null && command.isConstructed()
	 */
	private final Command command;

	/**
	 * Get the source representation of this program.
	 *
	 * @return	The source representation of the main command
	 * 			of this program is returned.
	 * 			| result.equals(getCommand().toSource())
	 */
	public String toSource() {
		return getCommand().toSource();
	}

	/**
	 * Save this program to a file at the given file path.
	 * 
	 * @param path
	 * 			The file path of the file to save to.
	 * 
	 * @effect	The program is saved to a file at
	 * 			the given file path.
	 * 			| save(new File(path))
	 * 
	 * @throws	IllegalArgumentException
	 * 			If the given file path is not effective.
	 * 			| path == null
	 * @throws	IOException
	 * 			If the program could not be written to the file.
	 */
	public void save(String path) throws IllegalArgumentException, IOException {
		if (path == null)
			throw new IllegalArgumentException("Path must be effective.");

		save(new File(path));
	}

	/**
	 * Save this program to the given file.
	 * 
	 * @param file
	 * 			The file to save to.
	 * 
	 * @throws	IllegalArgumentException
	 * 			If the given file is not effective.
	 * 			| file == null
	 */
	public void save(File file) throws IllegalArgumentException, IOException {
		if (file == null)
			throw new IllegalArgumentException("File must be effective.");

		// Create file if it doesn't exist yet
		file.createNewFile();

		// Write program source to file
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(toSource());
		} finally {
			if (writer != null)
				writer.close();
		}
	}

	/**
	 * Load the contents of the program file at the given file path
	 * and parse them into a program.
	 * 
	 * @param path
	 * 			The file path of the program file.
	 * 
	 * @effect	The file associated with the given file path
	 * 			is loaded and parsed into a program.
	 * 			| load(new File(path))
	 * 
	 * @throws	IllegalArgumentException
	 * 			If the given path is not effective.
	 * 			| path == null
	 */
	public static Program load(String path) throws IllegalArgumentException, IOException, ParseException {
		if (path == null)
			throw new IllegalArgumentException("Path must be effective.");

		return load(new File(path));
	}

	/**
	 * Load the contents of the given program file
	 * and parse them into a program.
	 * 
	 * @param file
	 * 			The program file.
	 * 
	 * @return	The loaded program.
	 * 
	 * @throws	IllegalArgumentException
	 * 			If the given file is not effective.
	 * 			| file == null
	 * @throws	IOException
	 * 			If the given file could not be read.
	 * @throws	ParseException
	 * 			If the file contents of the given file
	 * 			could not be parsed into a program.
	 */
	public static Program load(File file) throws IllegalArgumentException, IOException, ParseException {
		if (file == null)
			throw new IllegalArgumentException("File must be effective.");

		// Read the entire file contents into a buffer
		CharBuffer buffer = readFile(file);

		// Parse the file contents
		Parser parser = new Parser(buffer);
		Command command = parser.parse();

		return new Program(command);
	}

	/**
	 * Read the entire file contents of the given file.
	 * 
	 * @param file
	 * 			The file to read.
	 * 
	 * @return	A character buffer containing the file contents.
	 * 
	 * @throws	IOException
	 * 			If the given file could not be read.
	 */
	private static CharBuffer readFile(File file) throws IOException {
		FileChannel channel = null;
		try {
			channel = new FileInputStream(file).getChannel();
			ByteBuffer byteBuffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
			CharBuffer charBuffer = Charset.defaultCharset().newDecoder().decode(byteBuffer);
			return charBuffer;
		} finally {
			channel.close();
		}
	}
}
