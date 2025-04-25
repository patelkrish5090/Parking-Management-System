package com.parking.payment;

import com.parking.users.Subscription;
import com.parking.users.User;
import com.parking.core.Reservation;
import com.parking.exception.PaymentFailedException;
import java.time.temporal.ChronoUnit;


public class BillingSystem {
    private static final double SUBSCRIPTION_DISCOUNT_RATE = 1.0; // 100% discount for subscription hours

    /**
     * Calculates total charges for a reservation
     * @param reservation The completed reservation
     * @return Total amount to charge
     */
    public double calculateCharge(Reservation reservation) {
        if (reservation == null || reservation.getCheckOut() == null) {
            throw new IllegalArgumentException("Invalid reservation");
        }

        User user = reservation.getUser();
        long hoursParked = ChronoUnit.HOURS.between(
                reservation.getCheckIn(),
                reservation.getCheckOut()
        );

        if (user.isSubscription()) {
            return calculateSubscriptionCharge(user, reservation.getVehicle().getRatePerHour(), hoursParked);
        } else {
            return calculateRegularCharge(reservation.getVehicle().getRatePerHour(), hoursParked);
        }
    }

    private double calculateRegularCharge(double ratePerHour, long hoursParked) {
        return ratePerHour * hoursParked;
    }

    private double calculateSubscriptionCharge(User user, double ratePerHour, long hoursParked) {
        Subscription subscription = user.getSubscription();
        if (!subscription.isValid()) {
            return calculateRegularCharge(ratePerHour, hoursParked);
        }

        int coveredHours = Math.min((int)hoursParked, subscription.getRemainingDailyHours());
        double charge = (hoursParked - coveredHours) * ratePerHour;

        // Update remaining hours
        subscription.useHours((int)hoursParked);

        return charge;
    }

    /**
     * Processes payment for a user
     * @param user The user making payment
     * @param amount Amount to be paid
     * @param processor Payment processor to use
     * @return true if payment succeeded
     * @throws PaymentFailedException If payment fails
     */
    public boolean processPayment(User user, double amount, PaymentProcessor processor)
            throws PaymentFailedException {
        if (amount <= 0) {
            return true; // No payment needed
        }

        if (processor == null) {
            throw new IllegalArgumentException("Payment processor cannot be null");
        }

        if (!processor.pay(amount)) {
            throw new PaymentFailedException("Payment failed for user: ", amount);
        }

        return true;
    }

    /**
     * Generates a formatted bill/receipt
     * @param reservation The reservation details
     * @return Formatted bill string
     */
    public String generateBill(Reservation reservation) {
        double charge = calculateCharge(reservation);
        User user = reservation.getUser();

        StringBuilder bill = new StringBuilder();
        bill.append("\n=== Parking Receipt ===\n");
        bill.append(String.format("User ID: %s\n", user.getUserId()));
        bill.append(String.format("Vehicle: %s\n", reservation.getVehicle()));
        bill.append(String.format("Entry: %s\n", reservation.getCheckIn()));
        bill.append(String.format("Exit: %s\n", reservation.getCheckOut()));

        long hours = ChronoUnit.HOURS.between(reservation.getCheckIn(), reservation.getCheckOut());
        bill.append(String.format("Duration: %d hours\n", hours));

        if (user.isSubscription()) {
            bill.append("User Type: Subscription\n");
            int remainingHours = user.getSubscription().getRemainingDailyHours();
            bill.append(String.format("Remaining Daily Hours: %d\n", remainingHours));
        } else {
            bill.append("User Type: Regular\n");
        }

        bill.append(String.format("Total Charge: $%.2f\n", charge));
        bill.append("======================");

        return bill.toString();
    }
}
