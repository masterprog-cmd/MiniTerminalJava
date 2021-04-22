package Fitxers;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import Miniterminal.MiniTerminal;

public class FileManager {

	public static void cd() {
		MiniTerminal.setWd(new File(System.getProperty("user.home")));
	}

	public static void cd(String arg) throws Exception {
		if (!arg.startsWith("/")) {
			arg = relToAbs(arg);
		}
		// Absolute
		File dir = new File(arg);
		if (dir.exists())
			MiniTerminal.setWd(dir);
		else
			throw new Exception("The directory does not exist.");
	}

	public static boolean mkdir(String arg) {
		File a = new File(arg);
		if (!a.exists()) {
			if (a.mkdirs())
				System.out.println("Directory created.");
			return true;
		} else
			System.out.println("Directory doesn't created");
		return false;
	}

	public static boolean rmArch(String path) throws FileNotFoundException {
		File rm = new File(path);
		if (!rm.exists())
			throw new FileNotFoundException("The directory entered doesn't exist.");

		if (rm.isDirectory()) {
			for (File mirar : rm.listFiles()) {
				if (mirar.isDirectory()) {
					for (File eliminar : mirar.listFiles()) {
						eliminar.delete();// Borra los archivos que haya dentro de mirar.
					}
				}
				mirar.delete();// Borra los "hijos" de a.
			}
		}
		rm.delete();// Borra el directorio en sí

		return true;
	}

	public static void ls() {
		File[] listado = MiniTerminal.getWd().listFiles();
		Arrays.sort(listado);
		if (listado == null || listado.length == 0) {
			System.out.println("Sorry, but this directory/file is empty.");
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

	public static void ls(String arg) {
		if (!arg.startsWith("/")) {
			arg = relToAbs(arg);
		}
		File a = new File(arg);
		File[] listado = a.listFiles();
		Arrays.sort(listado);
		if (listado == null || listado.length == 0) {
			System.out.println("Sorry, but this directory/file is empty.");
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

	public static void ll() {
		File a = new File(System.getProperty("user.home"));
		SimpleDateFormat fechaMod = new SimpleDateFormat();
		File[] listado = a.listFiles();
		Arrays.sort(listado);
		if (listado == null || listado.length == 0) {
			System.out.println("Sorry, but this directory/file is empty.");
			return;
		} else {
			for (int i = 0; i < listado.length; i++) {
				if (listado[i].isDirectory())
					System.out.println(listado[i].length() + "bytes,  " + fechaMod.format(listado[i].lastModified())
							+ "   " + listado[i].getName() + "/");
				else
					System.out.println(listado[i].length() + "bytes  " + fechaMod.format(listado[i].lastModified())
							+ "   " + listado[i].getName());
			}
		}
	}

	public static void ll(String arg) throws Exception {
		if (!arg.startsWith("/")) {
			arg = relToAbs(arg);
		}
		File a = new File(arg);
		SimpleDateFormat fechaMod = new SimpleDateFormat();
		if (!a.exists()) {
			throw new Exception("No such file or directory.");
		}
		File[] listado = a.listFiles();
		Arrays.sort(listado);
		if (listado == null || listado.length == 0) {
			System.out.println("Sorry, but this directory/file is empty.");
			return;
		} else {
			for (int i = 0; i < listado.length; i++) {
				if (listado[i].isDirectory())
					System.out.println(listado[i].length() + "bytes,  " + fechaMod.format(listado[i].lastModified())
							+ "   " + listado[i].getName() + "/");
				else
					System.out.println(listado[i].length() + "bytes  " + fechaMod.format(listado[i].lastModified())
							+ "   " + listado[i].getName());
			}
		}
	}

	public static String relToAbs(String relPath) {
		File parentFolder = new File(MiniTerminal.getWd().getAbsolutePath());
		File Absolute = new File(parentFolder, relPath);
		return Absolute.getAbsolutePath();
	}
}
