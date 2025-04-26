package com.parking.payment;

public class CashProcessor implements PaymentProcessor {
    @Override
    public boolean pay(double amount) {
        System.out.printf("Accepting cash payment of $%.2f\n", amount);
        return true;
    }
}