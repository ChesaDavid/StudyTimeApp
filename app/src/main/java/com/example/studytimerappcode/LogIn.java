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

public class LogIn extends AppCompatActivity {
    private Button login;
    private EditText password,email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        login = findViewById(R.id.loginbtn);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passwordField = password.getText().toString();
                String emailField = email.getText().toString();
                if(TextUtils.isEmpty(passwordField) || TextUtils.isEmpty(emailField)){
                    Toast.makeText(LogIn.this,"All field are requierd.", Toast.LENGTH_SHORT).show();
                }
                else{
                    loginUser(passwordField,emailField);
                }
            }
        });
    }
    private void loginUser(String email,String password){
//        need to make the login function
    }
}