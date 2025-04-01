package com.smartparking;

import java.util.Scanner;
import java.io.*;
import com.smartparking.exceptions.ParkingFullException;
import com.smartparking.exceptions.PaymentFailedException;

public class Main {
    public static void main(String[] args) {
        // Create parking lot with capacity of 10 slots.
        ParkingLot parkingLot = new ParkingLot(10);

        // Start parking monitor thread
        ParkingMonitor monitor = new ParkingMonitor(parkingLot);
        Thread monitorThread = new Thread(monitor);
        monitorThread.start();

        // Scanner for user input
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Smart Parking Management System");

        // Simple loop to simulate parking management
        while (true) {
            System.out.println("\nMenu:\n1. Park Vehicle\n2. Reserve Slot\n3. Check Availability\n4. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch(choice) {
                case 1:
                    // Park vehicle (simulate entry)
                    System.out.print("Enter vehicle type (car/bike/truck): ");
                    String type = scanner.next();
                    Vehicle vehicle = null;
                    if (type.equalsIgnoreCase("car")) {
                        vehicle = new Car("Car", "ABC123");
                    } else if (type.equalsIgnoreCase("bike")) {
                        vehicle = new Bike("Bike", "BIKE001");
                    } else if (type.equalsIgnoreCase("truck")) {
                        vehicle = new Truck("Truck", "TRK789");
                    } else {
                        System.out.println("Invalid vehicle type.");
                        break;
                    }
                    try {
                        ParkingLot.ParkingSlot slot = parkingLot.assignSlot(vehicle);
                        System.out.println("Vehicle parked at slot: " + slot.getSlotNumber());
                    } catch (ParkingFullException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 2:
                    // Reserve a slot
                    System.out.print("Enter reservation duration (in hours): ");
                    int duration = scanner.nextInt();
                    Reservation reservation = new Reservation(duration);
                    reservation.makeReservation();
                    break;
                case 3:
                    // Check availability
                    System.out.println("Available slots: " + parkingLot.getAvailableSlots());
                    break;
                case 4:
                    System.out.println("Exiting system. Goodbye!");
                    // Write exit log to file
                    try (FileWriter fw = new FileWriter("parking_log.txt", true);
                         BufferedWriter bw = new BufferedWriter(fw)) {
                        bw.write("System exited at " + java.time.LocalDateTime.now() + "\n");
                    } catch (IOException ex) {
                        System.out.println("Error writing to log file: " + ex.getMessage());
                    }
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
