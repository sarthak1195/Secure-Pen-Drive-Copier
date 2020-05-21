package usbdrivedectector.detectors;

import usbdrivedectector.USBStorageDevice;
import usbdrivedectector.process.CommandExecutor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OSXStorageDeviceDetector extends AbstractStorageDeviceDetector {
    

    /**
     * system_profiler SPUSBDataType | grep "BSD Name:\|Mount Point:"
     */
    private static final String CMD_SYSTEM_PROFILER_USB = "system_profiler SPUSBDataType";
    private static final Pattern macOSXPattern = Pattern.compile("^.*Mount Point: (.+)$");

    protected OSXStorageDeviceDetector() {
        super();
    }

    @Override
    public List<USBStorageDevice> getStorageDevicesDevices() {
        final ArrayList<USBStorageDevice> listDevices = new ArrayList<>();

        try (CommandExecutor commandExecutor = new CommandExecutor(CMD_SYSTEM_PROFILER_USB)){
            commandExecutor.processOutput((String outputLine) -> {
                final Matcher matcher = macOSXPattern.matcher(outputLine);

                if (matcher.matches()) {
                    listDevices.add(getUSBDevice(matcher.group(1)));
                }
            });

        } catch (IOException e) {            
        }

        return listDevices;
    }
}
