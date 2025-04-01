package com.smartparking;

public class Reservation {
    private int durationHours;

    public Reservation(int durationHours) {
        this.durationHours = durationHours;
    }

    public void makeReservation() {
        System.out.println("Reservation made for " + durationHours + " hours.");
    }
}
