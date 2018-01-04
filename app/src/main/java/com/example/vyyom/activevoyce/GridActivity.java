package com.example.vyyom.activevoyce;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.vyyom.activevoyce.database.activevoyce.DatabaseHelper;

public class GridActivity extends AppCompatActivity {

    private DatabaseHelper mDatabaseHelper;
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
                        Intent intent = new Intent(GridActivity.this, GameActivity.class);
                        intent.putExtra("User", mUser);
                        startActivityForResult(intent, 0);
                        return true;
                    case R.id.navigation_view:
                        Intent intent1 = new Intent(GridActivity.this, GridActivity.class);
                        intent1.putExtra("User", mUser);
                        startActivityForResult(intent1, 0);
                        return true;
                }
                return false;
            }
        });

        GridLayout gridLayout = findViewById(R.id.table_layout);
        gridLayout.setColumnCount(DatabaseHelper.TOTAL_PREPOSITIONS);
        gridLayout.setRowCount(DatabaseHelper.TOTAL_VERBS);
    }
}
