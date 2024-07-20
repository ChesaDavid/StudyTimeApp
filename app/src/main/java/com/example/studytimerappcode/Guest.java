package com.example.studytimerappcode;

import android.annotation.SuppressLint;
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

    public Toast toast;

    private Handler handler = new Handler();
    private long startTimer = 0L, timeMiliseconds = 0L,timeSwampBuff = 0L, updateTime = 0L;

    int sec,min,hour;
    private Runnable updateTimerThread = new Runnable() {
        @Override
        public void run() {
            timeMiliseconds = System.currentTimeMillis() - startTimer;
            updateTime = timeSwampBuff + timeMiliseconds;
            sec = (int)(updateTime/1000);
            min = sec/60;
            sec %= 60;
            hour = min/60;
            min %= 60;
            int miliseconds = (int)(updateTime % 1000);
            hourText.setText(String.format("%02d",hour));
            minText.setText(String.format("%02d",min));
            secText.setText(String.format("%02d",sec));
            handler.postDelayed(this,0);
        }
    };

    private Runnable updateToast = new Runnable() {
        @Override
        public void run() {
            toast = Toast.makeText(Guest.this,"You have studied for: " + hour + " hours " + min + " minutes " + sec + " seconds",Toast.LENGTH_SHORT);
        }
    };

    @SuppressLint("MissingInflatedId")
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
        handler.postDelayed(updateToast,0);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer = System.currentTimeMillis();
                handler.postDelayed(updateTimerThread,0);
                handler.postDelayed(updateToast,0);
                restartBtn.setVisibility(View.VISIBLE);
                stopBtn.setVisibility(View.VISIBLE);
                startBtn.setVisibility(View.INVISIBLE);
            }
        });

        restartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(restartBtn.getText().equals("Pause")){
                    restartBtn.setText("Restart");
                    timeSwampBuff += timeMiliseconds;
                    handler.removeCallbacks(updateTimerThread);
                    getToast();
                }
                else{
                    restartBtn.setText("Pause");
                    startTimer = System.currentTimeMillis();
                    handler.postDelayed(updateTimerThread,0);
                    handler.postDelayed(updateToast,0);
                }

            }
        });
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeSwampBuff += timeMiliseconds;
                handler.removeCallbacks(updateTimerThread);
                getToast();
                startTimer = 0L;
                timeSwampBuff = 0L;
                timeMiliseconds = 0L;
                updateTime = 0L;
                min = 0;
                sec = 0;
                hour=0;
                hourText.setText("00");
                minText.setText("00");
                secText.setText("00");
                restartBtn.setVisibility(View.INVISIBLE);
                stopBtn.setVisibility(View.INVISIBLE);
                startBtn.setVisibility(View.VISIBLE);
            }
        });
    }
    private void getToast(){
        String hour = hourText.getText() + "hour ";
        String minute = minText.getText() + "minute ";
        String second = secText.getText() + "second ";
        String massage = "You have studied for: " + hour + minute + second;
        Toast toast1 = Toast.makeText(this,massage,Toast.LENGTH_SHORT);
        toast1.show();
    }
}