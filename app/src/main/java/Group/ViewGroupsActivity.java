package Group;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import Adapter.GroupAdapter;
import com.example.finalprojectg3.DatabaseHelper;
import com.example.finalprojectg3.databinding.ActivityViewGroupsBinding;
import model.Group;

import java.util.ArrayList;

public class ViewGroupsActivity extends AppCompatActivity {

    private ActivityViewGroupsBinding binding;
    private DatabaseHelper databaseHelper;
    private ArrayList<Group> groups;
    private GroupAdapter groupAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewGroupsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseHelper = new DatabaseHelper(this);
        groups = databaseHelper.getGroups();

        groupAdapter = new GroupAdapter(groups, group -> {
            Intent intent = new Intent(ViewGroupsActivity.this, ViewGroupDetailsActivity.class);
            intent.putExtra("group_id", group.getId());
            intent.putExtra("group_name", group.getName());
            startActivity(intent);
        });

        binding.rvGroups.setLayoutManager(new LinearLayoutManager(this));
        binding.rvGroups.setAdapter(groupAdapter);
    }
}