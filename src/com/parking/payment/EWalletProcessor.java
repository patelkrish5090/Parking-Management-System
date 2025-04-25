package com.parking.payment;

public class EWalletProcessor implements PaymentProcessor {
    @Override
    public boolean pay(double amount) {
        // Simulate e-wallet processing
        System.out.printf("Processing e-wallet payment of $%.2f\n", amount);
        return Math.random() > 0.15; // 85% success rate for demo
    }
}
