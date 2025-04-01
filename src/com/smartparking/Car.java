package com.smartparking;

public class Car extends Vehicle {
    public Car(String type, String licensePlate) {
        super(type, licensePlate);
    }

    @Override
    public void displayInfo() {
        System.out.println("Car License: " + licensePlate);
    }
}
