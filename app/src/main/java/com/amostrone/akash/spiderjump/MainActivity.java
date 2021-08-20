package com.amostrone.akash.spiderjump;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    Game game;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        game =new Game(this);
        game.setBackgroundResource(R.drawable.background);
        setContentView(game);
    }
}