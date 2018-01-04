package com.example.vyyom.activevoyce;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class HelpActivity extends AppCompatActivity {

    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        //noinspection ConstantConditions
        mUser = (User) getIntent().getExtras().get("User");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("User", mUser);
        startActivityForResult(intent, 0);
        finish();
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
