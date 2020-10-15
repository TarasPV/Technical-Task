package com.example.technicaltask.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.technicaltask.R;
import com.example.technicaltask.Tools.Constants;
import com.example.technicaltask.Tools.Utils;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private int BOOKSHELF_ROWS = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Utils.putPrefsByKey(this, Constants.PREFS_IS_FIRST_OPEN, false);

        init();
    }

    private void init() {
        for (int i = 0; i < BOOKSHELF_ROWS; i++) {
            final TableLayout detailsTable = (TableLayout) findViewById(R.id.tableLayout);
            final TableRow tableRow = (TableRow) getLayoutInflater().inflate(R.layout.tablerow, null);

            // The data obtained for left
            Drawable image = getResources().getDrawable(R.drawable.ex1);
            String name = "Cashmere Textured Dress";
            float price = 50.0f;
            float salePrice = 100.0f;
            int rate = 5;
            int vote = 8;

            // Filling in left cells
            setLeftColumn(tableRow, image, name, price, salePrice, rate, vote);


            // The data obtained for right
            image = getResources().getDrawable(R.drawable.ex2);
            name = "Wool dress";
            rate = -1;
            vote = 2;

            // Filling in right cells
            setRightColumn(tableRow, image, name, price, salePrice, rate, vote);


            // Add row to the table
            detailsTable.addView(tableRow);
        }
    }

    private void setLeftColumn(TableRow tableRow, Drawable image, String name,
                               float price, float salePrice, int rate, int vote) {
        if (image == null || TextUtils.isEmpty(name)) {
            LinearLayout mainLinear = (LinearLayout) tableRow.findViewById(R.id.llLeft);
            mainLinear.setVisibility(View.GONE);
            return;
        }
        TextView tvName = (TextView) tableRow.findViewById(R.id.tvLeft);
        TextView tvPrice = (TextView) tableRow.findViewById(R.id.tvPriceLeft);
        if (salePrice != 0.0f) {
            TextView tvSalePrice = (TextView) tableRow.findViewById(R.id.tvSalePriceLeft);
            tvSalePrice.setVisibility(View.VISIBLE);
            tvSalePrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            tvSalePrice.setText(String.valueOf(salePrice));
        }
        ImageView img = (ImageView) tableRow.findViewById(R.id.imgLeft);
        if (rate >= 0) {
            RatingBar rating = (RatingBar) tableRow.findViewById(R.id.ratingLeft);
            rating.setVisibility(View.VISIBLE);
            rating.setRating(rate);
            if (vote > 0) {
                TextView tvVotes = (TextView) tableRow.findViewById(R.id.tvCountVotesLeft);
                tvVotes.setVisibility(View.VISIBLE);
                tvVotes.setText(String.format("(%s)", vote));
            }
        }

        tvPrice.setText(String.valueOf(price));
        img.setImageDrawable(image);
        tvName.setText(name);
    }

    private void setRightColumn(TableRow tableRow, Drawable image, String name,
                                float price, float salePrice, int rate, int vote) {

        if (image == null || TextUtils.isEmpty(name)) {
            LinearLayout mainLinear = (LinearLayout) tableRow.findViewById(R.id.llRight);
            mainLinear.setVisibility(View.GONE);
            return;
        }

        TextView tvName = (TextView) tableRow.findViewById(R.id.tvRight);
        TextView tvPrice = (TextView) tableRow.findViewById(R.id.tvPriceRight);
        if (salePrice != 0.0f) {
            TextView tvSalePrice = (TextView) tableRow.findViewById(R.id.tvSalePriceRight);
            tvSalePrice.setVisibility(View.VISIBLE);
            tvSalePrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            tvSalePrice.setText(String.valueOf(salePrice));
        }
        ImageView img = (ImageView) tableRow.findViewById(R.id.imgRight);
        if (rate >= 0) {
            RatingBar rating = (RatingBar) tableRow.findViewById(R.id.ratingRight);
            rating.setVisibility(View.VISIBLE);
            rating.setRating(rate);
            if (vote > 0) {
                TextView tvVotes = (TextView) tableRow.findViewById(R.id.tvCountVotesRight);
                tvVotes.setVisibility(View.VISIBLE);
                tvVotes.setText(String.format("(%s)", vote));
            }
        }

        tvPrice.setText(String.valueOf(price));
        img.setImageDrawable(image);
        tvName.setText(name);
    }

    public void onLinearClick(View view) {
        try {
            Drawable imgDrawable = null;
            String mainText = null, price = null;
            LinearLayout ll = (LinearLayout) view;
            for (int i = 0; i < ll.getChildCount(); i++) {
                if (ll.getChildAt(i) instanceof ImageView) {
                    ImageView img = (ImageView) ll.getChildAt(i);
                    imgDrawable = img.getDrawable();
                    Log.d(TAG, "onImgClick its image: " + img.getDrawable());
                } else if (ll.getChildAt(i) instanceof TextView) {
                    TextView tv = (TextView) ll.getChildAt(i);
                    mainText = tv.getText().toString();
                    Log.d(TAG, "onImgClick its tv: " + tv.getText());
                } else if (ll.getChildAt(i) instanceof LinearLayout) {
                    LinearLayout childLinear = (LinearLayout) ll.getChildAt(i);
                    for (int j = 0; j < childLinear.getChildCount(); j++) {
                        if (childLinear.getChildAt(j) instanceof TextView) {
                            TextView tv = (TextView) childLinear.getChildAt(j);
                            price = tv.getText().toString();
                            Log.d(TAG, "onImgClick its child tv : " + tv.getText());
                        }
                    }
                }
            }

            Intent intent = new Intent(MainActivity.this, ReviewActivity.class);
            Bitmap bitmap = ((BitmapDrawable) imgDrawable).getBitmap();
            Bitmap converetdImage = getResizedBitmap(bitmap, 500);

            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            converetdImage.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
            byte[] byteArray = byteStream.toByteArray();

            intent.putExtra(Constants.BUNDLE_MAIN_IMAGE, byteArray);
            intent.putExtra(Constants.BUNDLE_NAME, mainText);
            intent.putExtra(Constants.BUNDLE_PRICE, price);
            startActivity(intent);

        } catch (Exception ex) {
            Log.e(TAG, "onLinearClick E: " + ex.getMessage());
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public void onBackClick(View view) {
        startActivity(new Intent(this, WizardActivity.class));
        finish();
    }

 /*   public void onFavoriteClick(View view) {
    }*/
}