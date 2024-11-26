package AddExpense;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalprojectg3.R;
import com.example.finalprojectg3.databinding.ActivityAddExpenseBinding;

import java.util.Calendar;

public class AddExpense extends AppCompatActivity {

    private ActivityAddExpenseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable Edge-to-Edge layout
        EdgeToEdge.enable(this);

        // Set up ViewBinding
        binding = ActivityAddExpenseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());




        setupAddExpense();

        setupDatePicker();
    }

    private void setupAddExpense() {
        // Handle Add Expense button click
        binding.btnAddExpense.setOnClickListener(v -> {
            String amount = binding.etAmount.getText().toString();
            String category = binding.spCategory.getSelectedItem().toString();
            String date = binding.etDate.getText().toString();
            String notes = binding.etNotes.getText().toString();
            String paymentMethod = binding.spPaymentMethod.getSelectedItem().toString();

            // Input validation
            if (TextUtils.isEmpty(amount)) {
                showToast("Amount is required!");
                return;
            }

            if (TextUtils.isEmpty(date)) {
                showToast("Date is required!");
                return;
            }

            // Save expense into SharedPreferences
            saveExpense(amount, category, date, notes, paymentMethod);
        });
    }

    private void setupDatePicker() {
        binding.etDate.setOnClickListener(v -> {
            // Get current date
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // Create DatePickerDialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    AddExpense.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    // Set selected date to EditText in "dd/MM/yyyy" format
                    String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                    binding.etDate.setText(selectedDate);
                }
            }, year, month, day);

            // Show DatePickerDialog
            datePickerDialog.show();
        });
    }

    private void saveExpense(String amount, String category, String date, String notes, String paymentMethod) {
        // Use SharedPreferences to save the expense data
        SharedPreferences sharedPreferences = getSharedPreferences("ExpensePrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // For simplicity, we'll save a single expense with a unique key
        String key = "expense_" + System.currentTimeMillis(); // Use current timestamp as a unique key
        editor.putString(key + "_amount", amount);
        editor.putString(key + "_category", category);
        editor.putString(key + "_date", date);
        editor.putString(key + "_notes", notes);
        editor.putString(key + "_paymentMethod", paymentMethod);

        // Apply changes to SharedPreferences
        editor.apply();

        // Show the saved expense in a Toast
        String expenseDetails = String.format("Expense Added:\nAmount: %s\nCategory: %s\nDate: %s\nNotes: %s\nPayment Method: %s",
                amount, category, date, notes, paymentMethod);
        showToast(expenseDetails);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
