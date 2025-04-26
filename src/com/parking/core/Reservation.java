package com.parking.core;

import com.parking.users.User;
import com.parking.vehicles.Vehicle;
import com.parking.vehicles.VehicleType;
import com.parking.exception.NoAvailableSlotException;
import com.parking.util.SlotCodeGenerator;
import com.parking.util.Constants;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

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

    /**
     * Calculates duration of parking in hours
     * @return Number of hours parked
     */
    public long calculateParkingDuration() {
        if (checkOut == null) {
            return 0;
        }
        return java.time.Duration.between(checkIn, checkOut).toHours();
    }

    // Getters and setters
    public String getReservationId() {
        return reservationId;
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
