package com.example.technicaltask.Tools;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.technicaltask.Activity.ReviewActivity;
import com.example.technicaltask.R;

import java.io.ByteArrayOutputStream;

public class CustomAdapter extends BaseAdapter {

    private final String TAG = "CustomAdapter";
    private Context context;
    private LayoutInflater layoutInflater;
    private String[] namesArr;
    private int[] imagesArr;
    private float[] priceArr;
    private float[] salePriceArr;
    private int[] ratesArr;
    private int[] votesArr;

    public CustomAdapter(Context context, String[] namesArr, int[] imagesArr,
                         float[] priceArr, float[] salePriceArr, int[] ratesArr, int[] votesArr) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.namesArr = namesArr;
        this.imagesArr = imagesArr;

        this.priceArr = priceArr;
        this.salePriceArr = salePriceArr;
        this.ratesArr = ratesArr;
        this.votesArr = votesArr;
    }

    @Override
    public int getCount() {
        return namesArr.length;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = layoutInflater.inflate(R.layout.table_layout, viewGroup, false);
        }

        final TextView tvName = view.findViewById(R.id.tvName);
        final ImageView imgMain = view.findViewById(R.id.imgMain);

        final TextView tvPrice = view.findViewById(R.id.tvPrice);
        final TextView tvSalePrice = view.findViewById(R.id.tvSalePrice);

        final RatingBar rating = view.findViewById(R.id.rating);
        final TextView tvCountVotes = view.findViewById(R.id.tvCountVotes);

        if (i < namesArr.length)
            tvName.setText(namesArr[i]);
        else
            tvName.setVisibility(View.INVISIBLE);

        if (i < imagesArr.length && imagesArr[i] > 0)
            imgMain.setImageDrawable(context.getResources().getDrawable(imagesArr[i]));
        else
            imgMain.setVisibility(View.INVISIBLE);

        if (i < priceArr.length && priceArr[i] > 0)
            tvPrice.setText(String.valueOf(priceArr[i]));
        else
            tvPrice.setVisibility(View.INVISIBLE);

        if (i < salePriceArr.length && salePriceArr[i] > 0) {
            tvSalePrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            tvSalePrice.setText(String.valueOf(salePriceArr[i]));
        } else
            tvSalePrice.setVisibility(View.INVISIBLE);

        if (i < ratesArr.length && i < votesArr.length && ratesArr[i] > 0 && votesArr[i] > 0) {
            if (ratesArr[i] > 5)
                ratesArr[i] = 5;
            rating.setRating(ratesArr[i]);
            tvCountVotes.setText(String.format("(%s)", votesArr[i]));
        } else {
            rating.setVisibility(View.INVISIBLE);
            tvCountVotes.setVisibility(View.INVISIBLE);
        }


        LinearLayout mainLinear = view.findViewById(R.id.mainLinear);
        mainLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ReviewActivity.class);
                byte[] byteArray = getImageBytes(imgMain);
                String name = tvName.getText().toString();
                String price = tvPrice.getText().toString();
                String salePrice = tvSalePrice.getText().toString();
                String votes = tvCountVotes.getText().toString();
                float rate = rating.getRating();

                intent.putExtra(Constants.INTENT_MAIN_IMAGE, byteArray);
                intent.putExtra(Constants.INTENT_NAME, name);
                intent.putExtra(Constants.INTENT_PRICE, price);
                intent.putExtra(Constants.INTENT_SALE_PRICE, salePrice);
                intent.putExtra(Constants.INTENT_RATE, rate);
                intent.putExtra(Constants.INTENT_VOTES, votes);

                context.startActivity(intent);
            }
        });

        return view;
    }

    private byte[] getImageBytes(ImageView imgMain) {
        Bitmap bitmap = ((BitmapDrawable) imgMain.getDrawable()).getBitmap();
        Bitmap converetdImage = getResizedBitmap(bitmap, 500);

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        converetdImage.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
        return byteStream.toByteArray();
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
}
