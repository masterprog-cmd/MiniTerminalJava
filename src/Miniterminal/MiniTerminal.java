package Miniterminal;

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

import Files.FileManager;
import Files.WebManager;

public class MiniTerminal {

	private static boolean isRunning = true;
	protected static String systemName = System.getProperty("os.name");
	protected static String user = System.getProperty("user.name");
	protected static File wd = new File(System.getProperty("user.home"));
	private static Collection<String> commands = new ArrayList<String>();
	private static History history = new DefaultHistory();

	public static String prefix = "[" + Colorize.ANSI_WHITE + "Mini" + Colorize.ANSI_BRIGHT_RED + "Terminal"
			+ Colorize.ANSI_RESET + "] ";
	public static String errPrefix = Colorize.ANSI_RED + "[!] " + Colorize.ANSI_RESET + "[" + Colorize.ANSI_WHITE
			+ "Mini" + Colorize.ANSI_BRIGHT_RED + "Terminal" + Colorize.ANSI_RESET + "] " + Colorize.ANSI_RED;
	public static String helpPrefix = Colorize.ANSI_BRIGHT_YELLOW + "[HELP] " + Colorize.ANSI_RESET + "["
			+ Colorize.ANSI_WHITE + "Mini" + Colorize.ANSI_BRIGHT_RED + "Terminal" + Colorize.ANSI_RESET + "] "
			+ Colorize.ANSI_BRIGHT_YELLOW;

