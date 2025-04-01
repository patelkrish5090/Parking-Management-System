package com.smartparking;

import com.smartparking.exceptions.PaymentFailedException;

public class PaymentGateway implements Payment, Notification {
    @Override
    public void processPayment(double amount) throws PaymentFailedException {
        // Simulate payment processing using wrapper classes (Double)
        Double amt = amount;
        if (amt <= 0) {
            throw new PaymentFailedException("Invalid payment amount!");
        }
        System.out.println("Processing payment of $" + amt);
        // Simulate payment success
        System.out.println("Payment processed successfully.");
    }

    @Override
    public void sendNotification(String message) {
        System.out.println("Notification: " + message);
    }
}
