package Group;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import Adapter.GroupMemberAdapter;
import com.example.finalprojectg3.DatabaseHelper;
import com.example.finalprojectg3.databinding.ActivityViewGroupDetailsBinding;
import model.GroupMember;

import java.util.ArrayList;

public class ViewGroupDetailsActivity extends AppCompatActivity {

    private ActivityViewGroupDetailsBinding binding;
    private DatabaseHelper databaseHelper;
    private ArrayList<GroupMember> members;
    private GroupMemberAdapter memberAdapter;
    private int groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewGroupDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseHelper = new DatabaseHelper(this);

        // Retrieve group ID and name
        groupId = getIntent().getIntExtra("group_id", -1);
        String groupName = getIntent().getStringExtra("group_name");

        if (groupId == -1 || groupName == null) {
            Toast.makeText(this, "Invalid group!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        binding.tvGroupName.setText(groupName);

        // Load members
        members = databaseHelper.getGroupMembers(groupId);
        memberAdapter = new GroupMemberAdapter(members, this::showDeleteMemberConfirmation);

        binding.rvGroupMembers.setLayoutManager(new LinearLayoutManager(this));
        binding.rvGroupMembers.setAdapter(memberAdapter);

        // Add new member
        binding.btnAddMember.setOnClickListener(v -> {
            String newMemberName = binding.etNewMemberName.getText().toString().trim();
            if (!newMemberName.isEmpty()) {
                databaseHelper.addGroupMember(groupId, newMemberName);
                members.clear();
                members.addAll(databaseHelper.getGroupMembers(groupId));
                memberAdapter.notifyDataSetChanged();
                binding.etNewMemberName.setText(""); // Clear input field
                Toast.makeText(this, "Member added!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Enter a member name!", Toast.LENGTH_SHORT).show();
            }
        });

        // Delete group
        binding.btnDeleteGroup.setOnClickListener(v -> showDeleteGroupConfirmation());
    }

    private void showDeleteMemberConfirmation(GroupMember member) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Member")
                .setMessage("Are you sure you want to delete this member?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    databaseHelper.deleteGroupMember(member.getId());
                    members.remove(member);
                    memberAdapter.notifyDataSetChanged();
                    Toast.makeText(this, "Member deleted!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void showDeleteGroupConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Group")
                .setMessage("Are you sure you want to delete the entire group?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    databaseHelper.deleteGroup(groupId);
                    Toast.makeText(this, "Group deleted!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton("No", null)
                .show();
    }
}