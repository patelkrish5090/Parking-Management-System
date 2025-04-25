package com.parking.util;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

public final class TimeUtils {
    private TimeUtils() {
        // Prevent instantiation
    }

    /**
     * Calculates hours between two timestamps with minimum billing period
     * @param start Start time
     * @param end End time
     * @param minBillableHours Minimum hours to bill (e.g., 1 hour)
     * @return total billable hours
     */
    public static long calculateBillableHours(LocalDateTime start, LocalDateTime end, int minBillableHours) {
        if (start == null || end == null || end.isBefore(start)) {
            throw new IllegalArgumentException("Invalid time range");
        }

        long minutes = Duration.between(start, end).toMinutes();
        long fullHours = minutes / 60;
        long remainderMinutes = minutes % 60;

        // Apply minimum billing
        if (fullHours == 0 && remainderMinutes > 0) {
            return minBillableHours;
        }

        // Round up partial hours
        return remainderMinutes > 0 ? fullHours + 1 : fullHours;
    }

    /**
     * Checks if a date is today
     * @param dateTime DateTime to check
     * @return true if date is today
     */
    public static boolean isToday(LocalDateTime dateTime) {
        return dateTime.toLocalDate().equals(LocalDate.now());
    }
}