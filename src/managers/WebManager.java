package managers;

/* *
 * 
 * This class contains the definition of all the commands 
 * that have something to do with Internet. 
 * 
 * 
 * MiniTerminal Java 
 * by @alejandrofan2 | @masterprog-cmd 
 * 
 * */


import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import miniTerminal.Colorize;
import miniTerminal.MiniTerminal;

public class WebManager {
	
	// Command to download files from web server.
	public static void wget(String arg) throws Exception {
		try {
			// Make a URL to the web page.
			URL url = new URL(arg);

			// Get the input stream through URL Connection.
			URLConnection con = url.openConnection();
			InputStream is = con.getInputStream();

			// Once you have the Input Stream, it's just plain old Java IO stuff.
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = null;

			// Read each line and write to System.out.
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}

		} catch (MalformedURLException e) {
			System.out.println("La URL " + arg + "no existe");
		} catch (IOException e) {
			e.getMessage();
		}
	}
	
	public static boolean wget(String link, String file) throws Exception {
		try {
			// Make a URL to the web page.
			URL url = new URL(link);
			
			// Creates the file
			FileManager.touch(file);
			
			// Get the input stream through URL Connection.
			URLConnection con = url.openConnection();
			InputStream is = con.getInputStream();

			// Once you have the Input Stream, it's just plain old Java IO stuff.
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = null;

			// Read each line and write to System.out.
			try {
			     FileWriter myWriter = new FileWriter(FileManager.relToAbs(file));
			     while ((line = br.readLine()) != null) {
				      myWriter.write(line);
			     }
			      myWriter.close();
			} catch (IOException e) {
				 System.out.println(MiniTerminal.ERRPREFIX + "An error occurred writting the file." + Colorize.ANSI_RESET);
			}
		} catch (MalformedURLException e) {
			System.out.println(MiniTerminal.ERRPREFIX + "La URL " + link + "no existe" + Colorize.ANSI_RESET);
		} catch (IOException e) {
			e.getMessage();
		}
		return false;
	}
}
