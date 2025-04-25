package com.parking.vehicles;

public class Truck extends Vehicle {
    private static final double BASE_RATE = 30.0;

    public Truck(String licensePlate) {
        super(licensePlate, VehicleType.TRUCK);
    }

    @Override
    public double getRatePerHour() {
        return BASE_RATE;
    }
}