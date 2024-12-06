package mainScreen;

import AddExpense.AddExpense;
import View.ViewExpenses;
import select.SelectTransaction;
import Group.CreateGroupActivity;
import Group.ViewGroupsActivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.finalprojectg3.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up ViewBinding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up button listeners

        // Add Expense
        binding.btnAddExpense.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddExpense.class);
            startActivity(intent);
        });

        // View Expenses
        binding.btnViewExpenses.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ViewExpenses.class);
            startActivity(intent);
        });

        // Set Reminder
        binding.btnAddReminder.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SelectTransaction.class);
            startActivity(intent);
        });

        // Create Group
        binding.btnCreateGroup.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CreateGroupActivity.class);
            startActivity(intent);
        });

        // View Groups
        binding.btnViewGroups.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ViewGroupsActivity.class);
            startActivity(intent);
        });
    }
}