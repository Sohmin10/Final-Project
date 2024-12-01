package select;
import editExpense.UpdateExpenseActivity;
import reminder.SetReminder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.finalprojectg3.R;
import com.example.finalprojectg3.databinding.ActivitySelectTransactionBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Adapter.ExpenseAdapter;
import model.Expense;
import reminder.SetReminder;

public class SelectTransaction extends AppCompatActivity {

    private ActivitySelectTransactionBinding binding;
    private ExpenseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectTransactionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up RecyclerView
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ExpenseAdapter();
        binding.recyclerView.setAdapter(adapter);

        // Set item click listener for the adapter
        adapter.setOnItemClickListener(new ExpenseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Expense expense, int position) {
                // Handle regular item click
                Intent intent = new Intent(SelectTransaction.this, SetReminder.class);
                intent.putExtra("expense", expense);
                startActivity(intent);
            }

            @Override
            public void onEditClick(Expense expense, int position) {
                // Handle edit button click
                if (position != -1) { // Make sure the position is valid
                    Intent intent = new Intent(SelectTransaction.this, UpdateExpenseActivity.class);
                    intent.putExtra("expense", expense); // Pass the selected expense
                    intent.putExtra("position", position); // Pass the valid position
                    startActivity(intent);
                } else {
                    Toast.makeText(SelectTransaction.this, "Invalid position", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Load the expenses from SharedPreferences
        loadExpenses();
    }

    private void loadExpenses() {
        // Load expenses from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("ExpensePrefs", MODE_PRIVATE);
        String expensesData = sharedPreferences.getString("expenses_list", "[]");

        try {
            JSONArray expenseArray = new JSONArray(expensesData);
            List<Expense> expenses = new ArrayList<>();

            for (int i = 0; i < expenseArray.length(); i++) {
                JSONObject expenseObj = expenseArray.getJSONObject(i);
                expenses.add(new Expense(
                        expenseObj.getString("amount"),
                        expenseObj.getString("category"),
                        expenseObj.getString("date"),
                        expenseObj.getString("notes"),
                        expenseObj.getString("paymentMethod")
                ));
            }

            // Check if there are expenses to display
            if (expenses.isEmpty()) {
                binding.tvNoExpenses.setVisibility(View.VISIBLE);
                Toast.makeText(this, "No expenses to display", Toast.LENGTH_SHORT).show();
            } else {
                binding.tvNoExpenses.setVisibility(View.GONE);
            }

            // Submit expenses list to adapter
            adapter.submitList(expenses);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading expenses", Toast.LENGTH_SHORT).show();
        }
    }
}
