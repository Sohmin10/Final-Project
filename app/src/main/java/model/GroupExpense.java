package model;

public class GroupExpense {
    private int expense_id;
    private int group_id;
    private String expense_name;
    private double total_amount;
    private double split_amount;

    // Constructor
    public GroupExpense(int expense_id, int group_id, String expenseName, double totalAmount, double splitAmount) {
        this.expense_id = expense_id;
        this.group_id = group_id;
        this.expense_name = expenseName;
        this.total_amount = totalAmount;
        this.split_amount = splitAmount;
    }

    // Getters and Setters
    public int getExpense_id() {
        return expense_id;
    }

    public void setExpense_id(int expense_id) {
        this.expense_id = expense_id;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public String getExpenseName() {
        return expense_name;
    }

    public void setExpenseName(String expenseName) {
        this.expense_name = expenseName;
    }

    public double getTotalAmount() {
        return total_amount;
    }

    public void setTotalAmount(double totalAmount) {
        this.total_amount = totalAmount;
    }

    public double getSplitAmount() {
        return split_amount;
    }

    public void setSplitAmount(double splitAmount) {
        this.split_amount = splitAmount;
    }
}