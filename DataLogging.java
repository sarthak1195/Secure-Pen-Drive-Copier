package usbdrivedectector;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataLogging {
	
	DataLogging() {
		try {			
			String text = null;
			text.concat("Serial Number: ");
			text.concat(serialNumber.HDD_SerialNo());
			text.concat("System Name :");
			text.concat(getUsername());
			text.concat("System Mac Address: ");
			byte[] mac = getMacAddress();
			text.concat(mac.toString());
			text.concat("Connection timestamp:");
			text.concat(SimpleTest.ts);
			
			FileWriter fw = new FileWriter("log.txt", true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(text);
			bw.newLine();
			bw.flush();
			bw.close();

		} catch (IOException ioe) {
			System.out.println(ioe);
		}
	}

	static byte[] getMacAddress() {
		byte mac[] = null;
		try {
			InetAddress add = InetAddress.getLocalHost();
			NetworkInterface nwi = NetworkInterface.getByInetAddress(add);
			mac = nwi.getHardwareAddress();
			return mac;
		} catch (Exception e) {
			System.out.println(e);
		}
		return mac;
	}// getMacAdddress function

	static String getUsername() {
		String uname = System.getProperty("user.name");
		return uname;
	}// getUsername function

	static String getTimeStamp() {
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		return timeStamp;
	}

	/*public static void main(String[] args) {
		// TODO Auto-generated method stub

	}*/

}
