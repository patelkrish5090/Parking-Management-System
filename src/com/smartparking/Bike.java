package com.smartparking;

public class Bike extends Vehicle {
    public Bike(String type, String licensePlate, boolean pass) {
        super(type, licensePlate, pass);
    }

    @Override
    public void displayInfo() {
        System.out.println("Bike License: " + licensePlate);
    }
}
