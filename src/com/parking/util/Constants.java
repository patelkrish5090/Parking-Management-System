package com.parking.util;

public final class Constants {
    public static final int NORMAL_SLOTS_PER_TYPE = 100;
    public static final int SUBSCRIPTION_SLOTS_PER_TYPE = 20;
    public static final int DAILY_SUBSCRIPTION_HOURS = 12;
    public static final double EV_CHARGING_RATE = 5.0;


    public static final int ENTRY_GRACE_PERIOD = 15;
    public static final int EXIT_GRACE_PERIOD = 10;

    private Constants() {
        throw new AssertionError("Cannot instantiate utility class");
    }
}
