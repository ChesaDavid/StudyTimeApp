package com.example.studytimerappcode;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.internal.InternalAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.tracing.FirebaseTrace;
import com.google.firebase.auth.*;

public class SignUp extends AppCompatActivity {
    private EditText email,name,password;
    private Button submit;
    private  FirebaseAuth mAuth;
    public FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        user = mAuth.getCurrentUser();
        if(user != null){
            reload();
        }
        email = findViewById(R.id.email);
        name = findViewById(R.id.name);
        password = findViewById(R.id.password);
        submit = findViewById(R.id.submit);
        mAuth = FirebaseAuth.getInstance();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailField = email.getText().toString();
                String passField = password.getText().toString();
                String nameField = name.getText().toString();
                if(TextUtils.isEmpty(emailField) || TextUtils.isEmpty(passField) || TextUtils.isEmpty(nameField)){
                    Toast.makeText(SignUp.this,"All field are requierd.",Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    registerUser(emailField,passField,nameField);
                }
            }
        });
    }

    private void reload() {
        FirebaseAuth.getInstance().signOut();
    }

    private void registerUser(String email,String password,String name){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                             FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            Toast.makeText(SignUp.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();

    }
}