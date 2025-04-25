package com.parking.vehicles;

public class EVCar extends Car {
    private final double chargingRate;

    public EVCar(String licensePlate, double chargingRate) {
        super(licensePlate);
        if (chargingRate <= 0) {
            throw new IllegalArgumentException("Charging rate must be positive");
        }
        this.chargingRate = chargingRate;
    }

    @Override
    public double getRatePerHour() {
        return super.getRatePerHour() + chargingRate;
    }

    public double getChargingRate() {
        return chargingRate;
    }
}
