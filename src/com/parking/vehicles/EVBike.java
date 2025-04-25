package com.parking.vehicles;

public class EVBike extends Bike {
    private final double chargingRate;

    public EVBike(String licensePlate, double chargingRate) {
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
