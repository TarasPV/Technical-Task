package com.example.technicaltask.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.technicaltask.R;
import com.example.technicaltask.Tools.Constants;
import com.example.technicaltask.Tools.HelpWizardFragmentPage;
import com.example.technicaltask.Tools.HelpWizardSwipeAdapter;
import com.example.technicaltask.Tools.Utils;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class WizardActivity extends AppCompatActivity {
    private static final String TAG = "WizardActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard);

        init();
    }

    private void init() {
        this.getSupportActionBar().hide();

        setupViewPager();
    }

    public void setupViewPager() {
        try {
            final List<HelpWizardFragmentPage> fragmentPages = new ArrayList<>();
            fragmentPages.add(new HelpWizardFragmentPage(R.drawable.ico_wizard_1,
                    getString(R.string.wizard_title_1),
                    getString(R.string.wizard_desc)));
            fragmentPages.add(new HelpWizardFragmentPage(R.drawable.ico_wizard_2,
                    getString(R.string.wizard_title_2),
                    getString(R.string.wizard_desc)));
            fragmentPages.add(new HelpWizardFragmentPage(R.drawable.ico_wizard_3,
                    getString(R.string.sign_in),
                    getString(R.string.wizard_desc)));


            HelpWizardSwipeAdapter swipeAdapter = new HelpWizardSwipeAdapter(fragmentPages, this);

            final ViewPager viewPager = findViewById(R.id.viewPager);
            viewPager.setAdapter(swipeAdapter);
            viewPager.setPadding(0, 0, 0, 0);
            swipeAdapter = new HelpWizardSwipeAdapter(fragmentPages, this);
            viewPager.setAdapter(swipeAdapter);
            viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                boolean swipeClose = false;

                @Override
                public void onPageScrolled(int i, float v, int i1) {
                    Log.d(TAG, "fragmentPages.size(): " + fragmentPages.size());
                    if (i != fragmentPages.size() - 1)
                        swipeClose = false;

                    if (swipeClose)
                        onBackPressed();

                    if (i == fragmentPages.size() - 1) {
                        swipeClose = true;
                        ((Button) findViewById(R.id.btnSkip)).setVisibility(View.VISIBLE);
                    } else {
                        ((Button) findViewById(R.id.btnSkip)).setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onPageSelected(int i) {

                }

                @Override
                public void onPageScrollStateChanged(int i) {

                }
            });
            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabDots);
            tabLayout.setupWithViewPager(viewPager, true);
        } catch (Exception ex) {
            Log.e(TAG, "onCreate: " + ex.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        Utils.putPrefsByKey(this, Constants.PREFS_IS_FIRST_OPEN, false);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onSkipClick(View view) {
        onBackPressed();
    }
}