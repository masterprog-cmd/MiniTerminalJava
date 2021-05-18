package managers;

/* *
 * 
 * This class contains the definition of all the commands 
 * that have something to do with files. 
 * 
 * 
 * MiniTerminal Java 
 * by @alejandrofan2 | @masterprog-cmd 
 * 
 * */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.jline.builtins.Commands;
import org.jline.terminal.Terminal;

import miniTerminal.Colorize;
import miniTerminal.MiniTerminal;

public class FileManager {

	// Command to changes the working directory.
	public static void cd() {
		MiniTerminal.setWd(new File(System.getProperty("user.home")));
		System.setProperty("user.dir", System.getProperty("user.home"));
	}

	// Command to changes the working directory to an specific one. (Parameter Overload)
	public static void cd(String arg) throws Exception {
		if (!arg.startsWith("/")) {
			arg = relToAbs(arg);
		}
		File dir = new File(arg);
		if (!dir.exists()) {
			throw new Exception();
		}
		if (dir.exists()) {
			MiniTerminal.setWd(dir);
			System.setProperty("user.dir", arg);
		} else
			throw new Exception();
	}

	// Command to create a directory.
	public static boolean mkdir(String arg) throws Exception {
		if (!arg.startsWith("/")) {
			arg = relToAbs(arg);
		}
		File a = new File(arg);
		if (!a.getParentFile().exists()) {
			throw new Exception();
		}
		if (!a.exists()) {
			if (a.mkdirs())
				return true;
			else
				System.out.println(MiniTerminal.ERRPREFIX + "An error occurred creating the directory.");
		} else
			System.out.println(MiniTerminal.PREFIX + "Directory already exists.");
		return false;
	}

	// Command to create a file.
	public static boolean touch(String arg) throws Exception {
		if (!arg.startsWith("/")) {
			arg = relToAbs(arg);
		}
		File a = new File(arg);
		if (!a.getParentFile().exists()) {
			throw new Exception();
		}
		if (!a.exists()) {
			if (a.createNewFile())
				return true;
			else
				System.out.println(MiniTerminal.ERRPREFIX + "An error occurred creating the file." +  Colorize.ANSI_RESET);
		} else
			System.out.println(MiniTerminal.PREFIX + "File already exists.");
		return false;
	}

	// Command to remove a file or directory.
	public static boolean rm(String arg) throws FileNotFoundException {
		if (!arg.startsWith("/")) {
			arg = relToAbs(arg);
		}
		File a = new File(arg);
		if (!a.exists()) {
			System.out.println(a.getAbsolutePath());
			throw new FileNotFoundException();
		}
		if (a.isDirectory()) {
			for (File mirar : a.listFiles()) {
				if (mirar.isDirectory()) {
					for (File eliminar : mirar.listFiles()) {
						eliminar.delete();
					}
				}
				mirar.delete();
			}
		}
		a.delete();

		return true;
	}

	// Command to see the content of a directory.
	public static void ls() throws Exception {
		File[] listado = MiniTerminal.getWd().listFiles();
		if (!MiniTerminal.getWd().exists()) {
			throw new Exception();
		}
		Arrays.sort(listado);
		if (listado == null || listado.length == 0) {
			System.out.println(MiniTerminal.PREFIX + "Sorry, but this directory/file is empty.");
			return;
		} else {
			for (int i = 0; i < listado.length; i++) {
				if (listado[i].isDirectory())
					System.out.println(listado[i].getName() + "/");
				else
					System.out.println(listado[i].getName());
			}
		}
	}

	// Command to see the content of a specific directory. (Parameter Overload)
	public static void ls(String arg) throws Exception {
		if (!arg.startsWith("/")) {
			arg = relToAbs(arg);
		}
		File a = new File(arg);
		if (!a.exists()) {
			throw new Exception();
		}
		File[] listado = a.listFiles();
		Arrays.sort(listado);
		if (listado == null || listado.length == 0) {
			System.out.println(MiniTerminal.PREFIX + "Sorry, but this directory/file is empty.");
			return;
		} else {
			for (int i = 0; i < listado.length; i++) {
				if (listado[i].isDirectory())
					System.out.println(listado[i].getName() + "/");
				else
					System.out.println(listado[i].getName());
			}
		}
	}

	// Command to see the content of a directory with details.
	public static void ll() throws Exception {
		File a = MiniTerminal.getWd();
		if (!a.exists()) {
			throw new Exception();
		}
		SimpleDateFormat fechaMod = new SimpleDateFormat();
		File[] listado = a.listFiles();
		Arrays.sort(listado);
		if (listado == null || listado.length == 0) {
			System.out.println(MiniTerminal.PREFIX + "Sorry, but this directory/file is empty.");
			return;
		} else {
			for (int i = 0; i < listado.length; i++) {
				if (listado[i].isDirectory())
					// Prints a formatted output.9
					System.out.format("%15s%12s%25s", listado[i].length() + " bytes  ",
							fechaMod.format(listado[i].lastModified()), "  " + listado[i].getName() + "/\n");
				else
					System.out.format("%15s%12s%25s", listado[i].length() + " bytes  ",
							fechaMod.format(listado[i].lastModified()), "  " + listado[i].getName() + "\n");
			}
		}
	}

