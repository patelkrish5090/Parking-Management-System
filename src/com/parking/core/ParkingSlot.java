package com.parking.core;

import com.parking.users.User;
import com.parking.vehicles.Vehicle;
import com.parking.vehicles.VehicleType;
import com.parking.exception.NoAvailableSlotException;
import com.parking.util.SlotCodeGenerator;
import com.parking.util.Constants;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ParkingSlot {
    private final String code;
    private boolean isOccupied;
    private final VehicleType type;
    private final boolean isSubscriptionSlot;
    private SlotStatistics statistics;

    public ParkingSlot(String code, VehicleType type, boolean isSubscriptionSlot) {
        this.code = code;
        this.type = type;
        this.isSubscriptionSlot = isSubscriptionSlot;
        this.isOccupied = false;
        this.statistics = new SlotStatistics();
    }

    // Getters and setters
    public String getCode() {
        return code;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    public VehicleType getType() {
        return type;
    }

    public boolean isSubscriptionSlot() {
        return isSubscriptionSlot;
    }

    public SlotStatistics getStatistics() {
        return statistics;
    }

    /**
     * Static nested class for analytics data
     */
    public static class SlotStatistics {
        private LocalDate date;
        private int totalHoursUsed;
        private int timesOccupied;

        public SlotStatistics() {
            this.date = LocalDate.now();
            this.totalHoursUsed = 0;
            this.timesOccupied = 0;
        }

        public void addUsage(int hours) {
            if (!date.equals(LocalDate.now())) {
                resetStatistics();
            }
            this.totalHoursUsed += hours;
            this.timesOccupied++;
        }

        private void resetStatistics() {
            this.date = LocalDate.now();
            this.totalHoursUsed = 0;
            this.timesOccupied = 0;
        }

        // Getters
        public LocalDate getDate() {
            return date;
        }

        public int getTotalHoursUsed() {
            return totalHoursUsed;
        }

        public int getTimesOccupied() {
            return timesOccupied;
        }
    }
}

