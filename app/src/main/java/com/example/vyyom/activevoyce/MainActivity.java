package com.example.vyyom.activevoyce;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vyyom.activevoyce.database.activevoyce.DatabaseHelper;

import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    private User userInfo;
    private DatabaseHelper mDatabaseHelper;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabaseHelper = new DatabaseHelper(this);
        //noinspection ConstantConditions
        userInfo = (User) getIntent().getExtras().get("User");

        TextView welcomeTextView = findViewById(R.id.welcome_screen_text);
        welcomeTextView.setText("Welcome back " + userInfo.getUserName() + " !");

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
        if(mDatabaseHelper.checkCompletion(userInfo.getUserName())) {
            mDatabaseHelper.resetGame(userInfo);
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String user = sharedPreferences.getString("User", null);
        if(user != null && user.equalsIgnoreCase(userInfo.getUserName())) {
            userInfo.addCompletedWords(sharedPreferences.getStringSet("Words", new HashSet<String>()));
            userInfo.setCurrentScore(sharedPreferences.getInt("CurrentScore", 0));
            mDatabaseHelper.getSavedGame(userInfo);
        } else {
            mDatabaseHelper.getIncompleteVerbs(userInfo.getUserName());
            mDatabaseHelper.getIncompletePrepositions(userInfo.getUserName());
            mDatabaseHelper.getIncompleteWords(userInfo.getUserName());
        }
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("User", userInfo);
        startActivity(intent);
        finish();
    }

    private void launchHelp() {
        Intent intent = new Intent(this, HelpActivity.class);
        intent.putExtra("User", userInfo);
        startActivityForResult(intent, 0);
        finish();
    }

    // Pressing back twice to exit
    private boolean doubleBackPressed = false;

    @Override
    public void onBackPressed() {
        if(doubleBackPressed) {
            super.onBackPressed();
        }

        this.doubleBackPressed = true;
        Toast.makeText(this, "Please press BACK again to exit!", Toast.LENGTH_SHORT)
                .show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackPressed = false;
            }
        }, 2000);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(resultCode) {
            case 0:
                this.setResult(0);
                this.finish();
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
