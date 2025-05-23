package com.parking.vehicles;

public enum VehicleType {
    CAR(true),
    BIKE(true),
    TRUCK(false),
    EVCar(true);

    private final boolean isSubscriptionAllowed;

    VehicleType(boolean isSubscriptionAllowed) {
        this.isSubscriptionAllowed = isSubscriptionAllowed;
    }

    public boolean isSubscriptionAllowed() {
        return isSubscriptionAllowed;
    }
}

