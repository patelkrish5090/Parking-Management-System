package com.smartparking;

public abstract class Vehicle {
    protected String type;
    protected String licensePlate;
    protected boolean pass;

    public Vehicle(String type, String licensePlate, boolean pass) {
        this.type = type;
        this.licensePlate = licensePlate;
        this.pass = pass;
    }

    public boolean isPass(){
        return pass;
    }

    public String getLicensePlate(){
        return licensePlate;
    }

    public abstract void displayInfo();
}
