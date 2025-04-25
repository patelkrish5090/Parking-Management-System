package com.parking.exception;

public class PaymentFailedException extends ParkingException {
    private final double amount;

    public PaymentFailedException(String message, double amount) {
        super(message);
        this.amount = amount;
    }

    public PaymentFailedException(String message, double amount, Throwable cause) {
        super(message, cause);
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }
}
