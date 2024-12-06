package Adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectg3.databinding.ItemGroupMemberBinding;
import model.GroupMember;

import java.util.ArrayList;

public class GroupMemberAdapter extends RecyclerView.Adapter<GroupMemberAdapter.MemberViewHolder> {

    private final ArrayList<GroupMember> members;
    private final OnMemberDeleteListener deleteListener;

    public interface OnMemberDeleteListener {
        void onDeleteMember(GroupMember member);
    }

    public GroupMemberAdapter(ArrayList<GroupMember> members, OnMemberDeleteListener deleteListener) {
        this.members = members;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemGroupMemberBinding binding = ItemGroupMemberBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new MemberViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        GroupMember member = members.get(position);
        holder.binding.tvMemberName.setText(member.getName());
        holder.binding.btnDeleteMember.setOnClickListener(v -> deleteListener.onDeleteMember(member));
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    static class MemberViewHolder extends RecyclerView.ViewHolder {
        ItemGroupMemberBinding binding;

        public MemberViewHolder(@NonNull ItemGroupMemberBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
