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

	public static void main(String[] args) {
		if (user.equals("root")) {
			System.out.println("No puedes ejecutar esta aplicación con el usuario ROOT");
			System.exit(1);
		}
		clearScreen();
		printWelcome();
		System.out.println(user);
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
					System.out.println("[MiniTerminal] Expected almost one arguments");
				break;
			case "touch":
				if (command.length >= 1) {
					try {
						FileManager.touch(command[1]);
					} catch (Exception e) {
						printPathNotFound();
					}
				} else
					System.out.println("[MiniTerminal] Expected almost one argument.");
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
					System.out.println("[MiniTerminal] Expected almost one argument.");
				break;
			case "rm":
				if (command.length >= 1) {
					try {
						FileManager.rm(command[1]);
					} catch (FileNotFoundException e) {
						printNotFound();
					}
				} else
					System.out.println("[MiniTerminal] Expected almost one argument.");
				break;
			case "mv":
				if (command.length >= 2) {
					try {
						FileManager.mv(command[1], command[2]);
					} catch (Exception e) {
						System.out.println("[MiniTerminal] The file/directory already exists.");
					}
				} else
					System.out.println("[MiniTerminal] Expected almost two arguments.");
				break;
			case "find":
				if (command.length > 1) {
					FileManager.find(command[1]);
				} else
					System.out.println("[MiniTerminal] Expected almost one argument");
				break;
			case "clear":
				clearScreen();
				break;
			case "help":
				clearScreen();
				if (command.length > 1)
					printHelp(command[1]);
				else
					printHelp();
				break;
			case "exit":
				System.out.println("Quitting...");
				System.exit(0);
				break;
			case "":
				break;
			default:
				System.out.println("[MiniTerminal] No such command. Try 'help' to see the avaliable commands.");
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
		System.out.println("Welcome to the help!\n" + "Use: 'help [command]' for specific info of a command\n"
				+ "Avaliable commands:\n" + "pwd\n" + "cd\n" + "ls\n" + "ll\n" + "mkdir\n" + "touch\n" + "echo\n"
				+ "cat\n" + "rm\n" + "mv\n" + "find\n" + "clear\n" + "help\n" + "exit");
	}

	private static void printHelp(String arg) {
		switch (arg) {
		case "pwd":
			System.out
					.println("[MiniTerminal HELP] Use that command to print the working directory.\n" + "Syntax: pwd");
			break;
		case "cd":
			System.out.println(
					"[MiniTerminal HELP] Use that command to change the working directory.\n" + "Syntax: cd [path]");
			break;
		case "ls":
			System.out.println("[MiniTerminal HELP] Use that command to list the contests of a directory.\n"
					+ "Syntax: ls [path]");
			break;
		case "ll":
			System.out.println(
					"[MiniTerminal HELP] Use that command to list the contents of a directory with more info.\n"
							+ "Syntax: ll [path]");
			break;
		case "mkdir":
			System.out.println("[MiniTerminal HELP] Use that command to make a directory.\n" + "Syntax: mkdir <path>");
			break;
		case "touch":
			System.out.println("[MiniTerminal HELP] Use that command to make a file.\n" + "Syntax: touch <file>");
			break;
		case "echo":
			System.out.println("[MiniTerminal HELP] Use that command to print something.\n" + "Syntax: echo <text>");
			break;
		case "cat":
			System.out.println(
					"[MiniTerminal HELP] Use that command to see the content of a file.\n" + "Syntax: cat <file>");
			break;
		case "rm":
			System.out.println(
					"[MiniTerminal HELP] Use that command to remove a file or a directory.\n" + "Syntax: rm <path>");
			break;
		case "mv":
			System.out.println("[MiniTerminal HELP] Use that command to move a file across directories or rename it.\n"
					+ "Syntax: mv <path> <path>");
			break;
		case "find":
			System.out.println("[MiniTerminal HELP] Use that command to search a file.\n" + "Syntax: find <search>");
			break;
		case "clear":
			System.out.println("[MiniTerminal HELP] Use that command to clear console.\n" + "Syntax: clear");
			break;
		case "help":
			System.out.println("[MiniTerminal HELP] Use that command to show this help.\n" + "Syntax: help [command]");
			break;
		case "exit":
			System.out.println("[MiniTerminal HELP] Use that command to terminate the program.\n" + "Syntax: exit");
			break;
		default:
			System.out.println("[MiniTerminal HELP] No such command. Try 'help' to see the avaliable commands.");
			break;
		}
	}

	private static void printPrompt() {
		if (wd.getAbsolutePath() == System.getProperty("user.home"))
			System.out.print("⁂  " + user + "@" + systemName + " ϟ ~ ➲ ");
		else if (wd.getAbsolutePath().startsWith(System.getProperty("user.home"))) {
			String finalPath = wd.getAbsolutePath().replace(System.getProperty("user.home"), "~");
			System.out.print("⁂  " + user + "@" + systemName + " ϟ " + finalPath + " ➲ ");
		} else
			System.out.print("⁂  " + user + "@" + systemName + " ϟ " + wd + " ➲ ");
	}

	private static void printWelcome() {
		System.out.println("___  ___ _         _  _____                         _                _ \n"
				+ "|  \\/  |(_)       (_)|_   _|                       (_)              | |\n"
				+ "| .  . | _  _ __   _   | |    ___  _ __  _ __ ___   _  _ __    __ _ | |\n"
				+ "| |\\/| || || '_ \\ | |  | |   / _ \\| '__|| '_ ` _ \\ | || '_ \\  / _` || |\n"
				+ "| |  | || || | | || |  | |  |  __/| |   | | | | | || || | | || (_| || |\n"
				+ "\\_|  |_/|_||_| |_||_|  \\_/   \\___||_|   |_| |_| |_||_||_| |_| \\__,_||_|\n"
				+ "                                              type 'help' for more info");
	}

	private static void printNotFound() {
		System.out.println("[MiniTermnal] The file does not exist.");
	}

	private static void printPathNotFound() {
		System.out.println("[MiniTermnal] The path does not exist.");
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
			System.err.println("[!] clearScreen() no se ejecuto correctamente.");
		}
	}

	public static File getWd() {
		return wd;
	}

	public static void setWd(File wd) {
		MiniTerminal.wd = wd;
	}
}
