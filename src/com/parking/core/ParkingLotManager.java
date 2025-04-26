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

public class ParkingLotManager {
    private final Map<VehicleType, List<ParkingSlot>> normalSlots;
    private final Map<VehicleType, List<ParkingSlot>> subscriptionSlots;
    private final Map<String, Reservation> activeReservations;
    private final AtomicInteger reservationCounter;

    public ParkingLotManager() {
        this.normalSlots = new HashMap<>();
        this.subscriptionSlots = new HashMap<>();
        this.activeReservations = new HashMap<>();
        this.reservationCounter = new AtomicInteger(1);
        initializeParkingSlots();
    }

    /**
     * Reserves a parking slot for the given user and vehicle
     * @param user The user making the reservation
     * @param vehicle The vehicle to be parked
     * @return The reserved ParkingSlot
     * @throws NoAvailableSlotException If no suitable slot is available
     */
    public ParkingSlot reserveSlot(User user, Vehicle vehicle) throws NoAvailableSlotException {
        VehicleType type = vehicle.getType();
        ParkingSlot slot = findAvailableSlot(user, type);

        if (slot == null) {
            throw new NoAvailableSlotException("No available slot for vehicle type: " + type);
        }

        slot.setOccupied(true);
        String reservationId = "RES-" + reservationCounter.getAndIncrement();
        Reservation reservation = new Reservation(reservationId, slot, vehicle, user, LocalDateTime.now());
        activeReservations.put(vehicle.getLicensePlate(), reservation);

        return slot;
    }

    /**
     * Releases a parking slot based on license plate
     * @param licensePlate The license plate of the vehicle leaving
     * @return The Reservation object with completed details
     */
    public Reservation releaseSlot(String licensePlate) {
        Reservation reservation = activeReservations.get(licensePlate);
        if (reservation == null) {
            return null;
        }

        reservation.getSlot().setOccupied(false);
        reservation.setCheckOut(LocalDateTime.now());
        activeReservations.remove(licensePlate);

        return reservation;
    }

    /**
     * Gets available slots for a specific vehicle type
     * @param vehicleType The type of vehicle (CAR, BIKE, etc.)
     * @return List of available slot codes
     */
    public List<String> getAvailableSlots(VehicleType vehicleType) {
        List<String> availableSlots = new ArrayList<>();

        // Check normal slots
        for (ParkingSlot slot : normalSlots.get(vehicleType)) {
            if (!slot.isOccupied()) {
                availableSlots.add(slot.getCode());
            }
        }

        // Check subscription slots
        try {
            for (ParkingSlot slot : subscriptionSlots.get(vehicleType)) {
                if (slot == null) continue;
                if (!slot.isOccupied()) {
                    availableSlots.add(slot.getCode());
                }
            }
        } catch (NullPointerException npe) {
            System.out.println(vehicleType+" not found in Subscription slots");
        }

        return availableSlots;
    }

    /**
     * Generates a unique slot code based on vehicle type and subscription status
     * @param vehicleType The type of vehicle
     * @param isSubscription Whether it's for subscription users
     * @return Generated slot code (e.g., "C10", "SC02")
     */
    public String generateSlotCode(VehicleType vehicleType, boolean isSubscription) {
        return SlotCodeGenerator.generateSlotCode(vehicleType, isSubscription);
    }

    private ParkingSlot findAvailableSlot(User user, VehicleType type) {
        // First check subscription slots if user has subscription
        if (user.isSubscription() && user.getSubscription().getAllowedType() == type) {
            for (ParkingSlot slot : subscriptionSlots.get(type)) {
                if (!slot.isOccupied()) {
                    return slot;
                }
            }
        }

        // Then check normal slots
        for (ParkingSlot slot : normalSlots.get(type)) {
            if (!slot.isOccupied()) {
                return slot;
            }
        }

        return null;
    }

    /**
     * Initializes parking slots on system startup
     */
    private void initializeParkingSlots() {
        for (VehicleType type : VehicleType.values()) {
            // Initialize normal slots
            List<ParkingSlot> normalSlotsForType = new ArrayList<>();
            for (int i = 1; i <= Constants.NORMAL_SLOTS_PER_TYPE; i++) {
                String code = generateSlotCode(type, false) + String.format("%02d", i);
                normalSlotsForType.add(new ParkingSlot(code, type, false));
            }
            normalSlots.put(type, normalSlotsForType);

            // Initialize subscription slots if allowed for this vehicle type
            if (type.isSubscriptionAllowed()) {
                List<ParkingSlot> subscriptionSlotsForType = new ArrayList<>();
                for (int i = 1; i <= Constants.SUBSCRIPTION_SLOTS_PER_TYPE; i++) {
                    String code = generateSlotCode(type, true) + String.format("%02d", i);
                    subscriptionSlotsForType.add(new ParkingSlot(code, type, true));
                }
                subscriptionSlots.put(type, subscriptionSlotsForType);
            }
        }
    }
}
