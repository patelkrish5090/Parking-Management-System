package com.parking.payment;

public class UPIProcessor implements PaymentProcessor {
    @Override
    public boolean pay(double amount) {

        System.out.printf("Processing UPI payment of $%.2f\n", amount);
        return true;
    }
}
