package com.parking.exception;

public class SlotStateException extends ParkingException {
    public SlotStateException(String message) {
        super(message);
    }

    public SlotStateException(String slotCode, boolean expectedOccupied) {
        super("Slot " + slotCode + " should be " +
                (expectedOccupied ? "occupied" : "free"));
    }
}