package com.parking.util;

import com.parking.vehicles.VehicleType;

public final class SlotCodeGenerator {
    private static final String NORMAL_PREFIX = "";
    private static final String SUBSCRIPTION_PREFIX = "S";

    private SlotCodeGenerator() {

    }

    
    public static String generateSlotCode(VehicleType type, boolean isSubscription) {
        if (type == null) {
            throw new IllegalArgumentException("Vehicle type cannot be null");
        }

        String prefix = isSubscription ? SUBSCRIPTION_PREFIX : NORMAL_PREFIX;
        return prefix + getVehicleTypeCode(type);
    }

    
    public static boolean validateSlotCode(String code) {
        if (code == null || code.length() < 2) {
            return false;
        }


        int index = 0;
        if (code.startsWith(SUBSCRIPTION_PREFIX)) {
            if (code.length() < 3) return false;
            index = 1;
        }

        char typeChar = code.charAt(index);
        return typeChar == 'C' || typeChar == 'B' || typeChar == 'T' || typeChar == 'E';
    }

    
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

