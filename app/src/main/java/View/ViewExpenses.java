package View;



import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.finalprojectg3.databinding.ActivityViewExpensesBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Adapter.ExpenseAdapter;
import model.Expense;

public class ViewExpenses extends AppCompatActivity {

    private ActivityViewExpensesBinding binding;
    private ExpenseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up ViewBinding
        binding = ActivityViewExpensesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Attach a LayoutManager to RecyclerView
        binding.rvExpenses.setLayoutManager(new LinearLayoutManager(this));

        // Set up adapter
        adapter = new ExpenseAdapter();
        binding.rvExpenses.setAdapter(adapter);

        // Load expenses from SharedPreferences
        loadExpenses();
    }

    private void loadExpenses() {
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

            adapter.submitList(expenses);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
