package usbdrivedectector.detectors;

import usbdrivedectector.USBStorageDevice;

import java.io.File;
import java.util.List;


public abstract class AbstractStorageDeviceDetector {
   

    private static final String OSName = System.getProperty("os.name")
            .toLowerCase();

    // private static final String OSVersion = System.getProperty("os.version");
    // private static final String OSArch = System.getProperty("os.arch");
    /**
     * {@link AbstractStorageDeviceDetector} instance. <br/>
     * This instance is created (Thread-Safe) when the JVM loads the class.
     */
    private static final AbstractStorageDeviceDetector instance;

    static {
        if (OSName.startsWith("win")) {
            instance = new WindowsStorageDeviceDetector();
        } else if (OSName.startsWith("linux")) {
            instance = new LinuxStorageDeviceDetector();
        } else if (OSName.startsWith("mac")) {
            instance = new OSXStorageDeviceDetector();
        } else {
            instance = null;
        }
    }

    public static AbstractStorageDeviceDetector getInstance() {
        if (instance == null) {
            throw new UnsupportedOperationException("Your Operative System (" + OSName + ") is not supported!");
        }

        return instance;
    }

    protected AbstractStorageDeviceDetector() {
    }

    /**
     * Returns the all storage devices currently connected to the computer.
     *
     * @return the list of the USB storage devices
     */
    public abstract List<USBStorageDevice> getStorageDevicesDevices();

    static USBStorageDevice getUSBDevice(final String rootPath) {
        return getUSBDevice(rootPath, null);
    }

    static USBStorageDevice getUSBDevice(final String rootPath, final String deviceName) {
        final File root = new File(rootPath);
        

        try {
            return new USBStorageDevice(root, deviceName);
        } catch (IllegalArgumentException e) {           
        }

        return null;
    }
}
