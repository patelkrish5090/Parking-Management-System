package com.parking.users;

import com.parking.vehicles.Vehicle;
import java.time.LocalDateTime;

public class User {
    private final String userId;  // License plate serves as user ID
    private final Vehicle vehicle;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private Subscription subscription;

    /**
     * Creates a regular (non-subscription) user
     * @param vehicle The user's vehicle (license plate becomes user ID)
     */
    public User(Vehicle vehicle) {
        this(vehicle, null);
    }

    /**
     * Creates a subscription user
     * @param vehicle The user's vehicle
     * @param subscription The user's subscription details
     * @throws IllegalArgumentException if vehicle license plate doesn't match subscription
     */
    public User(Vehicle vehicle, Subscription subscription) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null");
        }

        this.userId = vehicle.getLicensePlate();
        this.vehicle = vehicle;

        if (subscription != null) {
            setSubscription(subscription);
        }
    }

    // Core getters
    public String getUserId() {
        return userId;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public boolean isSubscription() {
        return subscription != null;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    /**
     * Updates the user's subscription
     * @param subscription New subscription
     * @throws IllegalArgumentException if vehicle type doesn't match subscription
     */
    public void setSubscription(Subscription subscription) {
        if (subscription != null && !subscription.getAllowedType().equals(vehicle.getType())) {
            throw new IllegalArgumentException("Vehicle type doesn't match subscription type");
        }
        this.subscription = subscription;
    }

    // Time tracking methods
    public void setEntryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
        this.exitTime = null;
    }

    public void setExitTime(LocalDateTime exitTime) {
        if (entryTime == null) {
            throw new IllegalStateException("Cannot set exit time without entry time");
        }
        if (exitTime.isBefore(entryTime)) {
            throw new IllegalArgumentException("Exit time cannot be before entry time");
        }
        this.exitTime = exitTime;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public long getParkingDurationHours() {
        if (entryTime == null || exitTime == null) {
            return 0;
        }
        return java.time.Duration.between(entryTime, exitTime).toHours();
    }

    public boolean isCurrentlyParked() {
        return entryTime != null && exitTime == null;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", vehicle=" + vehicle +
                (isSubscription() ? ", subscription=true" : "") +
                (entryTime != null ? ", entryTime=" + entryTime : "") +
                (exitTime != null ? ", exitTime=" + exitTime : "") +
                '}';
    }
}