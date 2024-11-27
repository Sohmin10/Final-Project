package AddExpense;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalprojectg3.R;
import com.example.finalprojectg3.databinding.ActivityAddExpenseBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

        setupSpinner(); // Initialize the spinner with data
        setupAddExpense(); // Handle add expense functionality
        setupDatePicker(); // Handle date selection
    }

    private void setupSpinner() {
        // Populate the spinner with categories from resources
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.expense_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spCategory.setAdapter(adapter);

        // Populate payment method spinner
        ArrayAdapter<CharSequence> paymentAdapter = ArrayAdapter.createFromResource(
                this, R.array.payment_methods, android.R.layout.simple_spinner_item);
        paymentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spPaymentMethod.setAdapter(paymentAdapter);
    }

    private void setupAddExpense() {
        binding.btnAddExpense.setOnClickListener(v -> {
            String amount = binding.etAmount.getText().toString();
            String category = binding.spCategory.getSelectedItem() != null
                    ? binding.spCategory.getSelectedItem().toString()
                    : "Uncategorized"; // Default value
            String date = binding.etDate.getText().toString();
            String notes = binding.etNotes.getText().toString();
            String paymentMethod = binding.spPaymentMethod.getSelectedItem() != null
                    ? binding.spPaymentMethod.getSelectedItem().toString()
                    : "Cash"; // Default value

            // Input validation
            if (TextUtils.isEmpty(amount)) {
                showToast("Amount is required!");
                return;
            }

            if (TextUtils.isEmpty(date)) {
                showToast("Date is required!");
                return;
            }

            if (TextUtils.isEmpty(category) || category.equals("Uncategorized")) {
                showToast("Please select a valid category!");
                return;
            }

            // Save expense to SharedPreferences
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
                    AddExpense.this, (view, year1, monthOfYear, dayOfMonth) -> {
                // Set selected date to EditText in "dd/MM/yyyy" format
                String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                binding.etDate.setText(selectedDate);
            }, year, month, day);

            // Show DatePickerDialog
            datePickerDialog.show();
        });
    }

    private void saveExpense(String amount, String category, String date, String notes, String paymentMethod) {
        SharedPreferences sharedPreferences = getSharedPreferences("ExpensePrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Try to load the existing list of expenses, or create a new one if not found
        JSONArray expenseArray;
        try {
            String expensesData = sharedPreferences.getString("expenses_list", "[]");
            expenseArray = new JSONArray(expensesData);
        } catch (JSONException e) {
            expenseArray = new JSONArray();
        }

        // Create a new expense object and add it to the list
        JSONObject expenseObj = new JSONObject();
        try {
            expenseObj.put("amount", amount);
            expenseObj.put("category", category);
            expenseObj.put("date", date);
            expenseObj.put("notes", notes);
            expenseObj.put("paymentMethod", paymentMethod);

            // Add the new expense to the existing array
            expenseArray.put(expenseObj);

            // Save the updated list to SharedPreferences
            editor.putString("expenses_list", expenseArray.toString());
            editor.apply();

            // Show confirmation
            String expenseDetails = String.format("Expense Added:\nAmount: %s\nCategory: %s\nDate: %s\nNotes: %s\nPayment Method: %s",
                    amount, category, date, notes, paymentMethod);
            showToast(expenseDetails);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
