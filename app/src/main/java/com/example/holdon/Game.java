package com.example.holdon;

import android.os.Handler;

import java.util.Observable;

public class Game extends Observable {
    private int score = 0; // current Score
    private int highScore;
    private int delay = 100; // delay in milliseconds
    private Handler timerHandler;
    private Runnable timerRunnable;

    public Game(int highScore) {
        timerHandler = new Handler();
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                increase();
                setChanged();
                notifyObservers();
                timerHandler.postDelayed(this, Game.this.delay);
            }
        };
        this.highScore = highScore;
    }

    public boolean reachedHighScore() {
        return this.score >= this.highScore;
    }

    public void startCount() {
        timerHandler.postDelayed(timerRunnable, 0);
        notifyObservers(this);
    }

    public void stopCount() {
        timerHandler.removeCallbacks(timerRunnable);
        this.score = -1;
    }

    private void increase() {
        this.score += 1;
        updateHighScore();
    }

    private void updateHighScore() {
        if(this.score > this.highScore) {
            this.highScore = score;
        }
    }

    public int getScore() {
        return this.score;
    }

    public int getHighScore() {
        return this.highScore;
    }
}
