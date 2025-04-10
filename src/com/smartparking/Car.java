package com.smartparking;

public class Car extends Vehicle {
    private Billing bill;
    private boolean pass;

    public Car(String type, String licensePlate, boolean pass) {
        super(type, licensePlate, pass);
        this.bill = new Billing(20, 1);
        this.pass = pass;
    }

    @Override
    public void displayInfo() {
        System.out.println("Car License: " + licensePlate);
    }

//    public double calcBill(){
//
//    }
}
