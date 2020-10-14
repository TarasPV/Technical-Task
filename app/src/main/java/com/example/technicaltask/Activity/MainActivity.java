package com.example.technicaltask.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Utils.putPrefsByKey(this, Constants.PREFS_IS_FIRST_OPEN, false);

        int BOOKSHELF_ROWS = 5;


//        for (Nfce_Product nfceProduct : nfceProducts) {
        for (int i = 0; i < BOOKSHELF_ROWS; i++) {
            final TableLayout detailsTable = (TableLayout) findViewById(R.id.tableLayout);
            final TableRow tableRow = (TableRow) getLayoutInflater().inflate(R.layout.tablerow, null);
            TextView tvName, tvPrice, tvSalePrice;
            ImageView img;
            //Filling in cells
            tvName = (TextView) tableRow.findViewById(R.id.tvLeft);
            tvPrice = (TextView) tableRow.findViewById(R.id.tvPriceLeft);
            tvSalePrice = (TextView) tableRow.findViewById(R.id.tvSalePriceLeft);
            img = (ImageView) tableRow.findViewById(R.id.imgLeft);
            img.setTag(String.valueOf(i));
            img.setImageDrawable(getResources().getDrawable(R.drawable.ex1));
            tvName.setText("Cashmere Textured Dress");
            tvSalePrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);

            tvName = (TextView) tableRow.findViewById(R.id.tvRight);
            tvPrice = (TextView) tableRow.findViewById(R.id.tvPriceRight);
            tvSalePrice = (TextView) tableRow.findViewById(R.id.tvSalePriceRight);
            img = (ImageView) tableRow.findViewById(R.id.imgRight);
            img.setTag(String.valueOf(i));
            img.setImageDrawable(getResources().getDrawable(R.drawable.ex2));
            tvName.setText("Wool dress");
            tvSalePrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);


            //Add row to the table
            detailsTable.addView(tableRow);
        } //End for

    }

    public void onLinearClick(View view) {
        try {
            Drawable imgDrawable = null;
            Bitmap imgBitmap = null;
            String mainText = null, price = null;
            LinearLayout ll = (LinearLayout) view;
            for (int i = 0; i < ll.getChildCount(); i++) {
                if (ll.getChildAt(i) instanceof ImageView) {
                    ImageView img = (ImageView) ll.getChildAt(i);
                    imgDrawable = img.getDrawable();
                    imgBitmap = img.getDrawingCache();
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
    /*public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }*/

    class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
        Bitmap bitmap;

        private BitmapWorkerTask(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        @Override
        protected Bitmap doInBackground(Integer... params) {
            addBitmapToMemoryCache("testKey", bitmap);
            bitmap = getBitmapFromMemCache("testKey");

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            Log.d(TAG, "res : " + bitmap);
        }
    }

    private LruCache<String, Bitmap> memoryCache;

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            memoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return memoryCache.get(key);
    }
}