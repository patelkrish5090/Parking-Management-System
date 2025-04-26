package com.parking.util;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

public final class TimeUtils {
    private TimeUtils() {

    }

    
    public static long calculateBillableHours(LocalDateTime start, LocalDateTime end, int minBillableHours) {
        if (start == null || end == null || end.isBefore(start)) {
            throw new IllegalArgumentException("Invalid time range");
        }

        long minutes = Duration.between(start, end).toMinutes();
        long fullHours = minutes / 60;
        long remainderMinutes = minutes % 60;


        if (fullHours == 0 && remainderMinutes > 0) {
            return minBillableHours;
        }


        return remainderMinutes > 0 ? fullHours + 1 : fullHours;
    }

    
    public static boolean isToday(LocalDateTime dateTime) {
        return dateTime.toLocalDate().equals(LocalDate.now());
    }
}