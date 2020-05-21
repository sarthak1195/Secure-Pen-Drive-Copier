package usbdrivedectector;

import usbdrivedectector.events.DeviceEventType;
import usbdrivedectector.events.IUSBDriveListener;
import usbdrivedectector.events.USBStorageEvent;
import java.io.IOException;

public class SimpleTest implements IUSBDriveListener {
	public static String ts;

	public static void main(String[] args) throws IOException, InterruptedException {    	
        System.out.println("Start Test");
		USBDeviceDetectorManager driveDetector = new USBDeviceDetectorManager();

        driveDetector.getRemovableDevices().forEach(System.out::println);        
        
        SimpleTest sTest = new SimpleTest();
        Thread.sleep(1000); 
        driveDetector.addDriveListener(sTest);
        //ts=DataLogging.getTimeStamp();
       	LoginProgram lp=new LoginProgram();       	                  

        //System.out.println("Test finished");       	
       	
	}

	private SimpleTest() throws IOException {
		//DriveProperties.HideContents();
	}

	@Override
	public void usbDriveEvent(USBStorageEvent event) {
		System.out.println(event);
	}
}
