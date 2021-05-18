package miniTerminal;

/* *
 * 
 * This class contains the definition of the terminal and the main
 * method that makes everything work.
 * It also contains variables and functions that make everything easier.
 * 
 * 
 * MiniTerminal Java 
 * by @alejandrofan2 | @masterprog-cmd 
 * 
 * */

import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import org.jline.builtins.Commands;
import org.jline.builtins.Completers;
import org.jline.reader.Completer;
import org.jline.reader.History;
import org.jline.reader.LineReader;
import org.jline.reader.LineReader.Option;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.MaskingCallback;
import org.jline.reader.ParsedLine;
import org.jline.reader.Parser;
import org.jline.reader.impl.DefaultParser;
import org.jline.reader.impl.completer.AggregateCompleter;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.reader.impl.history.DefaultHistory;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.jline.utils.InfoCmp.Capability;
import org.jline.utils.Log;
import org.jline.widget.AutosuggestionWidgets;

import managers.FileManager;
import managers.WebManager;

public class MiniTerminal {

	// General variables of the application.
	private static boolean isRunning = true;
	protected static String systemName = System.getProperty("os.name");
	protected static String user = System.getProperty("user.name");
	protected static File wd = new File(System.getProperty("user.home"));
	private static Collection<String> commands = new ArrayList<String>();
	private static History history = new DefaultHistory();

	// Strings containing the prefix of the messages that are printed.
	public static final String PREFIX = "[" + Colorize.ANSI_WHITE + "Mini" + Colorize.ANSI_BRIGHT_RED + "Terminal"
			+ Colorize.ANSI_RESET + "] ";
	public static final String ERRPREFIX = Colorize.ANSI_RED + "[!] " + Colorize.ANSI_RESET + "[" + Colorize.ANSI_WHITE
			+ "Mini" + Colorize.ANSI_BRIGHT_RED + "Terminal" + Colorize.ANSI_RESET + "] " + Colorize.ANSI_RED;
	public static final String HELPPREFIX = Colorize.ANSI_BRIGHT_YELLOW + "[HELP] " + Colorize.ANSI_RESET + "["
			+ Colorize.ANSI_WHITE + "Mini" + Colorize.ANSI_BRIGHT_RED + "Terminal" + Colorize.ANSI_RESET + "] "
			+ Colorize.ANSI_BRIGHT_YELLOW;

