package com.parking.exception;

import com.parking.vehicles.VehicleType;

public class NoAvailableSlotException extends ParkingException {
    public NoAvailableSlotException(String message) {
        super(message);
    }
}
