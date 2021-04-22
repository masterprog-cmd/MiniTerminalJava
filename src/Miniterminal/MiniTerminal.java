package Miniterminal;

import java.io.File;
import java.util.Scanner;

public class MiniTerminal {

	private static boolean isRunning = true;
	protected static String systemName = System.getProperty("os.name");
	protected static String user = System.getProperty("user.name");
	protected static File wd = new File(System.getProperty("user.home"));

	public static void main(String[] args) {
		if (user == "root") {
			System.out.println("No puedes ejecutar esta aplicación con el usuario ROOT");
			System.exit(1);
		}
		clearScreen();
		printWelcome();
		while (isRunning) {
			String[] command = prompt().split(" ");
			switch (command[0]) {
			case "pwd":
				System.out.println(wd);
				break;
			case "cd":
				break;
			case "ls":
				break;
			case "ll":
				break;
			case "mkdir":
				break;
			case "rm":
				break;
			case "mv":
				break;
			case "help":
				clearScreen();
				break;
			case "exit":
				System.out.println("Quitting...");
				System.exit(0);
				break;
			default:
				break;
			}
		}
	}

	private static String prompt() {
		Scanner in = new Scanner(System.in);
		printPrompt();
		String command = in.nextLine();
		return command;
	}

	private static void printPrompt() {
		if (wd.getAbsolutePath() == System.getProperty("user.home"))
			System.out.print("⁂ " + user + "@" + systemName + " ϟ ~ ➲ ");
		else if (wd.getAbsolutePath().startsWith(System.getProperty("user.home"))) {
			String finalPath = wd.getAbsolutePath().replace(System.getProperty("user.home"), "~/");
			System.out.print("⁂ " + user + "@" + systemName + " ϟ " + finalPath + " ➲ ");
		} else
			System.out.print("⁂ " + user + "@" + systemName + " ϟ " + wd + " ➲ ");
	}

	private static void printWelcome() {
		System.out.println("___  ___ _         _  _____                         _                _ \n"
				+ "|  \\/  |(_)       (_)|_   _|                       (_)              | |\n"
				+ "| .  . | _  _ __   _   | |    ___  _ __  _ __ ___   _  _ __    __ _ | |\n"
				+ "| |\\/| || || '_ \\ | |  | |   / _ \\| '__|| '_ ` _ \\ | || '_ \\  / _` || |\n"
				+ "| |  | || || | | || |  | |  |  __/| |   | | | | | || || | | || (_| || |\n"
				+ "\\_|  |_/|_||_| |_||_|  \\_/   \\___||_|   |_| |_| |_||_||_| |_| \\__,_||_|");
	}

	private static void clearScreen() {
		try {
			if (systemName.contains("Windows")) {
				Runtime.getRuntime().exec("cls");
			} else {
				Runtime.getRuntime().exec("clear");
			}
		} catch (final Exception e) {
			System.err.println("[!] clearScreen() no se ejecuto correctamente.");
		}
	}
}
