package usbdrivedectector.events;

import usbdrivedectector.USBStorageDevice;


public class USBStorageEvent {
    private final USBStorageDevice storageDevice;
    private static DeviceEventType eventType;
    
    public USBStorageEvent(final USBStorageDevice storageDevice, final DeviceEventType eventType) {
        this.storageDevice = storageDevice;
        this.eventType = eventType;
    }
    
    public USBStorageDevice getStorageDevice() {
        return storageDevice;
    }

    public static DeviceEventType getEventType() {
        return eventType;
    }

    @Override
    public String toString() {
        return "USBStorageEvent{" + "storageDevice=" + storageDevice + ", eventType=" + eventType + '}';
    }
}
