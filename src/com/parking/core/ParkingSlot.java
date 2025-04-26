package com.parking.core;


import com.parking.vehicles.VehicleType;
import java.time.LocalDate;


public class ParkingSlot {
    private final String code;
    private boolean isOccupied;
    private final VehicleType type;
    private final boolean isSubscriptionSlot;
    private final SlotStatistics statistics;

    public ParkingSlot(String code, VehicleType type, boolean isSubscriptionSlot) {
        this.code = code;
        this.type = type;
        this.isSubscriptionSlot = isSubscriptionSlot;
        this.isOccupied = false;
        this.statistics = new SlotStatistics();
    }

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

    public static class SlotStatistics {
        private final LocalDate date;
        private final int totalHoursUsed;
        private final int timesOccupied;

        public SlotStatistics() {
            this.date = LocalDate.now();
            this.totalHoursUsed = 0;
            this.timesOccupied = 0;
        }
    }
}

