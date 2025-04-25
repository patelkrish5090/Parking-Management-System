package com.parking.payment;

public class CashProcessor implements PaymentProcessor {
    @Override
    public boolean pay(double amount) {
        // Cash payments always succeed
        System.out.printf("Accepting cash payment of $%.2f\n", amount);
        return true;
    }
}