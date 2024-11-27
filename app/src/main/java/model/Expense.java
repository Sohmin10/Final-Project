package model;

import java.io.Serializable;

public class Expense implements Serializable {  // Implement Serializable
    private final String amount;
    private final String category;
    private final String date;
    private final String notes;
    private final String paymentMethod;

    public Expense(String amount, String category, String date, String notes, String paymentMethod) {
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.notes = notes;
        this.paymentMethod = paymentMethod;
    }

    public String getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public String getDate() {
        return date;
    }

    public String getNotes() {
        return notes;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }
}
