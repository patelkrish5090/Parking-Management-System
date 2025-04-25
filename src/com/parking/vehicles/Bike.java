package com.parking.vehicles;

public class Bike extends Vehicle {
    private static final double BASE_RATE = 10.0;

    public Bike(String licensePlate) {
        super(licensePlate, VehicleType.BIKE);
    }

    @Override
    public double getRatePerHour() {
        return BASE_RATE;
    }
}