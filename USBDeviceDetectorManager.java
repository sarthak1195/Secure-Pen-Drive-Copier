package usbdrivedectector;


import usbdrivedectector.detectors.AbstractStorageDeviceDetector;
import usbdrivedectector.events.DeviceEventType;
import usbdrivedectector.events.IUSBDriveListener;
import usbdrivedectector.events.USBStorageEvent;

import java.io.IOException;
import java.util.*;


public class USBDeviceDetectorManager {
   

    /**
     * The default pooling interval is 10 seconds
     */
    private static final long DEFAULT_POLLING_INTERVAL = 10000;

    private long currentPollingInterval = DEFAULT_POLLING_INTERVAL;

    private final Set<USBStorageDevice> connectedDevices;
    private final ArrayList<IUSBDriveListener> listeners;
    private ListenerTask listenerTask;


    public USBDeviceDetectorManager() {
        this(DEFAULT_POLLING_INTERVAL);
    }

    /**
     * Creates a new USBDeviceDetectorManager
     * 
     * The polling interval is used as the update frequency for any attached
     * listeners.      
     * 
     * Polling doesn't happen until at least one listener is attached.
     *
     * @param pollingInterval the interval in milliseconds to poll for the USB
     *                        storage devices on the system.
     */
    public USBDeviceDetectorManager(long pollingInterval) {
        listeners = new ArrayList<>();
        connectedDevices = new HashSet<>();

        currentPollingInterval = pollingInterval;
    }

    /**
     * Sets the polling interval
     *
     * @param pollingInterval the interval in milliseconds to poll for the USB
     *                        storage devices on the system.
     */
    public synchronized void setPoolingInterval(final long pollingInterval) {        

        currentPollingInterval = pollingInterval;

        if (listeners.size() > 0) {
            stop();
            start();
        }
    }

    /**
     * Start polling to update listeners
     * <p>
     * This method only needs to be called if {@link #stop() stop()} has been
     * called after listeners have been added.
     * </p>
     */
    private synchronized void start() {
        if (listenerTask == null) {
            listenerTask = new ListenerTask(currentPollingInterval);
            listenerTask.start();
        }
    }

    /**
     * Forces the polling to stop, even if there are still listeners attached
     */
    private synchronized void stop() {
        if (listenerTask != null) {
            listenerTask.interrupt();
            listenerTask = null;
        }
    }

    /**
     * Adds an IUSBDriveListener.
     *
     * The polling timer is automatically started as needed when a listener is
     * added.
     * 
     *
     * @param listener the listener to be updated with the attached drives
     * @return true if the listener was not in the list and was successfully
     * added
     */
    public synchronized boolean addDriveListener(final IUSBDriveListener listener) {
        if (listeners.contains(listener)) {
            return false;
        }

        listeners.add(listener);
        start();
        return true;
    }

    /**
     * Removes an IUSBDriveListener.
     * <p>
     * The polling timer is automatically stopped if this is the last listener
     * being removed.
     * </p>
     *
     * @param listener the listener to remove
     * @return true if the listener existed in the list and was successfully
     * removed
     */
    public synchronized boolean removeDriveListener(final IUSBDriveListener listener) {
        boolean removed = listeners.remove(listener);
        if (listeners.isEmpty()) {
            stop();
        }

        return removed;
    }

    /**
     * Gets a list of currently attached USB storage devices.
     * <p>
     * This method has no effect on polling or listeners being updated
     * </p>
     *
     * @return list of attached USB storage devices.
     */
    public List<USBStorageDevice> getRemovableDevices() {
        return AbstractStorageDeviceDetector.getInstance().getStorageDevicesDevices();
    }

    /**
     * Updates the internal state of this manager and sends
     *
     * @param currentConnectedDevices a list with the currently connected USB storage devices
     */
    private void updateConnectedDevices(final List<USBStorageDevice> currentConnectedDevices) {
        final List<USBStorageDevice> removedDevices = new ArrayList<>();
        final List<USBStorageDevice> newDevices = currentConnectedDevices;

        synchronized (this) {
            final Iterator<USBStorageDevice> itConnectedDevices = connectedDevices.iterator();

            while (itConnectedDevices.hasNext()) {
                USBStorageDevice device = itConnectedDevices.next();

                if (currentConnectedDevices.contains(device)) {
                    newDevices.remove(device);
                } else {                   
                	removedDevices.add(device);
                    
                    itConnectedDevices.remove();
                }
            }

            connectedDevices.addAll(newDevices);
        }

        newDevices.forEach(device -> sendEventToListeners(new USBStorageEvent(device, DeviceEventType.CONNECTED)));
        removedDevices.forEach(device -> sendEventToListeners(new USBStorageEvent(device, DeviceEventType.REMOVED)));
    }

    private void sendEventToListeners(USBStorageEvent event) {
        /*
         Make this thread safe, so we deal with a copy of listeners so any 
         listeners being added or removed don't cause a ConcurrentModificationException.
         Also allows listeners to remove themselves while processing the event
         */
        ArrayList<IUSBDriveListener> listenersCopy;
        synchronized (listeners) {
            listenersCopy = (ArrayList<IUSBDriveListener>) listeners.clone();
        }

        for (IUSBDriveListener listener : listenersCopy) {
            try {
                listener.usbDriveEvent(event);
            } catch (Exception ex) {                
            }
        }
    }

    private class ListenerTask extends Thread {

        private final long pollingInterval;

        public ListenerTask(final long pollingInterval) {
            this.pollingInterval = pollingInterval;

            setDaemon(true);
        }

        @Override
        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    try {                        

                        List<USBStorageDevice> actualConnectedDevices = AbstractStorageDeviceDetector.getInstance().getStorageDevicesDevices();

                        updateConnectedDevices(actualConnectedDevices);

                    } catch (Exception e) {                        
                    }

                    sleep(pollingInterval);
                }
            } catch (InterruptedException ex) {                
            }
        }

    }
}
