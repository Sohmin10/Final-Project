package Group;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.finalprojectg3.DatabaseHelper;
import com.example.finalprojectg3.databinding.ActivityViewGroupExpensesBinding;

import java.util.ArrayList;
import java.util.List;

import Adapter.MembersAdapter;
import model.GroupExpense;

public class ViewGroupExpensesActivity extends AppCompatActivity {

    private ActivityViewGroupExpensesBinding binding;
    private MembersAdapter adapter;
    private DatabaseHelper databaseHelper;
    private List<String> groupNames;
    private List<Integer> groupIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up ViewBinding
        binding = ActivityViewGroupExpensesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Load group data into spinner
        loadGroupSpinner();

        // Set spinner listener
        binding.spinnerSelectGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) { // Ignore the "Select a group" placeholder
                    int selectedGroupId = groupIds.get(position - 1); // Adjust for placeholder
                    displayGroupExpenses(selectedGroupId);
                } else {
                    clearExpenseDisplay();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                clearExpenseDisplay();
            }
        });
    }

    private void loadGroupSpinner() {
       // Fetch group names and IDs from the database
        groupNames = new ArrayList<>();
        groupIds = new ArrayList<>();

        // Placeholder for the spinner
        groupNames.add("Select a group");

        List<DatabaseHelper.ABC> groups = databaseHelper.getGroup(); // Update method in DatabaseHelper
        for (DatabaseHelper.ABC group : groups) {
            groupNames.add(group.getGroupName());
            groupIds.add(group.getGroupId());
        }

        // Set up the spinner adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, groupNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerSelectGroup.setAdapter(adapter);
    }

    private void displayGroupExpenses(int groupId) {
        // Fetch all expenses for the selected group
        List<GroupExpense> groupExpenses = databaseHelper.getExpensesByGroupId(groupId);

        // Calculate the total expense for the group
        double totalExpense = 0.0;
        for (GroupExpense expense : groupExpenses) {
            totalExpense += expense.getTotalAmount();
        }

        // Fetch members with their split amounts
        List<String> membersWithSplit = databaseHelper.getMembersWithSplit(groupId);

        // Update the total expense TextView
        binding.totalExpenseTextView.setText("Total Expense: $" + String.format("%.2f", totalExpense));

        // Ensure LayoutManager is set for the RecyclerView
        if (binding.membersListView.getLayoutManager() == null) {
            binding.membersListView.setLayoutManager(new LinearLayoutManager(this));
        }

        // Set up the RecyclerView with the adapter
        adapter = new MembersAdapter(this, membersWithSplit);
        binding.membersListView.setAdapter(adapter);

        // Notify the adapter of data changes
        adapter.notifyDataSetChanged();
    }



    private void clearExpenseDisplay() {
        binding.totalExpenseTextView.setText("Total Expense: $0.00");
        binding.membersListView.setAdapter(null);
    }
}