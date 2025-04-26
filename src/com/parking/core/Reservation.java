package com.parking.core;

import com.parking.users.User;
import com.parking.vehicles.Vehicle;

import java.time.LocalDateTime;


public class Reservation {
    private final String reservationId;
    private final ParkingSlot slot;
    private final Vehicle vehicle;
    private final User user;
    private final LocalDateTime checkIn;
    private LocalDateTime checkOut;

    public Reservation(String reservationId, ParkingSlot slot, Vehicle vehicle, User user, LocalDateTime checkIn) {
        this.reservationId = reservationId;
        this.slot = slot;
        this.vehicle = vehicle;
        this.user = user;
        this.checkIn = checkIn;
    }

    public ParkingSlot getSlot() {
        return slot;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public User getUser() {
        return user;
    }

    public LocalDateTime getCheckIn() {
        return checkIn;
    }

    public LocalDateTime getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDateTime checkOut) {
        if (checkOut.isBefore(checkIn)) {
            throw new IllegalArgumentException("Check-out time cannot be before check-in");
        }
        this.checkOut = checkOut;
    }
}
