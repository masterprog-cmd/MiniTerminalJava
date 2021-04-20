package Fitxers;
import java.io.*;

public class FileManager {
	
	public void cd(File wd, String argumento) {
		if(argumento.equals(new File(argumento))) {
				wd = new File(argumento);
		}else if(argumento.equals("..")) {
			
		}
	}
	
	@SuppressWarnings("unlikely-arg-type")
	public boolean mkdir(String arg) {
		if(!arg.equals(new File(arg))) {
			File nuevoDir = new File(arg);
			return true;
		}
		return false;
	}
	
	public void rmArch(File rm) {
		if(rm.isDirectory()) {
			rm.delete();
		}
	}
}
