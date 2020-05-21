package usbdrivedectector;

import java.io.*;
import usbdrivedectector.detectors.WindowsStorageDeviceDetector;

public class DriveProperties {

	private final static String Hide_Contents = "attrib +s +h ";
	private final static String Unhide_Contents = "attrib -s -h ";
	private final static String Path = WindowsStorageDeviceDetector.Path;
	private static Process process;
	private static String[] directories;
	
	public static void DirectoryList(){
		
		File file = new File(Path);
		directories = file.list(new FilenameFilter() {
		  @Override
		  public boolean accept(File current, String name) {
		    return new File(current, name).isDirectory();
		  }
		});
		
	}
	
	public static void HideContents() throws IOException{
		process = Runtime.getRuntime().exec(Hide_Contents + Path + "*");		
		DirectoryList();
		for(int i = 0; i<directories.length; i++){						
			if(directories[i].equals("System Volume Information"))
				continue;		
			process = Runtime.getRuntime().exec(Hide_Contents + Path + '"' +directories[i] + '"');			
		}
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {			
			e.printStackTrace();
		}
		process = Runtime.getRuntime().exec(Unhide_Contents + Path + "USBDetector.jar");
	}
	
	public static void UnhideContents() throws IOException {		
		process = Runtime.getRuntime().exec(Unhide_Contents + Path + "*");
		DirectoryList();
		for(int i = 0; i<directories.length; i++){						
			if(directories[i].equals("System Volume Information"))
				continue;		
			process = Runtime.getRuntime().exec(Unhide_Contents + Path + '"' +directories[i] + '"');			
		}
		process = Runtime.getRuntime().exec(Hide_Contents + Path + "accounts.txt");
	}
}
