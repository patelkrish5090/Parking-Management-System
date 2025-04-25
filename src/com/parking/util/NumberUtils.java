package com.parking.util;

public final class NumberUtils {
    private NumberUtils() {
        // Prevent instantiation
    }

    /**
     * Rounds a double value to 2 decimal places
     * @param value Value to round
     * @return rounded value
     */
    public static double roundToTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    /**
     * Formats currency value
     * @param amount Amount to format
     * @return formatted string (e.g., "$12.50")
     */
    public static String formatCurrency(double amount) {
        return String.format("$%.2f", roundToTwoDecimals(amount));
    }
}