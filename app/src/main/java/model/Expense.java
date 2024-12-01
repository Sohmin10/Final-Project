package model;

import java.io.Serializable;

public class Expense implements Serializable {
    private String amount;
    private String category;
    private String date;
    private String notes;
    private String paymentMethod;

    // Constructor
    public Expense(String amount, String category, String date, String notes, String paymentMethod) {
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.notes = notes;
        this.paymentMethod = paymentMethod;
    }

    // Getters and Setters
    public String getAmount() { return amount; }
    public String getCategory() { return category; }
    public String getDate() { return date; }
    public String getNotes() { return notes; }
    public String getPaymentMethod() { return paymentMethod; }

    public void setAmount(String amount) { this.amount = amount; }
    public void setCategory(String category) { this.category = category; }
    public void setDate(String date) { this.date = date; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}
