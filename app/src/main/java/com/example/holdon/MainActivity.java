package com.example.holdon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity implements Observer {

    private TextView scoreTV;
    private TextView highScoreTV;
    private Button holdBtn;
    private Toast toast = null;
    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        game = new Game(loadHighScore());
        game.addObserver(this);
        scoreTV = findViewById(R.id.textView2);
        highScoreTV = findViewById(R.id.textView4);
        holdBtn = findViewById(R.id.button);

        displayHighScore();

        holdBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    game.startCount();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    displayToast();
                    game.stopCount();
                    saveHighScore();
                }
            return false;
            }
        });
    }

    public void displayScore() {
        this.scoreTV.setText(Integer.toString(game.getScore()));
    }

    public void displayHighScore() {
        this.highScoreTV.setText(Integer.toString(game.getHighScore()));
    }

    public void displayToast() {
        String toastString = "Geeez you're so bad!";
        if(game.reachedHighScore()) {
            toastString = "DAAAMN, you beat your HighScore!";
        }
        if(toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(MainActivity.this, toastString,
                Toast.LENGTH_SHORT);
        toast.show();
    }


    public void saveHighScore() {
        SharedPreferences settings = getApplicationContext().getSharedPreferences("HoldOnData", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("highScore", game.getHighScore());
        editor.apply();
    }

    public int loadHighScore() {
        SharedPreferences settings = getApplicationContext().getSharedPreferences("HoldOnData", 0);
        return settings.getInt("highScore", 0);
    }

    @Override
    public void update(Observable o, Object arg) {
        displayScore();
        displayHighScore();
    }
}
