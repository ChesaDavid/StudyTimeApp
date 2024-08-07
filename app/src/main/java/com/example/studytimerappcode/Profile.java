package com.example.studytimerappcode;

import static java.sql.Types.NULL;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Profile extends AppCompatActivity {

    FirebaseUser user;
    FirebaseAuth mAuth;
    TextView name;
    TextView logout;
    private ImageView calendar,todo,study,profileImg,change,stats;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        calendar = findViewById(R.id.calendar);
        todo = findViewById(R.id.todo);
        study = findViewById(R.id.study);
        profileImg = findViewById(R.id.profileImg);
//        change = findViewById(R.id.change);
        stats = findViewById(R.id.statistics);
        mAuth = FirebaseAuth.getInstance();
        calendar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Calendar.class);
                startActivity(intent);
                finish();
            }
        });
        todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ToDo.class);
                startActivity(intent);
                finish();
            }
        });
        study.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Study.class);
                startActivity(intent);
                finish();
            }
        });
        stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Statistics.class);
                startActivity(intent);
                finish();
            }
        });
        name = findViewById(R.id.profile);
        logout = findViewById(R.id.button);
        user = mAuth.getCurrentUser();
        if(user == null){
            Toast.makeText(this, "No user found", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            if(TextUtils.isEmpty(user.getDisplayName())){
                name.setText(user.getEmail());
            }else {
                name.setText(user.getDisplayName());
            }
        }
    logout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }
    });
    }
}