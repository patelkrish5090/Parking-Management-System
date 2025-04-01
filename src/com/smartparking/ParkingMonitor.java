package com.smartparking;

public class ParkingMonitor implements Runnable {
    private ParkingLot parkingLot;

    public ParkingMonitor(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("[Monitor] Available slots: " + parkingLot.getAvailableSlots());
            try {
                Thread.sleep(5000); // Sleep for 5 seconds
            } catch (InterruptedException e) {
                System.out.println("Monitor interrupted.");
            }
        }
    }
}
