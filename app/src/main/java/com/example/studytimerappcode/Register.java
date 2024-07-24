package com.example.studytimerappcode;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class Register extends AppCompatActivity {
    private EditText email, name, password;
    private Button submit;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private TextView click;
    private Button exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        exit = findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        name = findViewById(R.id.name);
        submit = findViewById(R.id.submit);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        click = findViewById(R.id.click);

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activity = new Intent(Register.this, LogIn.class);
                startActivity(activity);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailField = email.getText().toString();
                String passField = password.getText().toString();
                String nameField = name.getText().toString();
                submit.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);

                if (TextUtils.isEmpty(emailField) || TextUtils.isEmpty(passField) || TextUtils.isEmpty(nameField)) {
                    Toast.makeText(Register.this, "All fields are required.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    submit.setVisibility(View.VISIBLE);
                    return;
                } else {
                    registerUser(emailField, passField, nameField);
                }
            }
        });
    }

    private void registerUser(final String email, String password, final String nameField) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        submit.setVisibility(View.VISIBLE);
                        if (task.isSuccessful()) {
                            final FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(nameField)
                                        .build();
                                user.updateProfile(profileUpdate)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(Register.this, "Authentication successful.", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(Register.this, "Failed to update profile name.", Toast.LENGTH_SHORT).show();
                                                }
                                                Intent intent = new Intent(getApplicationContext(), Profile.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                            }
                        } else {
                            Toast.makeText(Register.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
