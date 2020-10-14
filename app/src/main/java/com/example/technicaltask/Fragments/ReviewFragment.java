package com.example.technicaltask.Fragments;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.technicaltask.R;
import com.example.technicaltask.Tools.Constants;

public class ReviewFragment extends Fragment {


    private static final String TAG = "ReviewFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review, container, false);

        byte[] byteArray = getArguments().getByteArray(Constants.BUNDLE_MAIN_IMAGE);
        String name = getArguments().getString(Constants.BUNDLE_NAME);
        String price = getArguments().getString(Constants.BUNDLE_PRICE);
        Drawable imgDrawable = new BitmapDrawable(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));

        Button btnClose = view.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(onClickCloseLisetener);
        ImageView imgMain = view.findViewById(R.id.imgMain);
        imgMain.setImageDrawable(imgDrawable);
        TextView tvName = view.findViewById(R.id.tvName);
        tvName.setText(name);
        TextView tvPrice = view.findViewById(R.id.tvPrice);
        tvPrice.setText(price);

        return view;
    }

    private View.OnClickListener onClickCloseLisetener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.addToBackStack(null);
            transaction.commit();
            getFragmentManager().beginTransaction().remove(getTargetFragment()).commit();

          /*  FragmentManager manager = getActivity().getFragmentManager();
            FragmentTransaction trans = manager.beginTransaction();
            trans.remove((Fragment) object);
            trans.commit();*/
//            getActivity().getSupportFragmentManager().beginTransaction().remove(getParentFragment());
//            getActivity().onBackPressed();
//            getActivity().getSupportFragmentManager().popBackStackImmediate();
//            getActivity().getFragmentManager().popBackStack();
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView");
    }
}