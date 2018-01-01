package com.example.vyyom.activevoyce;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.vyyom.activevoyce.database.activevoyce.DatabaseHelper;

public class GameActivity extends AppCompatActivity {

    private String mVerb;
    private String mPreposition;
    private DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mDatabaseHelper = new DatabaseHelper(this);

        Button randomizeVerbButton = findViewById(R.id.random_verb_button);
        randomizeVerbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                randomizeVerb();
            }
        });

        Button randomizePrepositionButton = findViewById(R.id.random_preposition_button);
        randomizePrepositionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                randomizePreposition();
            }
        });

        TextView verbAndPrepositionView = findViewById(R.id.verb_and_preposition_view);
        verbAndPrepositionView.setText(String.format("%s %s", mVerb, mPreposition));
    }

    private void randomizeVerb() {
        this.mVerb = Randomizer.
    }

    private void randomizePreposition() {

    }
}
