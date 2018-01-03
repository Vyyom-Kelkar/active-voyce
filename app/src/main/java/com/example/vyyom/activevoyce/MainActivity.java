package com.example.vyyom.activevoyce;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.vyyom.activevoyce.database.activevoyce.DatabaseHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> userInfo;
    private DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabaseHelper = new DatabaseHelper(this);
        userInfo = getIntent().getStringArrayListExtra("User");

        Button singlePlayerStartButton = findViewById(R.id.one_player_start_button);
        singlePlayerStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGame();
            }
        });

        Button helpButton = findViewById(R.id.help_button);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHelp();
            }
        });
    }

    private void startGame() {
        if(!mDatabaseHelper.checkCompletion(userInfo.get(0))) {
            mDatabaseHelper.resetGame(userInfo.get(0));
        }
        mDatabaseHelper.getIncompleteVerbs(userInfo.get(0));
        mDatabaseHelper.getIncompletePrepositions(userInfo.get(0));
        Intent intent = new Intent(this, GameActivity.class);
        intent.putStringArrayListExtra("User", userInfo);
        startActivity(intent);
        finish();
    }

    private void launchHelp() {
        Intent intent = new Intent(this, HelpActivity.class);
        intent.putStringArrayListExtra("User",
                getIntent().getStringArrayListExtra("User"));
        startActivity(intent);
        finish();
    }
}
