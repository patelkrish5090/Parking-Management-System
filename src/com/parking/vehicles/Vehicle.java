package com.parking.vehicles;

public abstract class Vehicle {
    private final String licensePlate;
    private final VehicleType type;

    /**
     * Constructs a new Vehicle
     * @param licensePlate The vehicle's license plate (cannot be null or empty)
     * @param type The vehicle type (from VehicleType enum)
     * @throws IllegalArgumentException if license plate is invalid
     */
    public Vehicle(String licensePlate, VehicleType type) {
        if (licensePlate == null || licensePlate.trim().isEmpty()) {
            throw new IllegalArgumentException("License plate cannot be null or empty");
        }
        this.licensePlate = licensePlate.trim();
        this.type = type;
    }

    /**
     * Gets the hourly parking rate for this vehicle type
     * @return Rate per hour in currency units
     */
    public abstract double getRatePerHour();

    /**
     * Gets the vehicle's license plate
     * @return license plate string
     */
    public String getLicensePlate() {
        return licensePlate;
    }

    /**
     * Gets the vehicle type
     * @return VehicleType enum value
     */
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