package com.parking.payment;

public class CreditCardProcessor implements PaymentProcessor {
    @Override
    public boolean pay(double amount) {

        System.out.printf("Processing credit card payment of $%.2f\n", amount);
        return true;
    }
}