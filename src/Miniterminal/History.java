package Miniterminal;

import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class History {

	public static boolean hashistory = false;
	private static File history = new File(System.getProperty("user.home") + ".minihistory");
    private static ArrayList<String> historyList = new ArrayList<String>();

	
    public History() {
        Toolkit defaultTookKit = Toolkit.getDefaultToolkit();
        defaultTookKit.addAWTEventListener(new AWTEventListener() {
              @Override
			public void eventDispatched(AWTEvent event) {
                    System.out.println("HELLO");
              }
        }, AWTEvent.KEY_EVENT_MASK);
        
        AWTEventListener[] listeners = defaultTookKit.getAWTEventListeners();
        System.out.println(listeners.length + " test");
  }
    
	public static void checkHistory() throws IOException {
		if (System.getProperty("os.name").contains("Windows")) {
			history = new File(System.getProperty("user.home") + "\\.minihistory");
		}
		if (!history.exists()) {
			System.out.println(history);
			history.createNewFile();
			hashistory = true;
		} else {
			getHistory();
			hashistory = true;
		}
	}

	public static void addHistory(String command) {
	    try {
	        FileWriter myWriter = new FileWriter(history, true);
	        myWriter.write(command + "\n");
	        myWriter.close();
	        historyList.add(command);
	      } catch (IOException e) {
	        System.out.println(MiniTerminal.errPrefix + "An error occurred saving the historial." + Colorize.ANSI_RESET);
	      }
	}
	
	public static void getHistory() throws IOException {
		try {
	    	 Scanner reader = new Scanner(history);
	         while (reader.hasNextLine()) {
	           String data = reader.nextLine();
	           historyList.add(data);
	         }
	         reader.close();
	      } catch (IOException e) {
	        System.out.println(MiniTerminal.errPrefix + "An error occurred reading the historial." + Colorize.ANSI_RESET);
	      }
	}
	
}
