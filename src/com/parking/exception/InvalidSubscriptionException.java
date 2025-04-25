package com.parking.exception;

public class InvalidSubscriptionException extends ParkingException {
    public InvalidSubscriptionException(String message) {
        super(message);
    }

    public InvalidSubscriptionException(String subscriptionCode, String reason) {
        super("Invalid subscription " + subscriptionCode + ": " + reason);
    }
}
