package com.smartparking;

public abstract class Vehicle {
    protected String type;
    protected String licensePlate;

    public Vehicle(String type, String licensePlate) {
        this.type = type;
        this.licensePlate = licensePlate;
    }

    public abstract void displayInfo();
}
