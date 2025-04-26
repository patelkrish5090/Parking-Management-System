package com.parking.payment;

public class EWalletProcessor implements PaymentProcessor {
    @Override
    public boolean pay(double amount) {

        System.out.printf("Processing e-wallet payment of $%.2f\n", amount);
        return true;
    }
}
