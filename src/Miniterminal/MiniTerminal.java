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
import java.util.List;
import java.util.function.Consumer;

import org.jline.builtins.Commands;
import org.jline.builtins.Completers;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.LineReader.Option;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.MaskingCallback;
import org.jline.reader.ParsedLine;
import org.jline.reader.Parser;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.jline.utils.InfoCmp.Capability;

import Files.FileManager;

public class MiniTerminal {

	private static boolean isRunning = true;
	protected static String systemName = System.getProperty("os.name");
	protected static String user = System.getProperty("user.name");
	protected static File wd = new File(System.getProperty("user.home"));

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

		TerminalBuilder builder = TerminalBuilder.builder();
		Completer completer = new Completers.FileNameCompleter();
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
		LineReader reader = LineReaderBuilder.builder().terminal(terminal).completer(completer).parser(parser)
				.variable(LineReader.SECONDARY_PROMPT_PATTERN, "%M%P > ").variable(LineReader.INDENTATION, 2)
				.option(Option.INSERT_BRACKET, true).build();
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
			String[] command = line.split(" ");
			ParsedLine pl = reader.getParser().parse(line, 0);
			String[] argv = pl.words().subList(1, pl.words().size()).toArray(new String[0]);
			switch (command[0].toLowerCase()) {
			case "pwd":
				System.out.println(wd);
				break;
			case "cd":
				if (command.length > 1) {
					try {
						FileManager.cd(command[1]);
					} catch (Exception e) {
						printPathNotFound();
					}
				} else
					FileManager.cd();
				break;
			case "ls":
				if (command.length > 1) {
					try {
						FileManager.ls(command[1]);
					} catch (Exception e) {
						printPathNotFound();
					}
				} else
					try {
						FileManager.ls();
					} catch (Exception e) {
						printPathNotFound();
					}
				break;
			case "ll":
				if (command.length > 1) {
					try {
						FileManager.ll(command[1]);
					} catch (Exception e) {
						printNotFound();
					}
				} else
					try {
						FileManager.ll();
					} catch (Exception e1) {
						printPathNotFound();
					}
				break;
			case "mkdir":
				if (command.length >= 1) {
					try {
						FileManager.mkdir(command[1]);
					} catch (Exception e) {
						printPathNotFound();
					}
				} else
					System.out.println(prefix + "Expected almost one arguments");
				break;
			case "touch":
				if (command.length >= 1) {
					try {
						FileManager.touch(command[1]);
					} catch (Exception e) {
						printPathNotFound();
					}
				} else
					System.out.println(prefix + "Expected almost one argument.");
				break;
			case "echo":
				if (command.length > 1) {
					String concat = "";
					for (int i = 1; i < command.length; i++) {
						concat = concat + " " + command[i];
					}
					System.out.println(concat);
				}
				break;
			case "cat":
				if (command.length >= 1) {
					try {
						FileManager.cat(command[1]);
					} catch (Exception e) {
						printPathNotFound();
					}
				} else
					System.out.println(prefix + "Expected almost one argument.");
				break;
			case "rm":
				if (command.length >= 1) {
					try {
						FileManager.rm(command[1]);
					} catch (FileNotFoundException e) {
						printNotFound();
					}
				} else
					System.out.println(prefix + "Expected almost one argument.");
				break;
			case "mv":
				if (command.length >= 2) {
					try {
						FileManager.mv(command[1], command[2]);
					} catch (Exception e) {
						System.out.println(
								errPrefix + "[MiniTerminal] The file/directory already exists." + Colorize.ANSI_RESET);
					}
				} else
					System.out.println(prefix + "Expected almost two arguments.");
				break;
			case "nano":
				try {
					Commands.nano(terminal, System.out, System.err, Paths.get(""), argv);
				} catch (Exception e) {
					System.out.println(errPrefix + "An error has occurred loading nano." + Colorize.ANSI_RESET);
				}
				break;
			case "history":
				try {
					Commands.history(reader, System.out, System.err, Paths.get(""), argv);
				} catch (Exception e) {
					System.out.println(errPrefix + "An error has occurred loading nano." + Colorize.ANSI_RESET);
				}
				break;
			case "find":
				if (command.length > 1) {
					FileManager.find(command[1]);
				} else
					System.out.println(prefix + "Expected almost one argument");
				break;
			case "clear":
				terminal.puts(Capability.clear_screen);
				terminal.flush();
				break;
			case "help":
				if (command.length > 1)
					printHelp(command[1]);
				else {
					terminal.puts(Capability.clear_screen);
					terminal.flush();
					printHelp();
				}
			case "?":
				if (command.length > 1)
					printHelp(command[1]);
				else {
					terminal.puts(Capability.clear_screen);
					terminal.flush();
					printHelp();
				}
				break;
			case "exit":
				System.out.println("Quitting...");
				System.exit(0);
				break;
			case "quit":
				System.out.println("Quitting...");
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

	private static void printHelp() {
		System.out.println(Colorize.ANSI_BRIGHT_YELLOW + " __    __   _______  __      .______   \n"
				+ "|  |  |  | |   ____||  |     |   _  \\  \n" + "|  |__|  | |  |__   |  |     |  |_)  | \n"
				+ "|   __   | |   __|  |  |     |   ___/  \n" + "|  |  |  | |  |____ |  `----.|  |      \n"
				+ "|__|  |__| |_______||_______|| _|      ");
		System.out.println("\nWelcome to the help! Use: 'help [command]' for specific info of a command\n"
				+ "Avaliable commands:\n" + "pwd\n" + "cd\n" + "ls\n" + "ll\n" + "mkdir\n" + "touch\n" + "echo\n"
				+ "cat\n" + "rm\n" + "mv\n" + "find\n" + "nano\n" + "history\n" + "clear\n" + "help\n" + "exit"
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
				.append(LocalDate.now().format(DateTimeFormatter.ISO_DATE)).append("\n")
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
