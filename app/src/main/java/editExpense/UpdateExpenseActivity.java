package editExpense;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalprojectg3.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import model.Expense;

public class UpdateExpenseActivity extends AppCompatActivity {

    private EditText etAmount, etCategory, etDate, etNotes, etPaymentMethod;
    private int expensePosition;
    private Expense expense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_expense);

        // Initialize views
        etAmount = findViewById(R.id.etAmount);
        etCategory = findViewById(R.id.etCategory);
        etDate = findViewById(R.id.etDate);
        etNotes = findViewById(R.id.etNotes);
        etPaymentMethod = findViewById(R.id.etPaymentMethod);

        // Retrieve the expense and position from the Intent
        Intent intent = getIntent();
        expense = (Expense) intent.getSerializableExtra("expense");
        expensePosition = intent.getIntExtra("position", -1);

        if (expense != null) {
            populateFields(expense);
        }

        findViewById(R.id.btnSave).setOnClickListener(v -> saveExpense());
        findViewById(R.id.btnCancel).setOnClickListener(v -> finish());
        findViewById(R.id.btnDelete).setOnClickListener(v -> deleteExpense());
    }

    private void populateFields(Expense expense) {
        etAmount.setText(expense.getAmount());
        etCategory.setText(expense.getCategory());
        etDate.setText(expense.getDate());
        etNotes.setText(expense.getNotes());
        etPaymentMethod.setText(expense.getPaymentMethod());
    }

    private void saveExpense() {
        if (expensePosition == -1) {
            Toast.makeText(this, "Invalid position", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update the expense object
        expense.setAmount(etAmount.getText().toString());
        expense.setCategory(etCategory.getText().toString());
        expense.setDate(etDate.getText().toString());
        expense.setNotes(etNotes.getText().toString());
        expense.setPaymentMethod(etPaymentMethod.getText().toString());

        // Save updated expense list
        updateSharedPreferences(expense, expensePosition);
        Toast.makeText(this, "Expense updated", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void deleteExpense() {
        // Remove expense from SharedPreferences
        deleteFromSharedPreferences(expensePosition);
        Toast.makeText(this, "Expense deleted", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void updateSharedPreferences(Expense updatedExpense, int position) {
        SharedPreferences sharedPreferences = getSharedPreferences("ExpensePrefs", MODE_PRIVATE);
        String expensesData = sharedPreferences.getString("expenses_list", "[]");

        try {
            JSONArray expenseArray = new JSONArray(expensesData);

            // Ensure that the position is valid
            if (position >= 0 && position < expenseArray.length()) {
                JSONObject updatedExpenseObj = new JSONObject();
                updatedExpenseObj.put("amount", updatedExpense.getAmount());
                updatedExpenseObj.put("category", updatedExpense.getCategory());
                updatedExpenseObj.put("date", updatedExpense.getDate());
                updatedExpenseObj.put("notes", updatedExpense.getNotes());
                updatedExpenseObj.put("paymentMethod", updatedExpense.getPaymentMethod());

                expenseArray.put(position, updatedExpenseObj);
                sharedPreferences.edit().putString("expenses_list", expenseArray.toString()).apply();
            } else {
                Toast.makeText(this, "Invalid expense position", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void deleteFromSharedPreferences(int position) {
        SharedPreferences sharedPreferences = getSharedPreferences("ExpensePrefs", MODE_PRIVATE);
        String expensesData = sharedPreferences.getString("expenses_list", "[]");

        try {
            JSONArray expenseArray = new JSONArray(expensesData);
            expenseArray.remove(position);

            sharedPreferences.edit().putString("expenses_list", expenseArray.toString()).apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}