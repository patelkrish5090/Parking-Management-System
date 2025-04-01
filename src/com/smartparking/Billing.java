package com.smartparking;

public class Billing {
    private double rate;
    private int hours;

    // Overloaded constructors
    public Billing() {
        this.rate = 10.0;
        this.hours = 1;
    }

    public Billing(double rate, int hours) {
        this.rate = rate;
        this.hours = hours;
    }

    // Overloaded method 1: calculate bill normally
    public double calculateBill() {
        return rate * hours;
    }

    // Overloaded method 2: calculate bill with discount
    public double calculateBill(double discount) {
        double bill = rate * hours;
        return bill - discount;
    }

    // Vararg overloading method 1: calculate total bill for multiple sessions
    public double calculateMultipleBills(double... hoursArray) {
        double total = 0;
        for (double h : hoursArray) {
            total += rate * h;
        }
        return total;
    }

    // Vararg overloading method 2: calculate total bill for different rates and hours
    public double calculateMultipleBills(double[] rates, double... hoursArray) {
        if (rates.length != hoursArray.length) {
            throw new IllegalArgumentException("Rates and hours must have same length.");
        }
        double total = 0;
        for (int i = 0; i < rates.length; i++) {
            total += rates[i] * hoursArray[i];
        }
        return total;
    }
}
