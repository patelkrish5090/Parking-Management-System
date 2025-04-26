package com.parking.core;

import com.parking.users.User;
import com.parking.vehicles.Vehicle;
import com.parking.vehicles.VehicleType;
import com.parking.exception.NoAvailableSlotException;
import com.parking.util.SlotCodeGenerator;
import com.parking.util.Constants;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ParkingLotManager {
    private final List<ParkingSlot> normalSlots;
    private final List<ParkingSlot> subscriptionSlots;
    private final List<Reservation> activeReservations;
    private final AtomicInteger reservationCounter;

    public ParkingLotManager() {
        this.normalSlots = new ArrayList<>();
        this.subscriptionSlots = new ArrayList<>();
        this.activeReservations = new ArrayList<>();
        this.reservationCounter = new AtomicInteger(1);
        initializeParkingSlots();
    }


    public ParkingSlot reserveSlot(User user, Vehicle vehicle) throws NoAvailableSlotException {
        VehicleType type = vehicle.getType();
        ParkingSlot slot = findAvailableSlot(user, type);

        if (slot == null) {
            throw new NoAvailableSlotException("No available slot for vehicle type: " + type);
        }

        slot.setOccupied(true);
        String reservationId = "RES-" + reservationCounter.getAndIncrement();
        Reservation reservation = new Reservation(reservationId, slot, vehicle, user, LocalDateTime.now());
        activeReservations.add(reservation);

        return slot;
    }

    public Reservation releaseSlot(Reservation reservation) {
        reservation.getSlot().setOccupied(false);
        return reservation;
    }

    public List<String> getAvailableSlots(VehicleType vehicleType) {
        List<String> availableSlots = new ArrayList<>();

        for (ParkingSlot slot : normalSlots) {
            if (slot.getType() == vehicleType && !slot.isOccupied()) {
                availableSlots.add(slot.getCode());
            }
        }

        for (ParkingSlot slot : subscriptionSlots) {
            if (slot.getType() == vehicleType && !slot.isOccupied()) {
                availableSlots.add(slot.getCode());
            }
        }

        return availableSlots;
    }

    private ParkingSlot findAvailableSlot(User user, VehicleType type) {
        if (user.isSubscription() && user.getSubscription().getAllowedType() == type) {
            for (ParkingSlot slot : subscriptionSlots) {
                if (slot.getType() == type && !slot.isOccupied()) {
                    return slot;
                }
            }
        }

        for (ParkingSlot slot : normalSlots) {
            if (slot.getType() == type && !slot.isOccupied()) {
                return slot;
            }
        }

        return null;
    }

    private void initializeParkingSlots() {
        for (VehicleType type : VehicleType.values()) {
            for (int i = 1; i <= Constants.NORMAL_SLOTS_PER_TYPE; i++) {
                String code = SlotCodeGenerator.generateSlotCode(type, false) + String.format("%02d", i);
                normalSlots.add(new ParkingSlot(code, type, false));
            }

            if (type.isSubscriptionAllowed()) {
                for (int i = 1; i <= Constants.SUBSCRIPTION_SLOTS_PER_TYPE; i++) {
                    String code = SlotCodeGenerator.generateSlotCode(type, true) + String.format("%02d", i);
                    subscriptionSlots.add(new ParkingSlot(code, type, true));
                }
            }
        }
    }
}