package com.parking.users;

import com.parking.vehicles.Vehicle;
import java.time.LocalDateTime;

public class User {
    private final String userId;
    private final Vehicle vehicle;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private Subscription subscription;

    public User(Vehicle vehicle) {
        this(vehicle, null);
    }

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

    public void setSubscription(Subscription subscription) {
        if (subscription != null && !subscription.getAllowedType().equals(vehicle.getType())) {
            throw new IllegalArgumentException("Vehicle type doesn't match subscription type");
        }
        this.subscription = subscription;
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