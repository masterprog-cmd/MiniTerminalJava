package Miniterminal;

import java.io.File;
import java.io.IOException;

public class History {

	private static File history = new File(System.getProperty("user.home") + ".minihistory");

	public static boolean checkHistory() throws IOException {
		boolean check = false;
		if (!history.exists()) {
			history.createNewFile();
			check = true;
		} else {
			check = true;
		}
		return check;
	}

	public static void addHistory() {

	}
}
