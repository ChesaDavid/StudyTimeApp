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

public class StartTask extends AppCompatActivity {

    private TextView taskTest;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private DocumentReference userDocRef;
    private Button abort,start,pause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_start_task);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //declare the firebase connection
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();
        //reference to the user's document in Firestore
        userDocRef = db.collection("user").document(user.getUid());

        loadWhichTaskIsStarted();

        abort = findViewById(R.id.abort);
        start = findViewById(R.id.start);
        pause = findViewById(R.id.pause);

        abort.setOnClickListener(v -> {
            Intent intent = new Intent(StartTask.this, ToDo.class);
            startActivity(intent);
            finish();
        });
    }
//    need to remake the logic
    public void loadWhichTaskIsStarted() {
        userDocRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Safely cast the 'tasks' field as a list of maps
                List<Map<String, Object>> tasks = (List<Map<String, Object>>) documentSnapshot.get("tasks");

                // Check if tasks are not null
                if (tasks != null) {
                    for (Map<String, Object> taskData : tasks) {
                        // Check if taskData contains the 'started' field and it's of type Boolean
                        if (taskData.containsKey("started") && taskData.get("started") instanceof Boolean) {
                            boolean started = (Boolean) taskData.get("started");

                            // If this task is started, set it as the active task
                            if (started) {
                                // Safely retrieve the taskName
                                String taskName = (String) taskData.get("taskName");
                                if (taskName != null) {
                                    taskTest.setText(taskName);  // Set the task name in the TextView
                                } else {
                                    Toast.makeText(StartTask.this, "Task name is missing", Toast.LENGTH_SHORT).show();
                                }
                                return;  // Exit the loop after finding the started task
                            }
                        } else {
                            Toast.makeText(StartTask.this, "Invalid task data", Toast.LENGTH_SHORT).show();
                        }
                    }

                    // If no task was started
                    Toast.makeText(StartTask.this, "No task is currently started.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(StartTask.this, "No tasks found.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(StartTask.this, "Document does not exist.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(StartTask.this, "Failed to load tasks: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("StartTask", "Failed to load tasks: ", e);
        });
    }

}