package com.example.technicaltask.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter;
import com.example.technicaltask.R;
import com.example.technicaltask.Tools.Constants;
import com.example.technicaltask.Tools.CustomAdapter;
import com.example.technicaltask.Tools.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ArrayList<String> imgArr = new ArrayList<>();
    private ArrayList<String> namesArr = new ArrayList<>();
    private ArrayList<Float> priceArr = new ArrayList<>();
    private ArrayList<Float> salePriceArr = new ArrayList<>();
    private ArrayList<Integer> ratesArr = new ArrayList<>();
    private ArrayList<Integer> votesArr = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Utils.putPrefsByKey(this, Constants.PREFS_IS_FIRST_OPEN, false);
        initNavigation();

        getDataFromFirebase();
    }

    private void getDataFromFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("dresses")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                setDataFromMap(document.getData());
                            }
                        } else {
                            Log.e(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void setDataFromMap(Map<String, Object> data) {
        try {
            findViewById(R.id.pbMain).setVisibility(View.VISIBLE);

            String image = String.valueOf(data.get("image"));
            if (TextUtils.isEmpty(image))
                imgArr.add(null);
            else
                imgArr.add(image);

            String name = String.valueOf(data.get("name"));
            if (TextUtils.isEmpty(name))
                namesArr.add(null);
            else
                namesArr.add(name);

            String price = String.valueOf(data.get("price"));
            if (TextUtils.isEmpty(price))
                priceArr.add(null);
            else
                priceArr.add(Float.valueOf(price));

            String salePrice = String.valueOf(data.get("sale_price"));
            if (TextUtils.isEmpty(salePrice))
                salePriceArr.add(null);
            else
                salePriceArr.add(Float.valueOf(salePrice));

            String rate = String.valueOf(data.get("rate"));
            if (TextUtils.isEmpty(rate))
                ratesArr.add(null);
            else
                ratesArr.add(Integer.valueOf(rate));

            String vote = String.valueOf(data.get("vote"));
            if (TextUtils.isEmpty(vote))
                votesArr.add(null);
            else
                votesArr.add(Integer.valueOf(vote));

            init();
        } catch (Exception ex) {
            Log.e(TAG, "setDataFromMap E: " + ex.getMessage());
            findViewById(R.id.pbMain).setVisibility(View.GONE);
        }
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
        CustomAdapter customAdapter = new CustomAdapter(this, namesArr, imgArr,
                priceArr, salePriceArr, ratesArr, votesArr);
        GridView gridFriends = findViewById(R.id.gridFriends);
        gridFriends.setAdapter(customAdapter);
        findViewById(R.id.pbMain).setVisibility(View.GONE);
    }

    public void onBackClick(View view) {
        startActivity(new Intent(this, WizardActivity.class));
        finish();
    }
}