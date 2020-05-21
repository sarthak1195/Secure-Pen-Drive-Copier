package usbdrivedectector.detectors;

import usbdrivedectector.USBStorageDevice;
import usbdrivedectector.process.CommandExecutor;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class WindowsStorageDeviceDetector extends AbstractStorageDeviceDetector {
    

    /**
     * wmic logicaldisk where drivetype=2 get description,deviceid,volumename
     */
    private static final String CMD_WMI_USB = "wmic logicaldisk where drivetype=2 get deviceid";
    public static String Path;
    
    protected WindowsStorageDeviceDetector() {
        super();
    }

    @Override
    public List<USBStorageDevice> getStorageDevicesDevices() {
        final ArrayList<USBStorageDevice> listDevices = new ArrayList<>();

        try (CommandExecutor commandExecutor = new CommandExecutor(CMD_WMI_USB)){
            commandExecutor.processOutput((String outputLine) -> {
                if (!outputLine.isEmpty() && !"DeviceID".equals(outputLine)) {
                    String rootPath = outputLine + File.separatorChar;
                    WindowsStorageDeviceDetector.Path = rootPath;                    
                    listDevices.add(getUSBDevice(rootPath, getDeviceName(rootPath)));
                }
            });

        } catch (IOException e) {            
        }

        return listDevices;
    }

    private String getDeviceName(String rootPath) {
        final File f = new File(rootPath);
        final FileSystemView v = FileSystemView.getFileSystemView();
        String name = v.getSystemDisplayName(f);

        if (name != null) {
            int idx = name.lastIndexOf('(');
            if (idx != -1) {
                name = name.substring(0, idx);
            }

            name = name.trim();
            if (name.isEmpty()) {
                name = null;
            }
        }
        return name;
    }

    /**
     * Returns the list of the removable devices actually connected to the computer. <br/>
     * This method was effectively tested on:
     * <ul>
     * <li>Windows 7 (English)</li>
     * </ul>
     *
     * @deprecated replaced by {@link #getWindowsRemovableDevicesCommand()}
     *
     * @return the list of removable devices
     */
//    @SuppressWarnings("unused")
//    private ArrayList<USBStorageDevice> getWindowsRemovableDevicesList() {
//
//        /**
//         * TODO: How to put this working in all languages?
//         */
//        String fileSystemDesc = "Removable Disk";
//
//        ArrayList<USBStorageDevice> listDevices = new ArrayList<USBStorageDevice>();
//
//        File[] roots = File.listRoots();
//
//        if (roots == null) {
//            // TODO: raise an error?
//            return listDevices;
//        }
//
//        for (File root : roots) {
//            if (root.canRead() && root.canWrite() && fsView.isDrive(root)
//                    && !fsView.isFloppyDrive(root)) {
//
//                if (fileSystemDesc.equalsIgnoreCase(fsView
//                        .getSystemTypeDescription(root))) {
//                    USBStorageDevice device = new USBStorageDevice(root,
//                            fsView.getSystemDisplayName(root));
//                    listDevices.add(device);
//                }
//
//                System.out.println(fsView.getSystemDisplayName(root) + " - "
//                        + fsView.getSystemTypeDescription(root));
//
//                /*
//                 * FileSystemView.getSystemTypeDescription();
//                 * 
//                 * Windows (8): Windows (7): "Removable Disk" Windows (XP):
//                 * Linux (Ubuntu): OSX (10.7):
//                 */
//            }
//        }
//
//        return listDevices;
//    }
}
