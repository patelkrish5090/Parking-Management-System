package com.parking.payment;

public interface PaymentProcessor {
    /**
     * Processes a payment transaction
     * @param amount Amount to charge
     * @return true if payment succeeded
     */
    boolean pay(double amount);
}