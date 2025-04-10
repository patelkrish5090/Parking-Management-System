package com.smartparking;

public class Truck extends Vehicle {
    public Truck(String type, String licensePlate, boolean pass) {
        super(type, licensePlate, pass);
    }

    @Override
    public void displayInfo() {
        System.out.println("Truck License: " + licensePlate);
    }
}
