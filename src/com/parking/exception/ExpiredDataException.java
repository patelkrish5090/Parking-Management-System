package com.parking.exception;

import java.time.LocalDateTime;

public class ExpiredDataException extends ParkingException {
    public ExpiredDataException(String message) {
        super(message);
    }

    public ExpiredDataException(String dataType, LocalDateTime expiry) {
        super(dataType + " expired at " + expiry);
    }
}