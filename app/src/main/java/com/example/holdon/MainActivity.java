package com.example.holdon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private int score = 0;
    private int highScore = 0;
    private TextView scoreTV;
    private TextView highScoreTV;
    private Button holdBtn;
    private Toast toast = null;

    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            increase();
            displayScore();
            displayHighScore();
            timerHandler.postDelayed(this, 100);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scoreTV = findViewById(R.id.textView2);
        highScoreTV = findViewById(R.id.textView4);
        holdBtn = findViewById(R.id.button);

        loadHighScore();
        displayHighScore();

        holdBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    startCount();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    stopCount();
                }
            return false;
            }
        });
    }

    public void startCount() {
        timerHandler.postDelayed(timerRunnable, 0);
    }

    public void stopCount() {
        timerHandler.removeCallbacks(timerRunnable);
        saveHighScore();
        displayToast();
        this.score = -1;
    }

    public void displayScore() {
        this.scoreTV.setText(Integer.toString(score));
    }

    public void displayHighScore() {
        this.highScoreTV.setText(Integer.toString(this.highScore));
    }

    public void displayToast() {
        String toastString = "Geeez you're so bad!";
        if(this.score >= this.highScore) {
            toastString = "DAAAMN, you beat your HighScore!";
        }
        if(toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(MainActivity.this, toastString,
                Toast.LENGTH_SHORT);
        toast.show();
    }

    public void increase() {
        this.score += 1;
        updateHighScore();
    }

    public void updateHighScore() {
        if(this.score > this.highScore) {
            this.highScore = score;
        }
    }

    public void saveHighScore() {
        SharedPreferences settings = getApplicationContext().getSharedPreferences("HoldOnData", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("highScore", highScore);
        editor.apply();
    }

    public void loadHighScore() {
        SharedPreferences settings = getApplicationContext().getSharedPreferences("HoldOnData", 0);
        this.highScore = settings.getInt("highScore", 0);
    }

}
