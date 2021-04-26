package Files;

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
		File dir = new File(arg);
		if (!dir.exists()) {
			throw new Exception();
		}
		if (dir.exists())
			MiniTerminal.setWd(dir);
		else
			throw new Exception("The directory does not exist.");
	}

	public static boolean mkdir(String arg) throws Exception {
		File a = new File(arg);
		if (!a.getParentFile().exists()) {
			throw new Exception();
		}
		if (!a.exists()) {
			if (a.mkdirs())
				return true;
		} else
			System.out.println("Directory doesn't created");
		return false;
	}

	public static boolean rm(String path) throws FileNotFoundException {
		File rm = new File(path);
		if (!rm.exists())
			throw new FileNotFoundException();
		if (rm.isDirectory()) {
			for (File mirar : rm.listFiles()) {
				if (mirar.isDirectory()) {
					for (File eliminar : mirar.listFiles()) {
						eliminar.delete();
					}
				}
				mirar.delete();
			}
		}
		rm.delete();

		return true;
	}

	public static void ls() throws Exception {
		File[] listado = MiniTerminal.getWd().listFiles();
		if (!MiniTerminal.getWd().exists()) {
			throw new Exception();
		}
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

	public static void ll() throws Exception {
		File a = MiniTerminal.getWd();
		if (!a.exists()) {
			throw new Exception();
		}
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
			throw new Exception();
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
