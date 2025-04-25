package com.parking.exception;

public class InvalidUserException extends ParkingException {
    public InvalidUserException(String message) {
        super(message);
    }

    public InvalidUserException(String userId, String reason) {
        super("Invalid user " + userId + ": " + reason);
    }
}
