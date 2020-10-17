package com.example.technicaltask.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter;
import com.example.technicaltask.R;
import com.example.technicaltask.Tools.Constants;
import com.example.technicaltask.Tools.CustomAdapter;
import com.example.technicaltask.Tools.Utils;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

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
        initNavigation();
        init();
    }

    private void initNavigation() {
        try {
            final AHBottomNavigation bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottomNav);
            AHBottomNavigationAdapter navigationAdapter = new AHBottomNavigationAdapter(this, R.menu.my_navigation_items);
            navigationAdapter.setupWithBottomNavigation(bottomNavigation);

            bottomNavigation.setCurrentItem(0);
            bottomNavigation.setAccentColor(getResources().getColor(R.color.colorDarkGreen));
            bottomNavigation.setNotification("1", 2);

            bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
                @Override
                public boolean onTabSelected(int position, boolean wasSelected) {
                    switch (position) {
                        case 1:
                        case 2:
                        case 3:
                            Toast.makeText(MainActivity.this, "Soon to be", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    return true;
                }
            });

        } catch (Exception ex) {
            Log.e(TAG, "MainActivity - initNavigation: " + ex.getMessage());
        }
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