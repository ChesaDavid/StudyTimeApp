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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToDo extends AppCompatActivity {

    private Button back, buttonAdd;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private TextView name;
    private EditText editTextTask;
    private LinearLayout tasksContainer;
    private DocumentReference userDocRef;

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

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
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

            // Reference to the user's document in Firestore
            userDocRef = db.collection("user").document(user.getUid());

            // Load tasks from Firestore
            loadTasksFromFirestore();

            // Add task button functionality
            buttonAdd.setOnClickListener(v -> {
                String task = editTextTask.getText().toString();
                if (!task.isEmpty()) {
                    // Add task to Firestore (append to the tasks array)
                    addTaskToFirestore(task);
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

    private void loadTasksFromFirestore() {
        userDocRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                List<String> tasks = (List<String>) documentSnapshot.get("tasks");
                if (tasks != null) {
                    tasksContainer.removeAllViews();  // Clear the container before adding tasks
                    for (String task : tasks) {
                        addTaskToUI(task);
                    }
                }
            } else {
                // Document does not exist, you might want to create it here if needed
                Toast.makeText(ToDo.this, "No tasks found.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(ToDo.this, "Failed to load tasks: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void addTaskToFirestore(String task) {
        userDocRef.get().addOnCompleteListener(taskSnapshot -> {
            if (taskSnapshot.isSuccessful()) {
                DocumentSnapshot document = taskSnapshot.getResult();
                if (document.exists()) {
                    // If document exists, update the tasks array

                    userDocRef.update("tasks", FieldValue.arrayUnion(task))
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(ToDo.this, "Task added successfully", Toast.LENGTH_SHORT).show();
                                addTaskToUI(task);  // Add the task to the UI
                                editTextTask.setText("");  // Clear the input field
                            })
                            .addOnFailureListener(e -> {
                                Log.e("ToDoActivity", "Failed to add task: " + e.getMessage());
                                Toast.makeText(ToDo.this, "Failed to add task: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                } else {
                    // If document does not exist, create it with the task array
                    Map<String, Object> userData = new HashMap<>();
                    List<String> tasks = new ArrayList<>();
                    tasks.add(task);
                    userData.put("tasks", tasks);

                    Map<Boolean,Object> userData2 = new HashMap<>();
                    List<Boolean> started = new ArrayList<>();
                    started.add(false);
                    userData2.put(Boolean.valueOf("started"),started);
                    userDocRef.set(userData2)
                                    .addOnCompleteListener(aVoid -> {
                                        Toast.makeText(ToDo.this, "Task added successfully", Toast.LENGTH_SHORT).show();
                                        addTaskToUI(task);  // Add the task to the UI
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("ToDoActivity", "Failed to create document: " + e.getMessage());
                                        Toast.makeText(ToDo.this, "Failed to add task: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });

                    userDocRef.set(userData)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(ToDo.this, "Task added successfully", Toast.LENGTH_SHORT).show();
                                addTaskToUI(task);  // Add the task to the UI
                                editTextTask.setText("");  // Clear the input field
                            })
                            .addOnFailureListener(e -> {
                                Log.e("ToDoActivity", "Failed to create document: " + e.getMessage());
                                Toast.makeText(ToDo.this, "Failed to add task: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                }
            } else {
                Log.e("ToDoActivity", "Failed to check document existence: " + taskSnapshot.getException());
                Toast.makeText(ToDo.this, "Error checking document: " + taskSnapshot.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addTaskToUI(String task) {
        View taskView = getLayoutInflater().inflate(R.layout.task_item, tasksContainer, false);
        TextView taskTextView = taskView.findViewById(R.id.taskText);
        TextView removeButton = taskView.findViewById(R.id.removeTaskButton);
        Button startTask = taskView.findViewById(R.id.startTaskButton);
        taskTextView.setText(task);

        startTask.setOnClickListener(v -> {
            // Start the StartTask activity
            userDocRef.update("started",true)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(ToDo.this, "Congratulations! You've started a task!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ToDo.this, StartTask.class);
                        startActivity(intent);
                        finish();
                            })
                    .addOnFailureListener(e -> {
                        Toast.makeText(ToDo.this, "Failed to start task: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
        });

        removeButton.setOnClickListener(v -> {
            // Remove task from Firestore (remove from the tasks array)
            userDocRef.update("tasks", FieldValue.arrayRemove(task))
                    .addOnSuccessListener(aVoid -> {
                        tasksContainer.removeView(taskView);  // Remove task from UI
                        Toast.makeText(ToDo.this, "Congratulations! You've completed a task!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(ToDo.this, "Failed to remove task: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        tasksContainer.addView(taskView);
    }
}
