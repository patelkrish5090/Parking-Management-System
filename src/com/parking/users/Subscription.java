package com.parking.users;

import com.parking.vehicles.VehicleType;

import java.time.LocalDate;

public class Subscription {
    private double remainingDailyHours;
    private final LocalDate expiryDate;
    private final String subscriptionCode;
    private final VehicleType allowedType;

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

    public double useHours(double hoursToUse) {
        if (!isValid()) {
            throw new IllegalStateException("Subscription has expired");
        }
        if (hoursToUse < 0) {
            throw new IllegalArgumentException("Hours cannot be negative");
        }

        double actualHoursUsed = Math.min(hoursToUse, remainingDailyHours);
        remainingDailyHours -= actualHoursUsed;

        return hoursToUse - actualHoursUsed;
    }


    public double getRemainingDailyHours() {
        return remainingDailyHours;
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