	public static void main(String[] args) throws IOException {
		// Check that the user running the application is not root.
		if (user.equals("root") || user.equals("Administrator")) {
			System.out.println(
					ERRPREFIX + "No puedes ejecutar esta aplicación con el usuario ROOT" + Colorize.ANSI_RESET);
			System.exit(1);
		}
		// Add all available commands to an array.
		commands.add("pwd");
		commands.add("cd");
		commands.add("ls");
		commands.add("ll");
		commands.add("mkdir");
		commands.add("touch");
		commands.add("echo");
		commands.add("cat");
		commands.add("rm");
		commands.add("mv");
		commands.add("nano");
		commands.add("history");
		commands.add("find");
		commands.add("clear");
		commands.add("help");
		commands.add("?");
		commands.add("exit");
		commands.add("quit");
		// Prepare the application to load the terminal and all its components.
		System.setProperty("user.dir", System.getProperty("user.home"));
		TerminalBuilder builder = TerminalBuilder.builder();
		builder.system(true);
		Completer completer = new AggregateCompleter(new Completers.FileNameCompleter(),
				new StringsCompleter(commands));
		DefaultParser p3 = new DefaultParser();
		p3.setEscapeChars(new char[] {});
		Parser parser = p3;
		List<Consumer<LineReader>> callbacks = new ArrayList<>();
		Terminal terminal = null;
		try {
			terminal = builder.build();
		} catch (IOException e2) {
			System.out.println(ERRPREFIX + "An error has ocurred loading the terminal.");
			System.exit(1);
		}
		LineReader reader = LineReaderBuilder.builder().terminal(terminal).history(history).completer(completer)
				.parser(parser).variable(LineReader.SECONDARY_PROMPT_PATTERN, "%M%P > ")
				.variable(LineReader.INDENTATION, 2).option(Option.INSERT_BRACKET, true).build();
		history.attach(reader);
		history.read(new File(System.getProperty("user.home"), ".minihistory").toPath(), true);
		AutosuggestionWidgets autosuggestionWidgets = new AutosuggestionWidgets(reader);
		autosuggestionWidgets.enable();
		callbacks.forEach(c -> c.accept(reader));
		if (!callbacks.isEmpty()) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// Clear the screen, prints the welcome message and starts the while that makes the magic.
		terminal.puts(Capability.clear_screen);
		terminal.flush();
		printWelcome();
		while (isRunning) {
			// Prints the prompt for each iteration of the while and save the data into a ParsedLine variable type
			String line = null;
			line = reader.readLine(printPrompt(), printPromptRight(), (MaskingCallback) null, null);
			line = line.trim();
			terminal.flush();
			ParsedLine pl = reader.getParser().parse(line, 0);
			// Saves the arguments into this variable to send it to the commands functions.
			String[] argv = pl.words().subList(1, pl.words().size()).toArray(new String[0]);
			// Takes the command of the parsed line and goes into the correct command in the switch.
			switch (pl.word().toLowerCase()) {
			case "pwd":
				System.out.println(wd);
				// At the end all of the commands add the line that is the string not parsed to the history for save it.
				addToHistory(line);
				break;
			case "cd":
				// Check the length of the arguments to execute the correct parameter overload.
				if (argv.length > 0) {
					try {
						FileManager.cd(argv[0]);
					} catch (Exception e) {
						printPathNotFound();
					}
				} else {
					FileManager.cd();
				}
				addToHistory(line);
				break;
			case "ls":
				if (argv.length > 0) {
					try {
						FileManager.ls(argv[0]);
					} catch (Exception e) {
						printPathNotFound();
					}
				} else
					try {
						FileManager.ls();
					} catch (Exception e) {
						printPathNotFound();
					}
				addToHistory(line);
				break;
			case "ll":
				if (argv.length > 0) {
					try {
						FileManager.ll(argv[0]);
					} catch (Exception e) {
						printNotFound();
					}
				} else
					try {
						FileManager.ll();
					} catch (Exception e1) {
						printPathNotFound();
					}
				addToHistory(line);
				break;
			case "mkdir":
				if (argv.length > 0) {
					try {
						FileManager.mkdir(argv[0]);
					} catch (Exception e) {
						printPathNotFound();
					}
				} else
					System.out.println(PREFIX + "Expected almost one arguments");
				addToHistory(line);
				break;
			case "touch":
				if (argv.length > 0) {
					try {
						FileManager.touch(argv[0]);
					} catch (Exception e) {
						printPathNotFound();
					}
				} else
					System.out.println(PREFIX + "Expected almost one argument.");
				addToHistory(line);
				break;
			case "echo":
				if (argv.length > 0) {
					// Concatenate all of the strings of argv to obtain the full sentence to print.
					String concat = "";
					for (int i = 0; i < argv.length; i++) {
						concat = concat + " " + argv[i];
					}
					System.out.println(concat);
				}
				addToHistory(line);
				break;
			case "cat":
				if (argv.length > 0) {
					try {
						FileManager.cat(argv[0]);
					} catch (Exception e) {
						printPathNotFound();
					}
				} else
					System.out.println(PREFIX + "Expected almost one argument.");
				addToHistory(line);
				break;
			case "rm":
				if (argv.length > 0) {
					try {
						FileManager.rm(argv[0]);
					} catch (FileNotFoundException e) {
						printNotFound();
					}
				} else
					System.out.println(PREFIX + "Expected almost one argument.");
				addToHistory(line);
				break;
			case "mv":
				if (argv.length > 1) {
					try {
						FileManager.mv(argv[0], argv[1]);
					} catch (Exception e) {
						System.out.println(ERRPREFIX + "The file/directory already exists." + Colorize.ANSI_RESET);
					}
				} else
					System.out.println(PREFIX + "Expected almost two arguments.");
				addToHistory(line);
				break;
			case "nano":
				try {
					FileManager.nano(terminal, argv);
				} catch (Exception e) {
					System.out.println(
							MiniTerminal.ERRPREFIX + "An error has occurred loading nano." + Colorize.ANSI_RESET);
				}
				addToHistory(line);
				break;
			case "history":
				try {
					Commands.history(reader, System.out, System.err, Paths.get(""), argv);
				} catch (Exception e) {
					System.out.println(ERRPREFIX + "An error has occurred loading nano." + Colorize.ANSI_RESET);
				}
				addToHistory(line);
				break;
			case "find":
				if (argv.length > 0) {
					FileManager.find(argv[0]);
				} else
					System.out.println(PREFIX + "Expected almost one argument");
				addToHistory(line);
				break;
			case "clear":
				// Uses a function provided by JLine3 for clear the screen.
				terminal.puts(Capability.clear_screen);
				terminal.flush();
				addToHistory(line);
				break;
			case "wget":
				if (argv.length == 1) {
					try {
						WebManager.wget(argv[0]);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (argv.length > 1) {
					try {
						WebManager.wget(argv[0], argv[1]);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else
					System.out.println(PREFIX + "Expected almost one argument");
				break;
			case "help":
				if (argv.length > 0)
					printHelp(argv[0]);
				else {
					terminal.puts(Capability.clear_screen);
					terminal.flush();
					printHelp();
				}
				addToHistory(line);
				break;
			case "?":
				if (argv.length > 0)
					printHelp(argv[1]);
				else {
					terminal.puts(Capability.clear_screen);
					terminal.flush();
					printHelp();
				}
				addToHistory(line);
				break;
			case "exit":
				System.out.println(PREFIX + "Quitting...");
				addToHistory(line);
				// Saves the history in a file on the user home.
				history.append(new File(System.getProperty("user.home"), ".minihistory").toPath(), true);
				System.exit(0);
				break;
			case "quit":
				System.out.println(PREFIX + "Quitting...");
				addToHistory(line);
				history.append(new File(System.getProperty("user.home"), ".minihistory").toPath(), true);
				System.exit(0);
				break;
			// For enter without characters.
			case "":
				break;
			// For bad input of a command.
			default:
				System.out.println(
						ERRPREFIX + "No such command. Try 'help' to see the avaliable commands. " + Colorize.ANSI_RESET);
				break;
			}
		}
	}

	// This function adds a string to the history that JLine3 gives us.
	private static void addToHistory(final String line) {
		try {
			history.add(line);
			history.save();
		} catch (final IOException e) {
			Log.error("Error saving history file", e);
			System.out.println("Error saving history file.");
		}
	}
	
	// Prints the welcome message.
	private static void printWelcome() {
		System.out.println(Colorize.ANSI_PURPLE
				+ "___  ___ _         _  _____                         _                _ \n"
				+ "|  \\/  |(_)       (_)|_   _|                       (_)              | |\n"
				+ "| .  . | _  _ __   _   | |    ___  _ __  _ __ ___   _  _ __    __ _ | |\n"
				+ "| |\\/| || || '_ \\ | |  | |   / _ \\| '__|| '_ ` _ \\ | || '_ \\  / _` || |\n"
				+ "| |  | || || | | || |  | |  |  __/| |   | | | | | || || | | || (_| || |\n"
				+ "\\_|  |_/|_||_| |_||_|  \\_/   \\___||_|   |_| |_| |_||_||_| |_| \\__,_||_|\n"
				+ "                                              type 'help' for more info" + Colorize.ANSI_RESET);
	}

	// Prints the help general command.
	private static void printHelp() {
		System.out.println(Colorize.ANSI_BRIGHT_YELLOW + " __    __   _______  __      .______   \n"
				+ "|  |  |  | |   ____||  |     |   _  \\  \n" + "|  |__|  | |  |__   |  |     |  |_)  | \n"
				+ "|   __   | |   __|  |  |     |   ___/  \n" + "|  |  |  | |  |____ |  `----.|  |      \n"
				+ "|__|  |__| |_______||_______|| _|      ");
		System.out.println("\nWelcome to the help! Use: 'help [command]' for specific info of a command\n"
				+ "Avaliable commands:\n" + "pwd\n" + "cd\n" + "ls\n" + "ll\n" + "mkdir\n" + "touch\n" + "echo\n"
				+ "cat\n" + "rm\n" + "mv\n" + "find\n" + "wget\n" + "nano\n" + "history\n" + "clear\n" + "help\n"
				+ "exit" + Colorize.ANSI_RESET);
	}

	// Prints the specific help command.
	private static void printHelp(String arg) {
		switch (arg) {
		case "pwd":
			System.out.println(HELPPREFIX + "Use that command to print the working directory.\n" + "Syntax: pwd"
					+ Colorize.ANSI_RESET);
			break;
		case "cd":
			System.out.println(HELPPREFIX + "Use that command to change the working directory.\n" + "Syntax: cd [path]"
					+ Colorize.ANSI_RESET);
			break;
		case "ls":
			System.out.println(HELPPREFIX + "Use that command to list the contests of a directory.\n"
					+ "Syntax: ls [path]" + Colorize.ANSI_RESET);
			break;
		case "ll":
			System.out.println(HELPPREFIX + "Use that command to list the contents of a directory with more info.\n"
					+ "Syntax: ll [path]" + Colorize.ANSI_RESET);
			break;
		case "mkdir":
			System.out.println(HELPPREFIX + "Use that command to make a directory.\n" + "Syntax: mkdir <path>"
					+ Colorize.ANSI_RESET);
			break;
		case "touch":
			System.out.println(
					HELPPREFIX + "[Use that command to make a file.\n" + "Syntax: touch <file>" + Colorize.ANSI_RESET);
			break;
		case "echo":
			System.out.println(HELPPREFIX + "Use that command to print something.\n" + "Syntax: echo <text>"
					+ Colorize.ANSI_RESET);
			break;
		case "cat":
			System.out.println(HELPPREFIX + "Use that command to see the content of a file.\n" + "Syntax: cat <file>"
					+ Colorize.ANSI_RESET);
			break;
		case "rm":
			System.out.println(HELPPREFIX + "Use that command to remove a file or a directory.\n" + "Syntax: rm <path>"
					+ Colorize.ANSI_RESET);
			break;
		case "mv":
			System.out.println(HELPPREFIX + "Use that command to move a file across directories or rename it.\n"
					+ "Syntax: mv <path> <path>" + Colorize.ANSI_RESET);
			break;
		case "find":
			System.out.println(HELPPREFIX + "Use that command to search a file.\n" + "Syntax: find <search>"
					+ Colorize.ANSI_RESET);
			break;
		case "wget":
			System.out.println(
					HELPPREFIX + "Use that command to download data and print to the console.\nIf you specify a file name saves the data into the file.\n" + "Syntax: wget <URL> [file]" + Colorize.ANSI_RESET);
		case "nano":
			System.out.println(HELPPREFIX + "Use that command to edit with nano a file.\n" + "Syntax: nano <path>"
					+ Colorize.ANSI_RESET);
			break;
		case "history":
			System.out.println(HELPPREFIX + "Use that command to see the last commands you used\n" + "Syntax: history"
					+ Colorize.ANSI_RESET);
			break;
		case "clear":
			System.out.println(
					HELPPREFIX + "Use that command to clear console.\n" + "Syntax: clear" + Colorize.ANSI_RESET);
			break;
		case "help":
			System.out.println(HELPPREFIX + "Use that command to show this help.\n" + "Syntax: help [command]"
					+ Colorize.ANSI_RESET);
			break;
		case "exit":
			System.out.println(
					HELPPREFIX + "Use that command to terminate the program.\n" + "Syntax: exit" + Colorize.ANSI_RESET);
			break;
		default:
			System.out.println(
					ERRPREFIX + "No such command. Try 'help' to see the avaliable commands." + Colorize.ANSI_RESET);
			break;
		}
	}

	// Definition of the prompt.
	private static String printPrompt() {
		String rt = "";
		if (wd.getAbsolutePath() == System.getProperty("user.home")) {
			rt = Colorize.ANSI_BRIGHT_GREEN + "⁂  " + user + "@" + systemName + " ϟ " + Colorize.ANSI_BRIGHT_BLUE + "~"
					+ Colorize.ANSI_BRIGHT_GREEN + " ➲ " + Colorize.ANSI_RESET;
		} else if (wd.getAbsolutePath().startsWith(System.getProperty("user.home"))) {
			String finalPath = wd.getAbsolutePath().replace(System.getProperty("user.home"), "~");
			rt = Colorize.ANSI_BRIGHT_GREEN + "⁂  " + user + "@" + systemName + " ϟ " + Colorize.ANSI_BRIGHT_BLUE
					+ finalPath + Colorize.ANSI_BRIGHT_GREEN + " ➲ " + Colorize.ANSI_RESET;
		} else
			rt = Colorize.ANSI_BRIGHT_GREEN + "⁂  " + user + "@" + systemName + " ϟ " + Colorize.ANSI_BRIGHT_BLUE + wd
					+ Colorize.ANSI_BRIGHT_GREEN + " ➲ " + Colorize.ANSI_RESET;
		return rt;
	}

	// Definition of the right prompt.
	private static String printPromptRight() {
		String rightPrompt = new AttributedStringBuilder().style(AttributedStyle.DEFAULT)
				.append(LocalDate.now().format(DateTimeFormatter.ISO_DATE)).append(" ")
				.style(AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN | AttributedStyle.BRIGHT))
				.append(LocalTime.now().format(new DateTimeFormatterBuilder().appendValue(HOUR_OF_DAY, 2)
						.appendLiteral(':').appendValue(MINUTE_OF_HOUR, 2).toFormatter()))
				.toAnsi();
		return rightPrompt;
	}

	// Error simplified into a function.
	private static void printNotFound() {
		System.out.println(ERRPREFIX + "The file does not exist." + Colorize.ANSI_RESET);
	}

	// Error simplified into a function.
	private static void printPathNotFound() {
		System.out.println(ERRPREFIX + "The path does not exist." + Colorize.ANSI_RESET);
	}

	// ** GETTERS ** //
	
	public static File getWd() {
		return wd;
	}

	// ** SETTERS ** //
	
	public static void setWd(File wd) {
		MiniTerminal.wd = wd;
	}
}