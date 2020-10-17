package com.example.technicaltask.Tools;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.example.technicaltask.Activity.ReviewActivity;
import com.example.technicaltask.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    private final String TAG = "CustomAdapter";
    private Context context;
    private LayoutInflater layoutInflater;

    private ArrayList<String> imgArr;
    private ArrayList<String> namesArr;
    private ArrayList<Float> priceArr;
    private ArrayList<Float> salePriceArr;
    private ArrayList<Integer> ratesArr;
    private ArrayList<Integer> votesArr;

    public CustomAdapter(Context context, ArrayList<String> namesArr, ArrayList<String> imgArr,
                         ArrayList<Float> priceArr, ArrayList<Float> salePriceArr,
                         ArrayList<Integer> ratesArr, ArrayList<Integer> votesArr) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.namesArr = namesArr;
        this.imgArr = imgArr;

        this.priceArr = priceArr;
        this.salePriceArr = salePriceArr;
        this.ratesArr = ratesArr;
        this.votesArr = votesArr;
    }

    @Override
    public int getCount() {
        return namesArr.size();
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

        if (i < namesArr.size())
            tvName.setText(namesArr.get(i));
        else
            tvName.setVisibility(View.INVISIBLE);

        if (i < imgArr.size() && !TextUtils.isEmpty(imgArr.get(i)))
            getDataFromFirebaseCloud(imgArr.get(i), imgMain);
        else
            imgMain.setVisibility(View.INVISIBLE);

        if (i < priceArr.size() && priceArr.get(i) > 0 && priceArr.get(i) != null)
            tvPrice.setText(String.format("$ %s", priceArr.get(i)));
        else
            tvPrice.setVisibility(View.INVISIBLE);

        if (i < salePriceArr.size() && salePriceArr.get(i) > 0 && salePriceArr.get(i) != null) {
            tvSalePrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            tvSalePrice.setText(String.format("$ %s", salePriceArr.get(i)));
        } else
            tvSalePrice.setVisibility(View.INVISIBLE);

        if (i < ratesArr.size() && i < votesArr.size()
                && ratesArr.get(i) > 0 && votesArr.get(i) > 0
                && ratesArr.get(i) != null && votesArr.get(i) != null) {
            if (ratesArr.get(i) > 5)
                ratesArr.add(i, 5);
            rating.setRating(ratesArr.get(i));
            tvCountVotes.setText(String.format("(%s)", votesArr.get(i)));
        } else {
            rating.setVisibility(View.GONE);
            tvCountVotes.setVisibility(View.GONE);
        }


        ConstraintLayout mainLinear = view.findViewById(R.id.mainLinear);
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

    private void getDataFromFirebaseCloud(String path, final ImageView imgMain) {
        final FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference gsReference = storage.getReferenceFromUrl(path);

        gsReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(imgMain);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e(TAG, "onFailure E: " + exception);
            }
        });

    }
}
