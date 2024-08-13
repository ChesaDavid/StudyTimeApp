package com.example.studytimerappcode;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ToDo extends AppCompatActivity {

    private Button back, buttonAdd;
    private FirebaseDatabase database;
    private DatabaseReference tasksRef;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private TextView name;
    private EditText editTextTask;
    private LinearLayout tasksContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_to_do);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Firebase Auth and Database
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        user = mAuth.getCurrentUser();

        // Initialize UI elements
        back = findViewById(R.id.buttonProfile);
        buttonAdd = findViewById(R.id.buttonAdd);
        editTextTask = findViewById(R.id.editTextTask);
        tasksContainer = findViewById(R.id.tasksContainer);
        name = findViewById(R.id.name);

        // Check user authentication and set user's display name
        if (user != null) {
            name.setText("Hello, " + user.getDisplayName());

            // Reference to the user's tasks in the database
            tasksRef = database.getReference("tasks").child(user.getUid());

            // Load tasks from Firebase
            loadTasksFromDatabase();

            // Add task button functionality
            buttonAdd.setOnClickListener(v -> {
                String task = editTextTask.getText().toString();
                if (!task.isEmpty()) {
                    String taskId = tasksRef.push().getKey();  // Generate a unique ID for the task
                    Map<String, Object> taskData = new HashMap<>();
                    taskData.put("task", task);

                    Log.d("ToDoActivity", "Adding task with ID: " + taskId);

                    tasksRef.child(taskId).setValue(taskData)
                            .addOnSuccessListener(aVoid -> {
                                // Write was successful
                                Toast.makeText(ToDo.this, "Task added successfully", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                // Write failed
                                Log.e("ToDoActivity", "Failed to add task: " + e.getMessage());
                                Toast.makeText(ToDo.this, "Failed to add task: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                    addTaskToUI(taskId, task);
                    editTextTask.setText("");  // Clear the input field
                }
            });

        } else {
            Toast.makeText(ToDo.this, "User not authenticated", Toast.LENGTH_SHORT).show();
            // Optionally, redirect to login or main activity
            Intent intent = new Intent(ToDo.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Navigate back to Profile activity
        back.setOnClickListener(v -> {
            Intent intent = new Intent(ToDo.this, Profile.class);
            startActivity(intent);
            finish();
        });
    }

    private void loadTasksFromDatabase() {
        tasksRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tasksContainer.removeAllViews();  // Clear the container before adding tasks
                for (DataSnapshot taskSnapshot : dataSnapshot.getChildren()) {
                    String taskId = taskSnapshot.getKey();
                    String task = taskSnapshot.child("task").getValue(String.class);
                    addTaskToUI(taskId, task);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ToDo.this, "Failed to load tasks: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addTaskToUI(String taskId, String task) {
        View taskView = getLayoutInflater().inflate(R.layout.task_item, tasksContainer, false);
        TextView taskTextView = taskView.findViewById(R.id.taskText);
        Button removeButton = taskView.findViewById(R.id.removeTaskButton);
        TextView startTask = taskView.findViewById(R.id.startTaskButton);
        taskTextView.setText(task);
        startTask.setOnClickListener(v -> {

            // Implement task start functionality if needed
        });

        removeButton.setOnClickListener(v -> {
            tasksRef.child(taskId).removeValue()  // Remove task from Firebase
                    .addOnSuccessListener(aVoid -> {
                        // Remove task from UI
                        tasksContainer.removeView(taskView);
                        Toast.makeText(ToDo.this, "Congratulations! You've completed a task!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(ToDo.this, "Failed to remove task: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        tasksContainer.addView(taskView);
    }
}
