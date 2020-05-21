package usbdrivedectector.events;

import java.io.IOException;

/**
 * Interface to implement by the classes who want to receive notifications when 
 * there are devices Connected or Removed of the computer.
 */
@FunctionalInterface
public interface IUSBDriveListener {
    
    void usbDriveEvent(USBStorageEvent event) throws IOException;
}
