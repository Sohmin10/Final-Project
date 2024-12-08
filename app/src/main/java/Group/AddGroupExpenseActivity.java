package Group;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalprojectg3.DatabaseHelper;
import com.example.finalprojectg3.databinding.ActivityAddGroupExpenseBinding;

import java.util.ArrayList;
import java.util.List;

public class AddGroupExpenseActivity extends AppCompatActivity {

    private ActivityAddGroupExpenseBinding binding;
    private DatabaseHelper databaseHelper;
    private List<String> groupList;  // Assuming this list contains group names

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up ViewBinding
        binding = ActivityAddGroupExpenseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseHelper = new DatabaseHelper(this);
        loadGroupSpinner();

        // Set up button listener for adding expense
        binding.btnAddExpense.setOnClickListener(v -> {
            String expenseName = binding.edtExpenseName.getText().toString();
            String totalAmount = binding.edtTotalAmount.getText().toString();

            if (expenseName.isEmpty() || totalAmount.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                addExpenseToDatabase(expenseName, totalAmount);
            }
        });
    }




    private void loadGroupSpinner() {
        // Fetch group names from the database
        groupList = databaseHelper.getGroupNames();  // Get group names from database

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, groupList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerSelectGroup.setAdapter(adapter);
    }

    private void addExpenseToDatabase(String expenseName, String totalAmount) {
        String selectedGroup = binding.spinnerSelectGroup.getSelectedItem().toString();

        // Assuming you have a method in the DatabaseHelper class to add group expense
        boolean success = databaseHelper.addGroupExpense(selectedGroup, expenseName, totalAmount);
        if (success) {
            Toast.makeText(this, "Expense added successfully!", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity after adding the expense
        } else {
            Toast.makeText(this, "Failed to add expense", Toast.LENGTH_SHORT).show();
        }
    }
}