package com.smartparking;

public class Truck extends Vehicle {
    public Truck(String type, String licensePlate) {
        super(type, licensePlate);
    }

    @Override
    public void displayInfo() {
        System.out.println("Truck License: " + licensePlate);
    }
}
