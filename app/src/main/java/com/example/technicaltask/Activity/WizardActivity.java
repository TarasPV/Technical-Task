package com.example.technicaltask.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.technicaltask.R;
import com.example.technicaltask.Tools.HelpWizardFragmentPage;
import com.example.technicaltask.Tools.HelpWizardSwipeAdapter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.ArrayList;
import java.util.List;

public class WizardActivity extends AppCompatActivity {
    private static final String TAG = "WizardActivity";

    private static final int RC_SIGN_IN = 9001;

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard);

        init();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
                updateUI(null);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
//        showProgressBar();
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(WizardActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
//                        hideProgressBar();
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        mAuth.signOut();
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
//        hideProgressBar();
        if (user != null) {
            Toast.makeText(this, user.getEmail(), Toast.LENGTH_SHORT).show();
        }
    }

    private void init() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

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
                        startMainActivity();

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
        finish();
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onSkipClick(View view) {
        startMainActivity();
    }

    public void onSignInClick(View view) {
        signOut();
        signIn();
    }
}