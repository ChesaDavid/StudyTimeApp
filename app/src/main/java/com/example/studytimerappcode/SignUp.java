package com.example.studytimerappcode;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.internal.InternalAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.tracing.FirebaseTrace;
import com.google.firebase.auth.*;

public class SignUp extends AppCompatActivity {
    private EditText email,name,password;
    private Button submit;

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
        email = findViewById(R.id.email);
        name = findViewById(R.id.name);
        password = findViewById(R.id.password);
        submit = findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailField = email.getText().toString();
                String passField = password.getText().toString();
                String nameField = name.getText().toString();
                if(TextUtils.isEmpty(emailField) || TextUtils.isEmpty(passField) || TextUtils.isEmpty(nameField)){
                    Toast.makeText(SignUp.this,"All field are requierd.",Toast.LENGTH_SHORT).show();
                }
                else{
                    registerUser(emailField,passField,nameField);
                }
            }
        });
    }
    private void registerUser(String emai,String password,String name){

    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();

    }
}