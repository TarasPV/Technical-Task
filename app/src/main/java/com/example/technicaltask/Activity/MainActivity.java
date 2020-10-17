package com.example.technicaltask.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.technicaltask.R;
import com.example.technicaltask.Tools.Constants;
import com.example.technicaltask.Tools.CustomAdapter;
import com.example.technicaltask.Tools.Utils;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private int BOOKSHELF_ROWS = 3;

    // Data for cells
    private int[] imagesArr = new int[]{R.drawable.ex1, R.drawable.ex2, R.drawable.ex3, R.drawable.ex4, R.drawable.ex1, R.drawable.ex2};
    private String[] namesArr = {"Scaridian dress", "Wool dress", "Cream cotton dress", "Black dress", "Scaridian dress", "Wool dress"};
    private float[] priceArr = {50.0f, 100.0f, 50.0f, 50.0f, 50.0f, 100.0f};
    private float[] salePriceArr = {100.0f};
    private int[] ratesArr = {5, 4};
    private int[] votesArr = {8, 2};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Utils.putPrefsByKey(this, Constants.PREFS_IS_FIRST_OPEN, false);

        init();
    }

    private void init() {
        CustomAdapter customAdapter = new CustomAdapter(this, namesArr, imagesArr,
                priceArr, salePriceArr, ratesArr, votesArr);
        GridView gridFriends = findViewById(R.id.gridFriends);
        gridFriends.setAdapter(customAdapter);
    }

    public void onBackClick(View view) {
        startActivity(new Intent(this, WizardActivity.class));
        finish();
    }

}