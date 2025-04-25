package com.parking.users;

import com.parking.vehicles.Vehicle;
import com.parking.vehicles.VehicleType;

import java.time.LocalDate;

public class Subscription {
    private int remainingDailyHours;
    private final LocalDate expiryDate;
    private final String subscriptionCode;
    private final VehicleType allowedType;

    /**
     * Creates a new subscription
     * @param expiryDate When the subscription expires
     * @param subscriptionCode Unique subscription code
     * @param allowedType Vehicle type allowed by this subscription
     * @param dailyHours Daily hour allowance (typically 12)
     */
    public Subscription(LocalDate expiryDate, String subscriptionCode,
                        VehicleType allowedType, int dailyHours) {
        if (expiryDate == null) {
            throw new IllegalArgumentException("Expiry date cannot be null");
        }
        if (subscriptionCode == null || subscriptionCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Subscription code cannot be null or empty");
        }
        if (allowedType == null) {
            throw new IllegalArgumentException("Vehicle type cannot be null");
        }
        if (dailyHours <= 0) {
            throw new IllegalArgumentException("Daily hours must be positive");
        }

        this.expiryDate = expiryDate;
        this.subscriptionCode = subscriptionCode.trim();
        this.allowedType = allowedType;
        this.remainingDailyHours = dailyHours;
    }

    public VehicleType getAllowedType() {
        return allowedType;
    }

    public boolean isValid() {
        return !LocalDate.now().isAfter(expiryDate);
    }

    public int useHours(int hours) {
        if (!isValid()) {
            throw new IllegalStateException("Subscription has expired");
        }
        if (hours < 0) {
            throw new IllegalArgumentException("Hours cannot be negative");
        }

        if (hours > remainingDailyHours) {
            int extraHours = hours - remainingDailyHours;
            remainingDailyHours = 0;
            return extraHours;
        }

        remainingDailyHours -= hours;
        return 0;
    }

    public void resetDailyHours(int dailyHours) {
        if (dailyHours <= 0) {
            throw new IllegalArgumentException("Daily hours must be positive");
        }
        this.remainingDailyHours = dailyHours;
    }

    // Getters
    public int getRemainingDailyHours() {
        return remainingDailyHours;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public String getSubscriptionCode() {
        return subscriptionCode;
    }

    @Override
    public String toString() {
        return "Subscription{" +
                "remainingDailyHours=" + remainingDailyHours +
                ", expiryDate=" + expiryDate +
                ", allowedType=" + allowedType +
                '}';
    }
}