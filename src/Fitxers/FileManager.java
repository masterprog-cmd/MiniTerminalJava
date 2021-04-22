package Fitxers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;

public class FileManager {

	public void cd(File wd, String argumento) {
		if (argumento.equals(new File(argumento))) {
			wd = new File(argumento);
		} else if (argumento.equals("..")) {

		}
	}

	@SuppressWarnings("unlikely-arg-type")
	public boolean mkdir(String arg) {
		if (!arg.equals(new File(arg))) {
			File nuevoDir = new File(arg);
			return true;
		}
		return false;
	}

	public boolean rmArch(String path) throws FileNotFoundException {
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

	public void ls(String path) {
		File a = new File(path);
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
}
