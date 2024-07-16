package com.example.studytimerappcode;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Guest extends AppCompatActivity {
    private TextView hourText,minText,secText;
    private Button startBtn,restartBtn,stopBtn;

    private Handler handler = new Handler();
    private long startTimer = 0L, timeMiliseconds = 0L,timeSwampBuff = 0L, updateTime = 0L;

    private Runnable updateTimerThread = new Runnable() {
        @Override
        public void run() {
            timeMiliseconds = System.currentTimeMillis() - startTimer;
            updateTime = timeSwampBuff + timeMiliseconds;
            int sec = (int)(updateTime/1000);
            int min = sec/60;
            sec %= 60;
            int hour = min/60;
            min %= 60;
            int miliseconds = (int)(updateTime % 1000);
            hourText.setText(String.format("%02d",hour));
            minText.setText(String.format("%02d",min));
            secText.setText(String.format("%02d",sec));
            handler.postDelayed(this,0);

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_guest);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        hourText = findViewById(R.id.hour);
        secText = findViewById(R.id.sec);
        minText = findViewById(R.id.min);
        startBtn = findViewById(R.id.start);
        restartBtn = findViewById(R.id.restart);
        stopBtn = findViewById(R.id.stop);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer = System.currentTimeMillis();
                handler.postDelayed(updateTimerThread,0);
            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeSwampBuff += timeMiliseconds;
                handler.removeCallbacks(updateTimerThread);
            }
        });
        Toast toast = Toast.makeText(this,"You have studied for: " + hourText.getText() + "h " + minText.getText() + "min "+ secText.getText() + "s",Toast.LENGTH_SHORT);
        restartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer = 0L;
                timeSwampBuff = 0L;
                timeMiliseconds = 0L;
                updateTime = 0L;
                toast.show();
                hourText.setText("00");
                minText.setText("00");
                secText.setText("00");
            }
        });
    }
}