package Adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.finalprojectg3.databinding.ItemExpenseBinding;
import java.util.ArrayList;
import java.util.List;
import model.Expense;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private static final List<Expense> expenses = new ArrayList<>();
    private static OnItemClickListener onItemClickListener;  // Declare the listener as an instance variable

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemExpenseBinding binding = ItemExpenseBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ExpenseViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        holder.bind(expenses.get(position));
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }

    // Method to submit the expense list to the adapter
    public void submitList(List<Expense> expenseList) {
        expenses.clear();
        expenses.addAll(expenseList);
        notifyDataSetChanged();
    }

    // Method to set the onItemClickListener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    // Interface to define the click listener
    public interface OnItemClickListener {
        void onItemClick(Expense expense);  // Method for handling item click
    }

    // ViewHolder class to bind the expense data
    static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        private final ItemExpenseBinding binding;

        public ExpenseViewHolder(@NonNull ItemExpenseBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            // Set up the click listener in the ViewHolder
            binding.getRoot().setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(expenses.get(getAdapterPosition()));
                }
            });
        }

        // Bind the expense data to the ViewHolder
        public void bind(Expense expense) {
            binding.tvAmount.setText("Amount: " + expense.getAmount());
            binding.tvCategory.setText("Category: " + expense.getCategory());
            binding.tvDate.setText("Date: " + expense.getDate());
            binding.tvNotes.setText("Notes: " + expense.getNotes());
            binding.tvPaymentMethod.setText("Payment Method: " + expense.getPaymentMethod());
        }
    }
}
