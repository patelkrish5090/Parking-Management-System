package com.parking.exception;

public class PaymentFailedException extends ParkingException {
    private final double amount;

    public PaymentFailedException(String message, double amount) {
        super(message);
        this.amount = amount;
    }
}
