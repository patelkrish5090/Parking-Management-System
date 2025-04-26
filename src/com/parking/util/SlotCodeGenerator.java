package com.parking.util;

import com.parking.vehicles.VehicleType;

public final class SlotCodeGenerator {
    private static final String NORMAL_PREFIX = "";
    private static final String SUBSCRIPTION_PREFIX = "S";

    private SlotCodeGenerator() {
        // Private constructor to prevent instantiation
    }

    /**
     * Generates a slot code based on vehicle type and subscription status
     * @param type Vehicle type
     * @param isSubscription Whether it's for subscription users
     * @return Generated slot code prefix (e.g., "C", "SC")
     */
    public static String generateSlotCode(VehicleType type, boolean isSubscription) {
        if (type == null) {
            throw new IllegalArgumentException("Vehicle type cannot be null");
        }

        String prefix = isSubscription ? SUBSCRIPTION_PREFIX : NORMAL_PREFIX;
        return prefix + getVehicleTypeCode(type);
    }

    /**
     * Validates a slot code format
     * @param code Code to validate
     * @return true if valid format
     */
    public static boolean validateSlotCode(String code) {
        if (code == null || code.length() < 2) {
            return false;
        }

        // Check for subscription slot
        int index = 0;
        if (code.startsWith(SUBSCRIPTION_PREFIX)) {
            if (code.length() < 3) return false;
            index = 1;
        }

        char typeChar = code.charAt(index);
        return typeChar == 'C' || typeChar == 'B' || typeChar == 'T' || typeChar == 'E';
    }

    /**
     * Parses vehicle type from slot code
     * @param code Slot code
     * @return VehicleType or null if invalid
     */
    public static VehicleType parseVehicleType(String code) {
        if (!validateSlotCode(code)) {
            return null;
        }

        int index = code.startsWith(SUBSCRIPTION_PREFIX) ? 1 : 0;
        char typeChar = code.charAt(index);

        switch (typeChar) {
            case 'C': return VehicleType.CAR;
            case 'B': return VehicleType.BIKE;
            case 'T': return VehicleType.TRUCK;
            case 'E': return VehicleType.EVCar;
            default: return null;
        }
    }

    private static char getVehicleTypeCode(VehicleType type) {
        switch (type) {
            case CAR: return 'C';
            case BIKE: return 'B';
            case TRUCK: return 'T';
            case EVCar: return 'E';
            default: throw new IllegalArgumentException("Unknown vehicle type");
        }
    }
}