	public static void main(String[] args) throws IOException {
		if (user.equals("root") || user.equals("Administrator")) {
			System.out.println(
					errPrefix + "No puedes ejecutar esta aplicación con el usuario ROOT" + Colorize.ANSI_RESET);
			System.exit(1);
		}
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
			System.out.println(errPrefix + "An error has ocurred loading the terminal.");
			System.exit(1);
		}
		LineReader reader = LineReaderBuilder.builder().terminal(terminal).history(history).completer(completer)
				.parser(parser).variable(LineReader.SECONDARY_PROMPT_PATTERN, "%M%P > ")
				.variable(LineReader.INDENTATION, 2).option(Option.INSERT_BRACKET, true).build();
		history.attach(reader);
		history.read(new File(System.getProperty("user.home"), ".minihistory").toPath(), true);
		// Enable autosuggestions
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
		terminal.puts(Capability.clear_screen);
		terminal.flush();
		printWelcome();
		while (isRunning) {
			String line = null;
			line = reader.readLine(printPrompt(), printPromptRight(), (MaskingCallback) null, null);
			line = line.trim();
			terminal.flush();
			ParsedLine pl = reader.getParser().parse(line, 0);
			String[] argv = pl.words().subList(1, pl.words().size()).toArray(new String[0]);
			switch (argv[0].toLowerCase()) {
			case "pwd":
				System.out.println(wd);
				addToHistory(line);
				break;
			case "cd":
				if (argv.length > 1) {
					try {
						FileManager.cd(argv[1]);
						completer = new Completers.DirectoriesCompleter(getWd());
					} catch (Exception e) {
						printPathNotFound();
					}
				} else {
					FileManager.cd();
					completer = new AggregateCompleter(new Completers.DirectoriesCompleter(getWd()));
				}
				addToHistory(line);
				break;
			case "ls":
				if (argv.length > 1) {
					try {
						FileManager.ls(argv[1]);
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
				if (argv.length > 1) {
					try {
						FileManager.ll(argv[1]);
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
				if (argv.length >= 1) {
					try {
						FileManager.mkdir(argv[1]);
					} catch (Exception e) {
						printPathNotFound();
					}
				} else
					System.out.println(prefix + "Expected almost one arguments");
				addToHistory(line);
				break;
			case "touch":
				if (argv.length >= 1) {
					try {
						FileManager.touch(argv[1]);
					} catch (Exception e) {
						printPathNotFound();
					}
				} else
					System.out.println(prefix + "Expected almost one argument.");
				addToHistory(line);
				break;
			case "echo":
				if (argv.length > 1) {
					String concat = "";
					for (int i = 1; i < argv.length; i++) {
						concat = concat + " " + argv[i];
					}
					System.out.println(concat);
				}
				addToHistory(line);
				break;
			case "cat":
				if (argv.length >= 1) {
					try {
						FileManager.cat(argv[1]);
					} catch (Exception e) {
						printPathNotFound();
					}
				} else
					System.out.println(prefix + "Expected almost one argument.");
				addToHistory(line);
				break;
			case "rm":
				if (argv.length >= 1) {
					try {
						FileManager.rm(argv[1]);
					} catch (FileNotFoundException e) {
						printNotFound();
					}
				} else
					System.out.println(prefix + "Expected almost one argument.");
				addToHistory(line);
				break;
			case "mv":
				if (argv.length >= 2) {
					try {
						FileManager.mv(argv[1], argv[2]);
					} catch (Exception e) {
						System.out.println(errPrefix + "The file/directory already exists." + Colorize.ANSI_RESET);
					}
				} else
					System.out.println(prefix + "Expected almost two arguments.");
				addToHistory(line);
				break;
			case "nano":
				try {
					FileManager.nano(terminal, argv);
				} catch (Exception e) {
					System.out.println(
							MiniTerminal.errPrefix + "An error has occurred loading nano." + Colorize.ANSI_RESET);
				}
				addToHistory(line);
				break;
			case "history":
				try {
					Commands.history(reader, System.out, System.err, Paths.get(""), argv);
				} catch (Exception e) {
					System.out.println(errPrefix + "An error has occurred loading nano." + Colorize.ANSI_RESET);
				}
				addToHistory(line);
				break;
			case "find":
				if (argv.length > 1) {
					FileManager.find(argv[1]);
				} else
					System.out.println(prefix + "Expected almost one argument");
				addToHistory(line);
				break;
			case "clear":
				terminal.puts(Capability.clear_screen);
				terminal.flush();
				addToHistory(line);
				break;
			case "wget":
				if (command.length > 1) {
					try {
						WebManager.wget(command[1]);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else
					System.out.println(prefix + "Expected almost two arguments");
				break;
			case "help":
				if (argv.length > 1)
					printHelp(argv[1]);
				else {
					terminal.puts(Capability.clear_screen);
					terminal.flush();
					printHelp();
				}
				addToHistory(line);
				break;
			case "?":
				if (argv.length > 1)
					printHelp(argv[1]);
				else {
					terminal.puts(Capability.clear_screen);
					terminal.flush();
					printHelp();
				}
				addToHistory(line);
				break;
			case "exit":
				System.out.println(prefix + "Quitting...");
				addToHistory(line);
				history.append(new File(System.getProperty("user.home"), ".minihistory").toPath(), true);
				System.exit(0);
				break;
			case "quit":
				System.out.println(prefix + "Quitting...");
				addToHistory(line);
				history.append(new File(System.getProperty("user.home"), ".minihistory").toPath(), true);
				System.exit(0);
				break;
			case "":
				break;
			default:
				System.out.println(
						errPrefix + "No such command. Try 'help' to see the avaliable commands." + Colorize.ANSI_RESET);
				break;
			}
		}
	}

	private static void addToHistory(final String line) {
		try {
			history.add(line);
			history.save();
		} catch (final IOException e) {
			Log.error("Error saving history file", e);
			System.out.println("Error saving history file.");
		}
	}

	private static void printHelp() {
		System.out.println(Colorize.ANSI_BRIGHT_YELLOW + " __    __   _______  __      .______   \n"
				+ "|  |  |  | |   ____||  |     |   _  \\  \n" + "|  |__|  | |  |__   |  |     |  |_)  | \n"
				+ "|   __   | |   __|  |  |     |   ___/  \n" + "|  |  |  | |  |____ |  `----.|  |      \n"
				+ "|__|  |__| |_______||_______|| _|      ");
		System.out.println("\nWelcome to the help! Use: 'help [command]' for specific info of a command\n"
				+ "Avaliable commands:\n" + "pwd\n" + "cd\n" + "ls\n" + "ll\n" + "mkdir\n" + "touch\n" + "echo\n"
				+ "cat\n" + "rm\n" + "mv\n" + "find\n" + "wget\n" + "nano\n" + "history\n" + "clear\n" + "help\n" + "exit"
				+ Colorize.ANSI_RESET);
	}

	private static void printHelp(String arg) {
		switch (arg) {
		case "pwd":
			System.out.println(helpPrefix + "Use that command to print the working directory.\n" + "Syntax: pwd"
					+ Colorize.ANSI_RESET);
			break;
		case "cd":
			System.out.println(helpPrefix + "Use that command to change the working directory.\n" + "Syntax: cd [path]"
					+ Colorize.ANSI_RESET);
			break;
		case "ls":
			System.out.println(helpPrefix + "Use that command to list the contests of a directory.\n"
					+ "Syntax: ls [path]" + Colorize.ANSI_RESET);
			break;
		case "ll":
			System.out.println(helpPrefix + "Use that command to list the contents of a directory with more info.\n"
					+ "Syntax: ll [path]" + Colorize.ANSI_RESET);
			break;
		case "mkdir":
			System.out.println(helpPrefix + "Use that command to make a directory.\n" + "Syntax: mkdir <path>"
					+ Colorize.ANSI_RESET);
			break;
		case "touch":
			System.out.println(
					helpPrefix + "[Use that command to make a file.\n" + "Syntax: touch <file>" + Colorize.ANSI_RESET);
			break;
		case "echo":
			System.out.println(helpPrefix + "Use that command to print something.\n" + "Syntax: echo <text>"
					+ Colorize.ANSI_RESET);
			break;
		case "cat":
			System.out.println(helpPrefix + "Use that command to see the content of a file.\n" + "Syntax: cat <file>"
					+ Colorize.ANSI_RESET);
			break;
		case "rm":
			System.out.println(helpPrefix + "Use that command to remove a file or a directory.\n" + "Syntax: rm <path>"
					+ Colorize.ANSI_RESET);
			break;
		case "mv":
			System.out.println(helpPrefix + "Use that command to move a file across directories or rename it.\n"
					+ "Syntax: mv <path> <path>" + Colorize.ANSI_RESET);
			break;
		case "find":
			System.out.println(helpPrefix + "Use that command to search a file.\n" + "Syntax: find <search>"
					+ Colorize.ANSI_RESET);
			break;
		case "wget":
			System.out.println(
					helpPrefix + "Use that command to download a data.\n" + "Syntax: wget <URL>" + Colorize.ANSI_RESET);
		case "nano":
			System.out.println(helpPrefix + "Use that command to edit with nano a file.\n" + "Syntax: nano <path>"
					+ Colorize.ANSI_RESET);
			break;
		case "history":
			System.out.println(helpPrefix + "Use that command to see the last commands you used\n" + "Syntax: history"
					+ Colorize.ANSI_RESET);
			break;
		case "clear":
			System.out.println(
					helpPrefix + "Use that command to clear console.\n" + "Syntax: clear" + Colorize.ANSI_RESET);
			break;
		case "help":
			System.out.println(helpPrefix + "Use that command to show this help.\n" + "Syntax: help [command]"
					+ Colorize.ANSI_RESET);
			break;
		case "exit":
			System.out.println(
					helpPrefix + "Use that command to terminate the program.\n" + "Syntax: exit" + Colorize.ANSI_RESET);
			break;
		default:
			System.out.println(
					errPrefix + "No such command. Try 'help' to see the avaliable commands." + Colorize.ANSI_RESET);
			break;
		}
	}

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

	private static String printPromptRight() {
		String rightPrompt = new AttributedStringBuilder().style(AttributedStyle.DEFAULT)
				.append(LocalDate.now().format(DateTimeFormatter.ISO_DATE)).append(" ")
				.style(AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN | AttributedStyle.BRIGHT))
				.append(LocalTime.now().format(new DateTimeFormatterBuilder().appendValue(HOUR_OF_DAY, 2)
						.appendLiteral(':').appendValue(MINUTE_OF_HOUR, 2).toFormatter()))
				.toAnsi();
		return rightPrompt;
	}

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

	private static void printNotFound() {
		System.out.println(errPrefix + "The file does not exist." + Colorize.ANSI_RESET);
	}

	private static void printPathNotFound() {
		System.out.println(errPrefix + "The path does not exist." + Colorize.ANSI_RESET);
	}

	public static File getWd() {
		return wd;
	}

	public static void setWd(File wd) {
		MiniTerminal.wd = wd;
	}
}