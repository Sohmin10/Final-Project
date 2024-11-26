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

    private final List<Expense> expenses = new ArrayList<>();

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

    public void submitList(List<Expense> expenseList) {
        expenses.clear();
        expenses.addAll(expenseList);
        notifyDataSetChanged();
    }

    static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        private final ItemExpenseBinding binding;

        public ExpenseViewHolder(@NonNull ItemExpenseBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Expense expense) {
            binding.tvAmount.setText("Amount: " + expense.getAmount());
            binding.tvCategory.setText("Category: " + expense.getCategory());
            binding.tvDate.setText("Date: " + expense.getDate());
            binding.tvNotes.setText("Notes: " + expense.getNotes());
            binding.tvPaymentMethod.setText("Payment Method: " + expense.getPaymentMethod());
        }
    }
}
