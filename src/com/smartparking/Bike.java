package com.smartparking;

public class Bike extends Vehicle {
    public Bike(String type, String licensePlate) {
        super(type, licensePlate);
    }

    @Override
    public void displayInfo() {
        System.out.println("Bike License: " + licensePlate);
    }
}
