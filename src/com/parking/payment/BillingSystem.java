package com.parking.payment;

import com.parking.users.Subscription;
import com.parking.users.User;
import com.parking.core.Reservation;
import com.parking.exception.PaymentFailedException;
import com.parking.util.Constants;

import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;


public class BillingSystem {
    private static final double SUBSCRIPTION_DISCOUNT_RATE = 1.0;
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");
    private static final double MINIMUM_CHARGE_HOURS = 1.0;

    public double calculateCharge(Reservation reservation) {
        if (reservation == null || reservation.getCheckOut() == null) {
            throw new IllegalArgumentException("Invalid reservation");
        }

        long minutesParked = ChronoUnit.MINUTES.between(
                reservation.getCheckIn(),
                reservation.getCheckOut()
        );

        double hoursParked = Math.max(MINIMUM_CHARGE_HOURS, minutesParked / 60.0);

        User user = reservation.getUser();
        if (user.isSubscription()) {
            return calculateSubscriptionCharge(user, reservation.getVehicle().getRatePerHour(), hoursParked);
        } else {
            return calculateRegularCharge(reservation.getVehicle().getRatePerHour(), hoursParked);
        }
    }

    private double calculateRegularCharge(double ratePerHour, double hoursParked) {

        double billedHours = Math.ceil(hoursParked);
        return ratePerHour * billedHours;
    }

    private double calculateSubscriptionCharge(User user, double ratePerHour, double hoursParked) {
        Subscription subscription = user.getSubscription();
        double excessHours = 0;
        double remainingHours = subscription.getRemainingDailyHours();
        double usedAllowance = 0;

        if (!subscription.isValid()) {
            return calculateRegularCharge(ratePerHour, hoursParked);
        }

        if (hoursParked > remainingHours) {
            excessHours = hoursParked - remainingHours;
            usedAllowance = 12;
            remainingHours = 0;
        } else {
            usedAllowance = remainingHours - hoursParked;
        }

        subscription.useHours(usedAllowance);

        return excessHours * ratePerHour;
    }

    public boolean processPayment(User user, double amount, PaymentProcessor processor)
            throws PaymentFailedException {
        if (amount <= 0) {
            return true;
        }

        if (processor == null) {
            throw new IllegalArgumentException("Payment processor cannot be null");
        }

        if (!processor.pay(amount)) {
            throw new PaymentFailedException("Payment failed for user: ", amount);
        }

        return true;
    }

    public String generateBill(Reservation reservation) {
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");

        double hoursParked = ChronoUnit.MINUTES.between(
                reservation.getCheckIn(),
                reservation.getCheckOut()
        ) / 60.0;

        double charge = calculateCharge(reservation);
        User user = reservation.getUser();

        StringBuilder bill = new StringBuilder();
        bill.append("\n=== Parking Receipt ===\n");
        bill.append(String.format("User ID: %s\n", user.getUserId()));
        bill.append(String.format("Vehicle: %s\n", reservation.getVehicle()));
        bill.append(String.format("Entry: %s\n", reservation.getCheckIn().format(timeFormat)));
        bill.append(String.format("Exit: %s\n", reservation.getCheckOut().format(timeFormat)));
        bill.append(String.format("Duration: %.1f hours\n", hoursParked));

        if (user.isSubscription()) {
            double allowance = Constants.DAILY_SUBSCRIPTION_HOURS;
            double remainingBefore = user.getSubscription().getRemainingDailyHours();
            double usedAllowance = Math.min(hoursParked, remainingBefore);
            double excessHours = Math.max(0, hoursParked - remainingBefore);
            double remainingAfter = Math.max(0, allowance - usedAllowance);
            user.getSubscription().setRemainingDailyHours(remainingAfter);

            bill.append("User Type: Subscription\n");
            bill.append(String.format("Daily Allowance: %.1f hours\n", allowance));
            bill.append(String.format("Used Allowance: %.1f hours\n", usedAllowance));
            bill.append(String.format("Excess Hours: %.1f hours\n", excessHours));
            bill.append(String.format("Remaining Today: %.1f hours\n", remainingAfter));

            if (excessHours > 0) {
                bill.append(String.format("Excess Charge: $%.2f\n", excessHours * reservation.getVehicle().getRatePerHour()));
            }
        }

        bill.append(String.format("Total Charge: $%.2f\n", charge));
        bill.append("======================");

        return bill.toString();
    }
}
