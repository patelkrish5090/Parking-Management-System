package com.parking.exception;

public class InvalidReservationException extends ParkingException {
    public InvalidReservationException(String message) {
        super(message);
    }

    public InvalidReservationException(String reservationId, String reason) {
        super("Invalid reservation " + reservationId + ": " + reason);
    }
}