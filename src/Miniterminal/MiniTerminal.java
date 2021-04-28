package Miniterminal;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

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

	public static void main(String[] args) {
		if (user.equals("root")) {
			System.out.println(
					errPrefix + "No puedes ejecutar esta aplicación con el usuario ROOT" + Colorize.ANSI_RESET);
			System.exit(1);
		}
		clearScreen();
		printWelcome();
		while (isRunning) {
			String[] command = prompt().split(" ");
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
			case "find":
				if (command.length > 1) {
					FileManager.find(command[1]);
				} else
					System.out.println(prefix + "Expected almost one argument");
				break;
			case "clear":
				clearScreen();
				break;
			case "help":
				if (command.length > 1)
					printHelp(command[1]);
				else
					clearScreen();
				printHelp();
				break;
			case "exit":
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

	private static String prompt() {
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		printPrompt();
		String command = in.nextLine();
		return command;
	}

	private static void printHelp() {
		System.out.println(Colorize.ANSI_BRIGHT_YELLOW + " __    __   _______  __      .______   \n"
				+ "|  |  |  | |   ____||  |     |   _  \\  \n" + "|  |__|  | |  |__   |  |     |  |_)  | \n"
				+ "|   __   | |   __|  |  |     |   ___/  \n" + "|  |  |  | |  |____ |  `----.|  |      \n"
				+ "|__|  |__| |_______||_______|| _|      ");
		System.out.println("\nWelcome to the help! Use: 'help [command]' for specific info of a command\n"
				+ "Avaliable commands:\n" + "pwd\n" + "cd\n" + "ls\n" + "ll\n" + "mkdir\n" + "touch\n" + "echo\n"
				+ "cat\n" + "rm\n" + "mv\n" + "find\n" + "clear\n" + "help\n" + "exit" + Colorize.ANSI_RESET);
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

	private static void printPrompt() {
		if (wd.getAbsolutePath() == System.getProperty("user.home"))
			System.out.print(Colorize.ANSI_BRIGHT_GREEN + "⁂  " + user + "@" + systemName + " ϟ "
					+ Colorize.ANSI_BRIGHT_BLUE + "~" + Colorize.ANSI_BRIGHT_GREEN + " ➲ " + Colorize.ANSI_RESET);
		else if (wd.getAbsolutePath().startsWith(System.getProperty("user.home"))) {
			String finalPath = wd.getAbsolutePath().replace(System.getProperty("user.home"), "~");
			System.out.print(Colorize.ANSI_BRIGHT_GREEN + "⁂  " + user + "@" + systemName + " ϟ "
					+ Colorize.ANSI_BRIGHT_BLUE + finalPath + Colorize.ANSI_BRIGHT_GREEN + " ➲ " + Colorize.ANSI_RESET);
		} else
			System.out.print(Colorize.ANSI_BRIGHT_GREEN + "⁂  " + user + "@" + systemName + " ϟ "
					+ Colorize.ANSI_BRIGHT_BLUE + wd + Colorize.ANSI_BRIGHT_GREEN + " ➲ " + Colorize.ANSI_RESET);
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

	private static void clearScreen() {
		try {
			if (systemName.contains("Windows")) {
				Runtime.getRuntime().exec("cls");
			} else {
				System.out.print("\033[H\033[2J");
				System.out.flush();
			}
		} catch (final Exception e) {
			System.err.println(errPrefix + "clearScreen() no se ejecuto correctamente." + Colorize.ANSI_RESET);
		}
	}

	public static File getWd() {
		return wd;
	}

	public static void setWd(File wd) {
		MiniTerminal.wd = wd;
	}
}
