package com.parking.payment;

public class UPIProcessor implements PaymentProcessor {
    @Override
    public boolean pay(double amount) {
        // Simulate UPI processing
        System.out.printf("Processing UPI payment of $%.2f\n", amount);
        return Math.random() > 0.05; // 95% success rate for demo
    }
}
