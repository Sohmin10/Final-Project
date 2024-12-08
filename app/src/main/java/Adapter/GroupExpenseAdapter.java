package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.finalprojectg3.R;
import model.GroupExpense;

import java.util.List;

public class GroupExpenseAdapter extends RecyclerView.Adapter<GroupExpenseAdapter.GroupExpenseViewHolder> {

    private final Context context;
    private final List<GroupExpense> expenseList;

    public GroupExpenseAdapter(Context context, List<GroupExpense> expenseList) {
        this.context = context;
        this.expenseList = expenseList;
    }

    @NonNull
    @Override
    public GroupExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_group_expense, parent, false);
        return new GroupExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupExpenseViewHolder holder, int position) {
        GroupExpense expense = expenseList.get(position);

        holder.tvExpenseName.setText(expense.getExpenseName());
        holder.tvTotalAmount.setText(String.format("Total Amount: $%.2f", expense.getTotalAmount()));
        holder.tvSplitAmount.setText(String.format("Split Amount: $%.2f", expense.getSplitAmount()));
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public static class GroupExpenseViewHolder extends RecyclerView.ViewHolder {

        TextView tvExpenseName, tvTotalAmount, tvSplitAmount;

        public GroupExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvExpenseName = itemView.findViewById(R.id.tvExpenseName);
            tvTotalAmount = itemView.findViewById(R.id.tvTotalAmount);
            tvSplitAmount = itemView.findViewById(R.id.tvSplitAmount);
        }
    }
}
