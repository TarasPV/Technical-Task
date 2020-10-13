package com.example.technicaltask.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.technicaltask.R;
import com.example.technicaltask.Tools.Constants;
import com.example.technicaltask.Tools.Utils;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        init();
    }

    private void init() {
        boolean isFirstOpen = Utils.getPrefsBoolByKey(this, Constants.PREFS_IS_FIRST_OPEN, true);

        Intent intent;
        if (false) {
            intent = new Intent(this, MainActivity.class);
        } else {
            intent = new Intent(this, WizardActivity.class);
        }
        startActivity(intent);
        finish();
    }
}