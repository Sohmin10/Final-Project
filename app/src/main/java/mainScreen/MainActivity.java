package mainScreen;

import AddExpense.AddExpense;
import View.ViewExpenses;
import select.SelectTransaction;

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
        binding.btnAddExpense.setOnClickListener(v -> {
            // Navigate to AddExpense activity
            Intent intent = new Intent(MainActivity.this, AddExpense.class);
            startActivity(intent);
        });

        binding.btnViewExpenses.setOnClickListener(v -> {
            // Navigate to ViewExpenses activity
            Intent intent = new Intent(MainActivity.this, ViewExpenses.class);
            startActivity(intent);
        });

        binding.btnAddReminder.setOnClickListener(v -> {
            // Navigate to SelectTransaction activity
            Intent intent = new Intent(MainActivity.this, SelectTransaction.class);
            startActivity(intent);
        });
    }
}
