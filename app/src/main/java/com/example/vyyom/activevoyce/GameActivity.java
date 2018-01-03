package com.example.vyyom.activevoyce;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.vyyom.activevoyce.database.activevoyce.DatabaseHelper;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

public class GameActivity extends AppCompatActivity {

    private DatabaseHelper mDatabaseHelper;
    private String mUserName;
    private ArrayList<String> userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mDatabaseHelper = new DatabaseHelper(this);
        userInfo = getIntent().getStringArrayListExtra("User");
        mUserName = userInfo.get(0);

        for(int i= 0; i < DatabaseHelper.VERBS.size() - 1; i++) {
            System.out.println(DatabaseHelper.VERBS.get(i));
        }
        for(int i= 0; i < DatabaseHelper.PREPOSITIONS.size() - 1; i++) {
            System.out.println(DatabaseHelper.PREPOSITIONS.get(i));
        }

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_play:
                        Intent intent = new Intent(GameActivity.this, GameActivity.class);
                        intent.putStringArrayListExtra("User", userInfo);
                        startActivity(intent);
                        return true;
                    case R.id.navigation_view:
                        Intent intent1 = new Intent(GameActivity.this, GameActivity.class);
                        intent1.putStringArrayListExtra("User", userInfo);
                        startActivity(intent1);
                        return true;
                }
                return false;
            }
        });

        final TextView verbTextView = findViewById(R.id.verb_text_view);
        final TextView prepositionTextView = findViewById(R.id.preposition_text_view);

        EditText editTextView = findViewById(R.id.enter_word_field);

        Button verbButton = findViewById(R.id.random_verb_button);
        verbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(prepositionTextView.getText().toString().matches("")) {
                    verbTextView.setText(DatabaseHelper.VERBS
                            .get(new Random().nextInt(DatabaseHelper.VERBS.size() - 1)));
                } else {
                    Vector<String> availableVerbs = new Vector<>();
                    Vector<String> verbVector =
                            mDatabaseHelper.getVerbsForPreposition(prepositionTextView
                                    .getText().toString());
                    for(String x : verbVector) {
                        if(DatabaseHelper.VERBS.contains(x)) {
                            availableVerbs.add(x);
                        }
                    }
                    verbTextView.setText(availableVerbs
                            .get(new Random().nextInt(availableVerbs.size() - 1)));
                }
            }
        });

        Button prepositionButton = findViewById(R.id.random_preposition_button);
        prepositionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(verbTextView.getText().toString().matches("")) {
                    prepositionTextView.setText(DatabaseHelper.PREPOSITIONS
                    .get(new Random().nextInt(DatabaseHelper.VERBS.size() - 1)));
                } else {
                    Vector<String> availablePrepositions = new Vector<>();
                    Vector<String> prepositionVector =
                            mDatabaseHelper.getPrepositionsForVerb(verbTextView
                                    .getText().toString());
                    for(String x : prepositionVector) {
                        if(DatabaseHelper.PREPOSITIONS.contains(x)) {
                            availablePrepositions.add(x);
                        }
                    }
                    prepositionTextView.setText(availablePrepositions
                            .get(new Random().nextInt(availablePrepositions.size() - 1)));
                }
            }
        });

        Button enterButton = findViewById(R.id.enter_button);
    }
}
