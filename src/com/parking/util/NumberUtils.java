package com.parking.util;

public final class NumberUtils {
    private NumberUtils() {

    }


    public static double roundToTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }


    public static String formatCurrency(double amount) {
        return String.format("$%.2f", roundToTwoDecimals(amount));
    }
}