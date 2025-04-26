package com.parking.payment;

public interface PaymentProcessor {
    
    boolean pay(double amount);
}