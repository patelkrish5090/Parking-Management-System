package com.parking.vehicles;

public abstract class Vehicle {
    private final String licensePlate;
    private final VehicleType type;

    public Vehicle(String licensePlate, VehicleType type) {
        if (licensePlate == null || licensePlate.trim().isEmpty()) {
            throw new IllegalArgumentException("License plate cannot be null or empty");
        }
        this.licensePlate = licensePlate.trim();
        this.type = type;
    }

    public abstract double getRatePerHour();

    public String getLicensePlate() {
        return licensePlate;
    }

    public VehicleType getType() {
        return type;
    }

    @Override
    public String toString() {
        return type + " [" + licensePlate + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return licensePlate.equals(vehicle.licensePlate) && type == vehicle.type;
    }

    @Override
    public int hashCode() {
        return 31 * licensePlate.hashCode() + type.hashCode();
    }
}