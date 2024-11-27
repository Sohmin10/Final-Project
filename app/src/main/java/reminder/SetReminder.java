package reminder;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalprojectg3.R;
import com.example.finalprojectg3.databinding.ActivitySetReminderBinding;

import java.util.Calendar;

import model.Expense;

public class SetReminder extends AppCompatActivity {

    private ActivitySetReminderBinding binding;
    private Expense expense;

    private static final String CHANNEL_ID = "ExpenseReminderChannel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySetReminderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Notification Channel (required for Android 8.0+)
        createNotificationChannel();

        // Retrieve selected expense from intent
        expense = (Expense) getIntent().getSerializableExtra("expense");

        binding.tvExpenseDetails.setText("Amount: " + expense.getAmount() + "\nNotes: " + expense.getNotes());

        // Set up date picker
        binding.etDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, monthOfYear, dayOfMonth) -> {
                String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                binding.etDate.setText(selectedDate);
            }, year, month, day);

            datePickerDialog.show();
        });

        // Set up time picker
        binding.etTime.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute1) -> {
                String selectedTime = hourOfDay + ":" + minute1;
                binding.etTime.setText(selectedTime);
            }, hour, minute, true);

            timePickerDialog.show();
        });

        binding.btnAddReminder.setOnClickListener(v -> {
            String date = binding.etDate.getText().toString();
            String time = binding.etTime.getText().toString();

            if (date.isEmpty() || time.isEmpty()) {
                Toast.makeText(this, "Please select a date and time", Toast.LENGTH_SHORT).show();
                return;
            }

            // Trigger notification on selected date and time
            showNotification(date, time);
        });
    }

    private void createNotificationChannel() {
        // Create Notification Channel for Android 8.0 and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Expense Reminder Channel";
            String description = "Channel for expense reminders";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Register the channel with the system
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private void showNotification(String date, String time) {
        // Create notification content
        String message = "Reminder for transaction of '$" + expense.getAmount() + "'\nNotes: " + expense.getNotes();

        Notification notification = new Notification.Builder(this, CHANNEL_ID)
                .setContentTitle("Expense Reminder")
                .setContentText(message)
                .setSmallIcon(android.R.drawable.ic_dialog_info) // Use default system icon
                .setPriority(Notification.PRIORITY_DEFAULT)
                .build();


        // Show notification immediately
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(0, notification);  // 0 is the notification ID
        }

        // Toast to show reminder has been set
        Toast.makeText(this, "Reminder set for: " + date + " " + time, Toast.LENGTH_SHORT).show();
    }
}
