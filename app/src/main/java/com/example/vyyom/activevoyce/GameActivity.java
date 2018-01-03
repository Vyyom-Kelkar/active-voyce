package com.example.vyyom.activevoyce;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vyyom.activevoyce.database.activevoyce.DatabaseHelper;

import java.util.Vector;

public class GameActivity extends AppCompatActivity {

    private DatabaseHelper mDatabaseHelper;
    @SuppressWarnings("ConstantConditions")
    private User mUser;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mDatabaseHelper = new DatabaseHelper(this);
        //noinspection ConstantConditions
        mUser = (User) getIntent().getExtras().get("User");

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_play:
                        Intent intent = new Intent(GameActivity.this, GameActivity.class);
                        intent.putExtra("User", mUser);
                        startActivity(intent);
                        return true;
                    case R.id.navigation_view:
                        Intent intent1 = new Intent(GameActivity.this, GameActivity.class);
                        intent1.putExtra("User", mUser);
                        startActivity(intent1);
                        return true;
                }
                return false;
            }
        });

        final TextView verbTextView = findViewById(R.id.verb_text_view);
        final TextView prepositionTextView = findViewById(R.id.preposition_text_view);
        final EditText editTextView = findViewById(R.id.enter_word_field);
        final TextView infoTextView = findViewById(R.id.info_view);

        infoTextView.setText(getString(R.string.score_text) +
                mUser.getCurrentScore() +
                "\n" + getString(R.string.words_remaining_text) +
                DatabaseHelper.WORDS.size() + "\n\n" + mDatabaseHelper.printLists());

        Button verbButton = findViewById(R.id.random_verb_button);
        verbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(prepositionTextView.getText().toString().matches("")) {
                    verbTextView.setText(DatabaseHelper.VERBS
                            .get(Randomizer.getRandom(DatabaseHelper.VERBS.size())));
                } else {
                    Vector<String> availableVerbs = new Vector<>();
                    Vector<String> verbVector =
                            mDatabaseHelper.getVerbsForPreposition(prepositionTextView
                                    .getText().toString());
                    for(String x : verbVector) {
                        if(DatabaseHelper.VERBS.contains(x)
                                && DatabaseHelper.WORDS
                                .contains(mDatabaseHelper.getWord(x,
                                        prepositionTextView.getText().toString()))) {
                            availableVerbs.add(x);
                        }
                    }
                    verbTextView.setText(availableVerbs
                            .get(Randomizer.getRandom(availableVerbs.size())));
                }
            }
        });

        final Button prepositionButton = findViewById(R.id.random_preposition_button);
        prepositionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(verbTextView.getText().toString().matches("")) {
                    prepositionTextView.setText(DatabaseHelper.PREPOSITIONS
                    .get(Randomizer.getRandom(DatabaseHelper.PREPOSITIONS.size())));
                } else {
                    Vector<String> availablePrepositions = new Vector<>();
                    Vector<String> prepositionVector =
                            mDatabaseHelper.getPrepositionsForVerb(verbTextView
                                    .getText().toString());
                    for(String x : prepositionVector) {
                        if(DatabaseHelper.PREPOSITIONS.contains(x)
                                && DatabaseHelper.WORDS
                                .contains(mDatabaseHelper.getWord(verbTextView.getText().toString(),
                                        x))) {
                            availablePrepositions.add(x);
                        }
                    }
                    prepositionTextView.setText(availablePrepositions
                            .get(Randomizer.getRandom(availablePrepositions.size())));
                }
            }
        });

        final Button enterButton = findViewById(R.id.enter_button);
        enterButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if(VerbPrepositionPairIsEmpty()) {
                    Toast.makeText(GameActivity.this,
                            R.string.premature_entry, Toast.LENGTH_SHORT)
                            .show();
                } else {
                    if(isCorrect()) {
                        Toast.makeText(GameActivity.this,
                                R.string.correct_entry, Toast.LENGTH_SHORT)
                                .show();
                        mUser.setCurrentScore(mUser.getCurrentScore() + 1);
                        if(!isSynonym(editTextView.getText().toString())) {
                            mUser.addCompletedWord(editTextView.getText().toString());
                        } else {
                            String word = mDatabaseHelper.getWord(editTextView.getText().toString());
                            mUser.addCompletedWord(word);
                        }
                        mDatabaseHelper.updateAfterCorrectEntry(editTextView.getText().toString());
                        infoTextView.setText(getString(R.string.score_text) +
                                mUser.getCurrentScore() +
                                "\n" + getString(R.string.words_remaining_text) +
                                DatabaseHelper.WORDS.size() + "\n" + mDatabaseHelper.printLists());
                        verbTextView.setText("");
                        prepositionTextView.setText("");
                        editTextView.setText("");
                    } else {
                        Toast.makeText(GameActivity.this,
                                R.string.incorrect_entry, Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            }

            private boolean isCorrect() {
                return isWord(editTextView.getText().toString()) ||
                        isSynonym(editTextView.getText().toString());
            }

            private boolean isWord(String entry) {
                if(DatabaseHelper.WORDS.contains(entry)) {
                    Pair<String, String> pair =
                            new Pair<>(verbTextView.getText().toString(),
                                    prepositionTextView.getText().toString());
                    return pair.equals(mDatabaseHelper.getVerbPrepositionPair(entry));
                } else {
                    return false;
                }
            }

            private boolean isSynonym(String entry) {
                String verb = verbTextView.getText().toString();
                String preposition = prepositionTextView.getText().toString();
                String word = mDatabaseHelper.getWord(verb, preposition);
                System.out.println(word);
                if(mDatabaseHelper.hasSynonyms(word)) {
                    String[] synonyms = mDatabaseHelper.getSynonyms(word);
                    for (int i = 0; i < synonyms.length - 1; i++) {
                        if (synonyms[i] != null) {
                            return synonyms[i].equals(entry);
                        }
                    }
                }
                return false;
            }

            private boolean VerbPrepositionPairIsEmpty() {
                return prepositionTextView.getText().toString().matches("") ||
                        verbTextView.getText().toString().matches("");
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println(mUser.toString());
        mDatabaseHelper.saveGame(mUser);
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println(mUser.toString());
        mDatabaseHelper.saveGame(mUser);
    }
}
