package editExpense;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalprojectg3.R;
import com.example.finalprojectg3.databinding.ActivityUpdateExpenseBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import model.Expense;

public class UpdateExpenseActivity extends AppCompatActivity {

    private ActivityUpdateExpenseBinding binding;
    private int expensePosition;
    private Expense expense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate layout with View Binding
        binding = ActivityUpdateExpenseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Populate the Category Spinner with expense categories
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.expense_categories, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCategory.setAdapter(categoryAdapter);

        // Populate the Payment Method Spinner with options
        ArrayAdapter<CharSequence> paymentMethodAdapter = ArrayAdapter.createFromResource(this,
                R.array.payment_methods, android.R.layout.simple_spinner_item);
        paymentMethodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerPaymentMethod.setAdapter(paymentMethodAdapter);

        // Retrieve the expense and position from the Intent
        Intent intent = getIntent();
        expense = (Expense) intent.getSerializableExtra("expense");
        expensePosition = intent.getIntExtra("position", -1);

        if (expense != null) {
            populateFields(expense);
        }

        binding.btnSave.setOnClickListener(v -> saveExpense());
        binding.btnCancel.setOnClickListener(v -> finish());
        binding.btnDelete.setOnClickListener(v -> deleteExpense());
    }

    private void populateFields(Expense expense) {
        binding.etAmount.setText(expense.getAmount());

        // Set the selected category in the Spinner
        ArrayAdapter<CharSequence> categoryAdapter = (ArrayAdapter<CharSequence>) binding.spinnerCategory.getAdapter();
        int categoryPosition = categoryAdapter.getPosition(expense.getCategory());
        binding.spinnerCategory.setSelection(categoryPosition);

        binding.etDate.setText(expense.getDate());
        binding.etNotes.setText(expense.getNotes());

        // Set the selected payment method in the Spinner
        ArrayAdapter<CharSequence> paymentMethodAdapter = (ArrayAdapter<CharSequence>) binding.spinnerPaymentMethod.getAdapter();
        int paymentMethodPosition = paymentMethodAdapter.getPosition(expense.getPaymentMethod());
        binding.spinnerPaymentMethod.setSelection(paymentMethodPosition);
    }

    private void saveExpense() {
        if (expensePosition == -1) {
            Toast.makeText(this, "Invalid position", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update the expense object
        expense.setAmount(binding.etAmount.getText().toString());
        expense.setCategory(binding.spinnerCategory.getSelectedItem().toString());
        expense.setDate(binding.etDate.getText().toString());
        expense.setNotes(binding.etNotes.getText().toString());
        expense.setPaymentMethod(binding.spinnerPaymentMethod.getSelectedItem().toString());

        // Save updated expense list
        updateSharedPreferences(expense, expensePosition);
        Toast.makeText(this, "Expense updated", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void deleteExpense() {
        deleteFromSharedPreferences(expensePosition);
        Toast.makeText(this, "Expense deleted", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void updateSharedPreferences(Expense updatedExpense, int position) {
        SharedPreferences sharedPreferences = getSharedPreferences("ExpensePrefs", MODE_PRIVATE);
        String expensesData = sharedPreferences.getString("expenses_list", "[]");

        try {
            JSONArray expenseArray = new JSONArray(expensesData);

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
