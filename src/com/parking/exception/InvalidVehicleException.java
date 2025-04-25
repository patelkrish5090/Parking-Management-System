package com.parking.exception;

public class InvalidVehicleException extends ParkingException {
    public InvalidVehicleException(String message) {
        super(message);
    }

    public InvalidVehicleException(String licensePlate, String reason) {
        super("Invalid vehicle " + licensePlate + ": " + reason);
    }
}