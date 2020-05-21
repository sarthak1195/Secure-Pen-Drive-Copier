package usbdrivedectector.events;

/**
 * Type of events that occur to USB Storage devices.
 */
public enum DeviceEventType {
    /**
     * A device has been removed.
     */
    REMOVED,

    /**
     * A new device has been connected.
     */
    CONNECTED
}