	// Command to see the content of a specific directory with details. (Parameter overload)
	public static void ll(String arg) throws Exception {
		if (!arg.startsWith("/")) {
			arg = relToAbs(arg);
		}
		File a = new File(arg);
		SimpleDateFormat fechaMod = new SimpleDateFormat();
		if (!a.exists()) {
			throw new Exception();
		}
		File[] listado = a.listFiles();
		Arrays.sort(listado);
		if (listado == null || listado.length == 0) {
			System.out.println(MiniTerminal.PREFIX + "Sorry, but this directory/file is empty.");
			return;
		} else {
			for (int i = 0; i < listado.length; i++) {
				if (listado[i].isDirectory())
					System.out.format("%15s%12s%25s", listado[i].length() + " bytes  ",
							fechaMod.format(listado[i].lastModified()), "  " + listado[i].getName() + "/\n");
				else
					System.out.format("%15s%12s%25s", listado[i].length() + " bytes  ",
							fechaMod.format(listado[i].lastModified()), "  " + listado[i].getName() + "\n");
			}
		}
	}

	// Command to move a file or directory.
	public static void mv(String arg, String arg1) throws Exception {
		if (!arg.startsWith("/")) {
			arg = relToAbs(arg);
		}
		if (!arg1.startsWith("/")) {
			arg1 = relToAbs(arg1);
		}
		File a = new File(arg);
		File b = new File(arg1);
		if (!b.exists()) {
			a.renameTo(b);
		} else
			throw new Exception();
	}
	
	// Command to see the content of a file.
	public static void cat(String arg) throws Exception {
		if (!arg.startsWith("/")) {
			arg = relToAbs(arg);
		}
		File a = new File(arg);
		if (!a.exists()) {
			throw new Exception();
		} else {
			try (BufferedReader br = new BufferedReader(new FileReader(a))) {
				String line;
				while ((line = br.readLine()) != null) {
					System.out.println(line);
				}
			}
		}
	}

	// Command to edit a file. It's NANO!
	public static void nano(Terminal term, String[] argv) throws Exception {
		if (!argv[0].startsWith("/")) {
			argv[0] = relToAbs(argv[0]);
		}
		Commands.nano(term, System.out, System.err, Paths.get(""), argv);
	}

	// Command to find a file into a directory.
	public static void find(String arg) {
		SimpleDateFormat fechaMod = new SimpleDateFormat();
		boolean find = false;
		String argBasic = arg;
		if (!arg.startsWith("/")) {
			arg = relToAbs(arg);
		}
		File a = new File(arg).getParentFile();
		File[] listado = a.listFiles();
		Arrays.sort(listado);
		System.out.format("%6s%16s%27s", "Size", "ModDate", "Name");
		System.out.println();
		for (int i = 0; i < listado.length; i++) {
			if (listado[i].getName().contains(argBasic)) {
				if (listado[i].isDirectory())
					System.out.format("%15s%12s%25s", listado[i].length() + " bytes  ",
							fechaMod.format(listado[i].lastModified()), "  " + listado[i].getName() + "/\n");
				else
					System.out.format("%15s%12s%25s", listado[i].length() + " bytes  ",
							fechaMod.format(listado[i].lastModified()), "  " + listado[i].getName() + "\n");
				find = true;
			}
		}
		if (!find) {
			System.out.println(MiniTerminal.PREFIX + "No results were found with this criterion.");
		}
	}

	// VERY IMPORTANT FUNCTION
	// Transforms the relative paths to absolute and add functionality to the . and .. for navigate backwards. 
	public static String relToAbs(String relPath) {
		String path;
		if (relPath.startsWith("..")) {
			File parentFolder = new File(MiniTerminal.getWd().getParent());
			File Absolute = new File(parentFolder, relPath.replace("..", ""));
			path = Absolute.getAbsolutePath();
		} else if (relPath.startsWith(".")) {
			File parentFolder = new File(MiniTerminal.getWd().getAbsolutePath());
			File Absolute = new File(parentFolder, relPath.replace(".", ""));
			path = Absolute.getAbsolutePath();
		} else {
			File parentFolder = new File(MiniTerminal.getWd().getAbsolutePath());
			File Absolute = new File(parentFolder, relPath);
			path = Absolute.getAbsolutePath();
		}
		return path;

	}
}
