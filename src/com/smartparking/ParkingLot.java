package com.smartparking;

import com.smartparking.exceptions.ParkingFullException;
import java.util.ArrayList;
import java.util.List;

public class ParkingLot {
    private List<ParkingSlot> slots;
    private int capacity;
    private IOhandler file;

    public ParkingLot(int capacity) {
        this.capacity = capacity;
        slots = new ArrayList<>();
        // Initialize parking slots
        for (int i = 1; i <= capacity; i++) {
            slots.add(new ParkingSlot(i));
        }
    }

    public synchronized ParkingSlot assignSlot(Vehicle vehicle) throws ParkingFullException {
        for (ParkingSlot slot : slots) {
            if (!slot.isOccupied()) {
                slot.assignVehicle(vehicle);
                file.logTime(vehicle.getLicensePlate(), true);
                return slot;
            }
        }
        throw new ParkingFullException("Parking Lot is Full!");
    }

    public synchronized int getAvailableSlots() {
        int count = 0;
        for (ParkingSlot slot : slots) {
            if (!slot.isOccupied()) {
                count++;
            }
        }
        return count;
    }

    public List<ParkingSlot> getSlots(){
        return slots;
    }
    // Nested static class for Parking Slot
    public static class ParkingSlot {
        private int slotNumber;
        private Vehicle parkedVehicle;

        public ParkingSlot(int slotNumber) {
            this.slotNumber = slotNumber;
        }

        public int getSlotNumber() {
            return slotNumber;
        }

        public boolean isOccupied() {
            return parkedVehicle != null;
        }

        public void assignVehicle(Vehicle vehicle) {
            this.parkedVehicle = vehicle;
        }

        public void removeVehicle() {
            this.parkedVehicle = null;
        }

        public Vehicle getParkedVehicle(){
            return parkedVehicle;
        }
    }
}
