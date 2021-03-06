package com.example.technicaltask.Activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.technicaltask.R;
import com.example.technicaltask.Tools.Constants;

public class ReviewActivity extends AppCompatActivity {

    private static final String TAG = "ReviewActivity";
    private String[] sizeArr = new String[]{"Small", "Medium", "Large"};
    private String[] colorArr = new String[]{"Light grey", "Black", "White"};
    private String[] countGoodsArr = new String[]{"1", "2", "3", "4", "5", "6", "7", "8"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        init();
    }

    private void init() {
        RatingBar rating = findViewById(R.id.rating);
        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                // TODO:: save rating data
                Log.d(TAG, "onRatingChanged v: " + v + " | b: " + v);
            }
        });

        Spinner spinnerSize = findViewById(R.id.spinnerSize);
        Spinner spinnerColor = findViewById(R.id.spinnerColor);
        Spinner spinnerCountGoods = findViewById(R.id.spinnerCountGoods);

        spinnerSize.setAdapter(getAdapter(sizeArr));
        spinnerColor.setAdapter(getAdapter(colorArr));
        spinnerCountGoods.setAdapter(getAdapter(countGoodsArr));

        setIntentData();

        // Some data
        String productCode = "578902-00";
        String category = "Sweaters";
        String material = "Cotton";
        String country = "Germany";
        // ---

        TextView tvProductCode = findViewById(R.id.tvProductCodeInfo);
        TextView tvCategory = findViewById(R.id.tvCategoryInfo);
        TextView tvMaterial = findViewById(R.id.tvMaterialInfo);
        TextView tvCountry = findViewById(R.id.tvCountryInfo);

        tvProductCode.setText(productCode);
        tvCategory.setText(category);
        tvMaterial.setText(material);
        tvCountry.setText(country);
    }

    private ArrayAdapter<String> getAdapter(String[] arr) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, R.layout.fill_spinner_style, arr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private void setIntentData() {
        Intent intent = getIntent();
        byte[] byteArray = intent.getByteArrayExtra(Constants.INTENT_MAIN_IMAGE);
        String name = intent.getStringExtra(Constants.INTENT_NAME);
        String price = intent.getStringExtra(Constants.INTENT_PRICE);

        String salePrice = intent.getStringExtra(Constants.INTENT_SALE_PRICE);
        String votes = intent.getStringExtra(Constants.INTENT_VOTES);
        float rate = intent.getFloatExtra(Constants.INTENT_RATE, 0f);

        ImageView imgMain = findViewById(R.id.imgMain);
        TextView tvName = findViewById(R.id.tvName);
        TextView tvPrice = findViewById(R.id.tvPrice);
        TextView tvSalePrice = findViewById(R.id.tvSalePrice);

        TextView tvCountVotes = findViewById(R.id.tvCountVotes);
        RatingBar rating = findViewById(R.id.rating);

        if (byteArray != null) {
            Drawable imgDrawable = new BitmapDrawable(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
            imgMain.setImageDrawable(imgDrawable);
            imgMain.setAdjustViewBounds(true);
        }
        tvName.setText(name);
        tvPrice.setText(price);

        if (!TextUtils.isEmpty(salePrice)) {
            tvSalePrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            tvSalePrice.setText(salePrice);
        } else {
            tvSalePrice.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(votes) && rate > 0) {
            tvCountVotes.setText(votes);
            rating.setRating(rate);
        } else {
            tvCountVotes.setVisibility(View.INVISIBLE);
            rating.setVisibility(View.INVISIBLE);
        }
    }

    public void onBackClick(View view) {
        onBackPressed();
    }

    public void onAddCardClick(View view) {
        Toast.makeText(ReviewActivity.this, "Soon to be", Toast.LENGTH_SHORT).show();
    }
}