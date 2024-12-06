package Group;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalprojectg3.DatabaseHelper;
import com.example.finalprojectg3.databinding.ActivityCreateGroupBinding;

import java.util.ArrayList;

public class CreateGroupActivity extends AppCompatActivity {

    private ActivityCreateGroupBinding binding;
    private DatabaseHelper databaseHelper;
    private ArrayList<String> memberNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateGroupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseHelper = new DatabaseHelper(this);
        memberNames = new ArrayList<>();

        // Add member to the list
        binding.btnAddMember.setOnClickListener(v -> {
            String memberName = binding.etMemberName.getText().toString().trim();
            if (!memberName.isEmpty()) {
                memberNames.add(memberName);
                Toast.makeText(this, "Member added: " + memberName, Toast.LENGTH_SHORT).show();
                binding.etMemberName.setText(""); // Clear input field
            } else {
                Toast.makeText(this, "Enter a member name!", Toast.LENGTH_SHORT).show();
            }
        });

        // Save group with members
        binding.btnSaveGroup.setOnClickListener(v -> {
            String groupName = binding.etGroupName.getText().toString().trim();

            if (groupName.isEmpty()) {
                Toast.makeText(this, "Enter a group name!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (memberNames.isEmpty()) {
                Toast.makeText(this, "Add at least one member!", Toast.LENGTH_SHORT).show();
                return;
            }

            long groupId = databaseHelper.addGroup(groupName);
            if (groupId != -1) {
                for (String member : memberNames) {
                    databaseHelper.addGroupMember((int) groupId, member);
                }
                Toast.makeText(this, "Group created successfully!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to create group!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}