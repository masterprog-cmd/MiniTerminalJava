package Miniterminal;

import java.io.File;
import java.util.Scanner;

public class MiniTerminal {

	public static boolean isRunning = true;
	protected static String systemName = System.getProperty("os.name");
	protected static String user = System.getProperty("user.name");
	protected static File wd = new File(System.getProperty("user.home"));

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		if (user == "root") {
			System.out.println("No puedes ejecutar esta aplicación con el usuario ROOT");
			System.exit(1);
		}
		clearScreen();
		while (isRunning) {
			String[] comando = prompt().split(" ");
			switch (comando[0]) {
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
				System.out.println("Help info");
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

	public static String prompt() {
		Scanner in = new Scanner(System.in);
		printPrompt();
		String comando = in.nextLine();
		return comando;
	}
	
	
	public static void printPrompt() {
		if (wd.getAbsolutePath() == System.getProperty("user.home"))
			System.out.print("⁂  " + user + "@" + systemName + " ϟ ~ ➲ ");
		else
			System.out.print("⁂  " + user + "@" + systemName + " ϟ " + wd + " ➲ ");

	}
	
	public static void clearScreen() {  
		for (int i = 0; i < 50; ++i) System.out.println();
	}

}
