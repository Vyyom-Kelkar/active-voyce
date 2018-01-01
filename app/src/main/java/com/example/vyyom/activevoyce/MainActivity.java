package com.example.vyyom.activevoyce;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        Intent intent = new Intent(this, GameActivity.class);
        intent.putStringArrayListExtra("User",
                getIntent().getStringArrayListExtra("User"));
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
