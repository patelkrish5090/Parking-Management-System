package com.parking.vehicles;

public class Car extends Vehicle {
    private static final double BASE_RATE = 20.0;

    public Car(String licensePlate) {
        super(licensePlate, VehicleType.CAR);
    }

    @Override
    public double getRatePerHour() {
        return BASE_RATE;
    }
}