package com.parking.payment;

public class CreditCardProcessor implements PaymentProcessor {
    @Override
    public boolean pay(double amount) {
        // Simulate credit card processing
        System.out.printf("Processing credit card payment of $%.2f\n", amount);
        return Math.random() > 0.1; // 90% success rate for demo
    }
}