package com.example.studytimerappcode;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
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

public class Study extends AppCompatActivity {

    private FirebaseUser user;
    private CountDownTimer studyTimer, breakTimer;
    private FirebaseAuth mAuth;
    private TextView name;
    private Button back,start,stop,pause;
    private TextView decimalStudy,decimalBreak,zecimalStudy,zecimalBreak;
    private long studyTimeLeftInMillis = 1500000 ;
    private long breakTimeLeftInMillis = 300000 ;
    private boolean isStudySession = true;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_study);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
//        set up the firebasae
        mAuth = FirebaseAuth.getInstance();
        name = findViewById(R.id.profile);
        user = mAuth.getCurrentUser();
//        set up the ui
        name.setText(user.getDisplayName());
        back = findViewById(R.id.back);
        start = findViewById(R.id.start);
        stop = findViewById(R.id.stop);
        pause = findViewById(R.id.pause);
        mediaPlayer = MediaPlayer.create(this, R.raw.ringtone);


        decimalBreak = findViewById(R.id.decimalBreak);
        decimalStudy = findViewById(R.id.decimalStudy);
        zecimalBreak = findViewById(R.id.zecimalBreak);
        zecimalStudy = findViewById(R.id.zecimalStudy);

//        set up the buttons
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Study.this, Profile.class);
                startActivity(intent);
                finish();
                }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.start();
                stop.setVisibility(View.VISIBLE);
                pause.setVisibility(View.VISIBLE);
                start.setVisibility(View.INVISIBLE);
                pomodoroStart();
                startPomodoroTimer();
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pomodoroRestart();
                stop.setVisibility(View.INVISIBLE);
                pause.setVisibility(View.INVISIBLE);
                start.setVisibility(View.VISIBLE);

            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pomodoroStop();
            }
        });
    }
    public void pomodoroStart(){

    }
    public void pomodoroRestart(){

    }
    public void pomodoroStop(){

    }
    private void startPomodoroTimer() {
        // Check if it's study session or break session
        if (isStudySession) {
            studyTimer = new CountDownTimer(studyTimeLeftInMillis, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    studyTimeLeftInMillis = millisUntilFinished;
                    updateStudyTime();
                }

                @Override
                public void onFinish() {
                    isStudySession = false; // Switch to break session
                    mediaPlayer.start();
                    startPomodoroTimer(); // Start break timer
                }
            }.start();
        } else {
            breakTimer = new CountDownTimer(breakTimeLeftInMillis, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    breakTimeLeftInMillis = millisUntilFinished;
                    updateBreakTime();
                }

                @Override
                public void onFinish() {
                    isStudySession = true; // Switch to study session
                    mediaPlayer.start();
                    startPomodoroTimer(); // Start study timer again
                }
            }.start();
        }
    }

    // Update study time TextViews
    private void updateStudyTime() {
        int minutes = (int) (studyTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (studyTimeLeftInMillis / 1000) % 60;

        // Extract the tens and units of the minutes and seconds
        int zecimalMinutes = minutes / 10;
        int decimalMinutes = minutes % 10;
        int zecimalSeconds = seconds / 10;
        int decimalSeconds = seconds % 10;

        zecimalStudy.setText(String.valueOf(zecimalMinutes));
        decimalStudy.setText(String.valueOf(decimalMinutes));
        zecimalBreak.setText(String.valueOf(zecimalSeconds));
        decimalBreak.setText(String.valueOf(decimalSeconds));
    }

    // Update break time TextViews
    private void updateBreakTime() {
        int minutes = (int) (breakTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (breakTimeLeftInMillis / 1000) % 60;

        // Extract the tens and units of the minutes and seconds
        int zecimalMinutes = minutes / 10;
        int decimalMinutes = minutes % 10;
        int zecimalSeconds = seconds / 10;
        int decimalSeconds = seconds % 10;

        zecimalStudy.setText(String.valueOf(zecimalMinutes));
        decimalStudy.setText(String.valueOf(decimalMinutes));
        zecimalBreak.setText(String.valueOf(zecimalSeconds));
        decimalBreak.setText(String.valueOf(decimalSeconds));
    }
}
