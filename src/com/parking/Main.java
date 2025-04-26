































































































































































































































































































































































































































































































package com.parking;

import com.parking.core.*;
import com.parking.users.*;
import com.parking.vehicles.*;
import com.parking.payment.*;
import com.parking.logging.*;
import com.parking.util.*;
import com.parking.exception.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final Scanner scanner = new Scanner(System.in);

    private final List<Reservation> activeReservations = new ArrayList<>();
    private final ParkingLotManager lotManager;
    private final BillingSystem billingSystem;
    private final EntryExitLogger logger;
    private final List<User> users;
    private User currentUser;
    private boolean testMode = false;
    private LocalDateTime simulatedNow = LocalDateTime.now();

    public Main() {
        this.lotManager = new ParkingLotManager();
        this.billingSystem = new BillingSystem();
        this.logger = new EntryExitLogger();
        this.users = new ArrayList<>();
        initializeSampleData();
    }

    private void initializeSampleData() {
        Vehicle regularCar = new Car("REG123");
        User regularUser = new User(regularCar);
        users.add(regularUser);

        Vehicle premiumCar = new Car("PREMIUM1");
        Subscription subscription = new Subscription(
                LocalDate.now().plusMonths(1),
                "SUB12345",
                VehicleType.CAR,
                Constants.DAILY_SUBSCRIPTION_HOURS
        );
        User premiumUser = new User(premiumCar, subscription);
        users.add(premiumUser);
    }

    public void start() {
        System.out.println("=== Parking Management System ===");

        while (true) {
            if (currentUser == null) {
                showMainMenu();
            } else {
                showUserMenu();
            }
        }
    }

    private LocalDateTime getCurrentTime() {
        return testMode ? simulatedNow : LocalDateTime.now();
    }

    private void advanceSimulatedTime(int hours) {
        if (testMode) {
            simulatedNow = simulatedNow.plusHours(hours);
            System.out.println("Simulated time advanced by " + hours + " hours to: " +
                    simulatedNow.format(TIME_FORMAT));
        }
    }

    private void showMainMenu() {
        System.out.println("\nMain Menu:");
        System.out.println("1. Register New User");
        System.out.println("2. Check-out Vehicle");
        System.out.println("3. Admin Login");
        System.out.println("4. Exit System");
        System.out.print("Select option: ");

        int choice = readIntInput(1, 4);

        switch (choice) {
            case 1 -> handleCheckIn();
            case 2 -> handleCheckOut();
            case 3 -> handleAdminLogin();
            case 4 -> {
                System.out.println("Exiting system...");
                System.exit(0);
            }
        }
    }

    private void showUserMenu() {
        System.out.println("\nUser Menu (" + currentUser.getUserId() + "):");
        System.out.println("1. Check-in");
        System.out.println("2. Check-out");
        System.out.println("3. View Parking History");
        System.out.println("4. Purchase Subscription");
        System.out.println("5. Logout");
        System.out.print("Select option: ");

        int choice = readIntInput(1, 5);

        switch (choice) {
            case 1 -> handleUserCheckIn();
            case 2 -> handleUserCheckOut();
            case 3 -> showParkingHistory();
            case 4 -> handlePurchaseSubscription();
            case 5 -> currentUser = null;
        }
    }

    private void handleCheckIn() {
        System.out.println("\nVehicle Check-In");
        System.out.print("Enter license plate: ");
        String licensePlate = scanner.nextLine().trim().toUpperCase();

        Optional<User> existingUser = users.stream()
                .filter(u -> u.getVehicle().getLicensePlate().equals(licensePlate))
                .findFirst();

        if (existingUser.isPresent()) {
            currentUser = existingUser.get();
            System.out.println("Welcome back, " + licensePlate);
            return;
        }

        System.out.println("New vehicle detected. Please provide details:");
        System.out.println("1. Car");
        System.out.println("2. Bike");
        System.out.println("3. Truck");
        System.out.println("4. EV Car");
        System.out.print("Select vehicle type: ");

        int typeChoice = readIntInput(1, 5);
        VehicleType type = switch (typeChoice) {
            case 1 -> VehicleType.CAR;
            case 2 -> VehicleType.BIKE;
            case 3 -> VehicleType.TRUCK;
            case 4 -> VehicleType.EVCar;
            default -> throw new IllegalStateException();
        };

        Vehicle vehicle;
        if (type == VehicleType.EVCar || type == VehicleType.EVCar) {
            System.out.print("Enter charging rate per hour: ");
            double rate = readDoubleInput();
            vehicle = type == VehicleType.EVCar ?
                    new EVCar(licensePlate, rate) :
                    new EVBike(licensePlate, rate);
        } else {
            vehicle = switch (type) {
                case CAR -> new Car(licensePlate);
                case BIKE -> new Bike(licensePlate);
                case TRUCK -> new Truck(licensePlate);
                default -> throw new IllegalStateException();
            };
        }

        currentUser = new User(vehicle);
        users.add(currentUser);
        System.out.println("New user registered: " + licensePlate);
    }

    private LocalDateTime getSimulatedTime(String prompt) {
        if (!testMode) return LocalDateTime.now();

        System.out.println(prompt);
        System.out.println("1. Use current time");
        System.out.println("2. Enter custom date/time");
        System.out.print("Select option: ");

        int choice = readIntInput(1, 2);
        if (choice == 1) return LocalDateTime.now();

        System.out.print("Enter date/time (yyyy-MM-dd HH:mm): ");
        String input = scanner.nextLine();
        try {
            return LocalDateTime.parse(input, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        } catch (Exception e) {
            System.out.println("Invalid format. Using current time.");
            return LocalDateTime.now();
        }
    }

    private void handleUserCheckIn() {
        try {
            ParkingSlot slot = lotManager.reserveSlot(currentUser, currentUser.getVehicle());
            LocalDateTime checkInTime = testMode ?
                    getSimulatedTime("\nSet check-in time:") :
                    LocalDateTime.now();

            String reservationId = "RES-" + System.currentTimeMillis();
            Reservation reservation = new Reservation(
                    reservationId,
                    slot,
                    currentUser.getVehicle(),
                    currentUser,
                    checkInTime
            );

            activeReservations.add(reservation);
            logger.logEntry(currentUser, checkInTime);

            System.out.println("\nCheck-in successful!");
            System.out.println("Slot: " + slot.getCode());
            System.out.println("Time: " + checkInTime.format(TIME_FORMAT));
        } catch (NoAvailableSlotException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleCheckOut() {
        System.out.print("\nEnter license plate to check-out: ");
        String licensePlate = scanner.nextLine().trim().toUpperCase();

        Optional<User> user = users.stream()
                .filter(u -> u.getVehicle().getLicensePlate().equals(licensePlate))
                .findFirst();

        if (!user.isPresent()) {
            System.out.println("Vehicle not found in system");
            return;
        }

        handleCheckOutProcess(user.get());
    }

    private void handleUserCheckOut() {
        handleCheckOutProcess(currentUser);
    }

    private void handleCheckOutProcess(User user) {
        try {
            Optional<Reservation> reservationOpt = activeReservations.stream()
                    .filter(r -> r.getUser().equals(user))
                    .findFirst();

            if (!reservationOpt.isPresent()) {
                System.out.println("Error: No active reservation found");
                return;
            }

            Reservation reservation = reservationOpt.get();
            LocalDateTime checkOutTime = testMode ?
                    getSimulatedTime("\nSet check-out time:") :
                    LocalDateTime.now();

            reservation.setCheckOut(checkOutTime);
            logger.logExit(user, checkOutTime);

            lotManager.releaseSlot(reservation);

            double charge = billingSystem.calculateCharge(reservation);
            System.out.println(billingSystem.generateBill(reservation));

            if (charge > 0) {
                processPayment(user, charge);
            } else if (user.isSubscription()) {
                System.out.println("No payment required - covered by subscription");
            } else {
                System.out.println("No payment required (minimum charge not reached)");
            }

            System.out.println("Check-out completed at " + checkOutTime.format(TIME_FORMAT));

            activeReservations.remove(reservation);
        } catch (Exception e) {
            System.out.println("Error during check-out: " + e.getMessage());
        }
    }

    private void processPayment(User user, double amount) {
        System.out.println("\nPayment Options:");
        System.out.println("1. Credit Card");
        System.out.println("2. UPI");
        System.out.println("3. E-Wallet");
        System.out.println("4. Cash");
        System.out.print("Select payment method: ");

        int choice = readIntInput(1, 4);
        PaymentProcessor processor = switch (choice) {
            case 1 -> new CreditCardProcessor();
            case 2 -> new UPIProcessor();
            case 3 -> new EWalletProcessor();
            case 4 -> new CashProcessor();
            default -> throw new IllegalStateException();
        };

        try {
            billingSystem.processPayment(user, amount, processor);
            System.out.println("Payment successful!");
        } catch (PaymentFailedException e) {
            System.out.println("Payment failed: " + e.getMessage());
            System.out.println("Please try another payment method");
            processPayment(user, amount);
        }
    }

    private void showParkingHistory() {
        System.out.println("\nParking History for " + currentUser.getUserId());
        List<String> logs = logger.getLogsByUser(currentUser.getUserId());

        if (logs.isEmpty()) {
            System.out.println("No parking history found");
        } else {
            logs.forEach(System.out::println);
        }
    }

    private void handlePurchaseSubscription() {
        if (currentUser.isSubscription()) {
            System.out.println("You already have an active subscription");
            return;
        }

        VehicleType type = currentUser.getVehicle().getType();
        if (!type.isSubscriptionAllowed()) {
            System.out.println("Subscriptions not available for " + type + " vehicles");
            return;
        }

        System.out.println("\nPurchase Subscription");
        System.out.println("Vehicle Type: " + type);
        System.out.println("Daily Hours: " + Constants.DAILY_SUBSCRIPTION_HOURS);

        System.out.println("1. 1 Month ($50)");
        System.out.println("2. 3 Months ($120)");
        System.out.println("3. 12 Months ($400)");
        System.out.print("Select duration: ");

        int choice = readIntInput(1, 3);
        LocalDate expiry = switch (choice) {
            case 1 -> LocalDate.now().plusMonths(1);
            case 2 -> LocalDate.now().plusMonths(3);
            case 3 -> LocalDate.now().plusMonths(12);
            default -> throw new IllegalStateException();
        };

        double amount = switch (choice) {
            case 1 -> 50;
            case 2 -> 120;
            case 3 -> 400;
            default -> 0;
        };

        processPayment(currentUser, amount);

        String subCode = "SUB-" + currentUser.getUserId() + "-" + System.currentTimeMillis();
        Subscription subscription = new Subscription(
                expiry,
                subCode,
                type,
                Constants.DAILY_SUBSCRIPTION_HOURS
        );

        currentUser.setSubscription(subscription);
        System.out.println("Subscription activated! Code: " + subCode);
    }

    private void handleAdminLogin() {
        System.out.print("\nEnter admin password: ");
        String password = scanner.nextLine();

        if (!"admin123".equals(password)) {
            System.out.println("Invalid password");
            return;
        }

        showAdminMenu();
    }

    private void showAdminMenu() {
        while (true) {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. View All Users");
            System.out.println("2. View Parking Logs");
            System.out.println("3. View Available Slots");
            System.out.println("4. Generate Daily Report");
            System.out.println("5. Toggle Test Mode (Current: " + (testMode ? "ON" : "OFF") + ")");
            System.out.println("6. Advance Simulated Time");
            System.out.println("7. Back to Main Menu");
            System.out.print("Select option: ");

            int choice = readIntInput(1, 7);

            switch (choice) {
                case 1 -> showAllUsers();
                case 2 -> showAllLogs();
                case 3 -> showAvailableSlots();
                case 4 -> generateDailyReport();
                case 5 -> toggleTestMode();
                case 6 -> advanceSimulatedTime();
                case 7 -> { return; }
            }
        }
    }

    private void toggleTestMode() {
        testMode = !testMode;
        simulatedNow = LocalDateTime.now();
        System.out.println("Test mode " + (testMode ? "activated" : "deactivated"));
    }

    private void advanceSimulatedTime() {
        if (!testMode) {
            System.out.println("Test mode must be active to advance time");
            return;
        }
        System.out.print("Enter hours to advance: ");
        int hours = readIntInput(1, 24);
        advanceSimulatedTime(hours);
    }

    private void showAllUsers() {
        System.out.println("\nRegistered Users:");
        if (users.isEmpty()) {
            System.out.println("No users registered");
        } else {
            users.forEach(user -> {
                System.out.print(user.getUserId() + " - " + user.getVehicle().getType());
                if (user.isSubscription()) {
                    System.out.print(" (Subscription)");
                }
                System.out.println();
            });
        }
    }

    private void showAllLogs() {
        System.out.println("\nSystem Logs:");
        users.forEach(user -> {
            System.out.println("\nUser: " + user.getUserId());
            logger.getLogsByUser(user.getUserId()).forEach(System.out::println);
        });
    }

    private void showAvailableSlots() {
        System.out.println("\nAvailable Slots:");
        for (VehicleType type : VehicleType.values()) {
            System.out.println(type + " slots:");
            List<String> available = lotManager.getAvailableSlots(type);
            if (available.isEmpty()) {
                System.out.println("  None available");
            } else {
                available.forEach(slot -> System.out.println("  " + slot));
            }
        }
    }

    private void generateDailyReport() {
        System.out.println("\n" + logger.generateDailyReport(getCurrentTime()));
    }

    private int readIntInput(int min, int max) {
        while (true) {
            try {
                int input = Integer.parseInt(scanner.nextLine());
                if (input >= min && input <= max) {
                    return input;
                }
                System.out.print("Please enter a number between " + min + " and " + max + ": ");
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }

    private double readDoubleInput() {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }

    public static void main(String[] args) {
        new Main().start();
    }
}