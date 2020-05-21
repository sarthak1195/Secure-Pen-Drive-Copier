package usbdrivedectector.detectors;

import usbdrivedectector.USBStorageDevice;
import usbdrivedectector.process.CommandExecutor;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LinuxStorageDeviceDetector extends AbstractStorageDeviceDetector {   

    private static final String CMD_DF = "df";
    private static final Pattern command1Pattern = Pattern.compile("^(\\/[^ ]+)[^%]+%[ ]+(.+)$");

    private static final String CMD_CHECK_USB = "udevadm info -q property -n ";
    private static final String strDeviceVerifier = "ID_USB_DRIVER=usb-storage";

    protected LinuxStorageDeviceDetector() {
        super();
    }

    private boolean isUSBStorage(String device) {
        String verifyCommand = CMD_CHECK_USB + device;

        try (CommandExecutor commandExecutor = new CommandExecutor(verifyCommand)){
//            return commandExecutor.checkOutput((String outputLine) -> strDeviceVerifier.equals(outputLine));
            return commandExecutor.checkOutput(strDeviceVerifier::equals);
        } catch (IOException e) {            
        }

        return false;
    }

    @Override
    public List<USBStorageDevice> getStorageDevicesDevices() {
        final ArrayList<USBStorageDevice> listDevices = new ArrayList<>();

        try (CommandExecutor commandExecutor = new CommandExecutor(CMD_DF)){
            commandExecutor.processOutput((String outputLine) -> {
                Matcher matcher = command1Pattern.matcher(outputLine);

                if (matcher.matches()) {
                    String device = matcher.group(1);
                    String rootPath = matcher.group(2);

                    if (isUSBStorage(device)) {
                        listDevices.add(getUSBDevice(rootPath));
                    }
                }
            });

        } catch (IOException e) {            
        }

        return listDevices;
    }
}
