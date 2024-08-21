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
    public void loadWhichTaskIsStarted(){
        userDocRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                List<Boolean> started = (List<Boolean>) documentSnapshot.get("started");
                if (started != null) {
                    for(Boolean bool : started){
                        if(bool){
                            taskTest.setText("task");
                        }
                    };
                }else{
                    Toast.makeText(StartTask.this, "No tasks found.", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(StartTask.this, "No tasks found.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}