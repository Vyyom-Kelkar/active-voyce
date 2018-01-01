package com.example.vyyom.activevoyce;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putStringArrayListExtra("User",
                getIntent().getStringArrayListExtra("User"));
        Log.d("TESTING", intent.getStringArrayListExtra("User").get(0));
        startActivity(intent);
        finish();
    }
}
