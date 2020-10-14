package com.example.technicaltask.Tools;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import com.example.technicaltask.R;

import java.util.List;

public class HelpWizardSwipeAdapter extends PagerAdapter {

    private List<HelpWizardFragmentPage> fragmentPage;
    private Context context;

    public HelpWizardSwipeAdapter(List<HelpWizardFragmentPage> fragmentPage, Context context) {
        this.fragmentPage = fragmentPage;
        this.context = context;
    }

    @Override
    public int getCount() {
        return fragmentPage.size();
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view.equals(o);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.help_wizard_fragment, container, false);

        ImageView imageView;
        TextView tvDesc, tvTitle;

        imageView = view.findViewById(R.id.imageView);
        tvTitle = view.findViewById(R.id.tvTitle);
        tvDesc = view.findViewById(R.id.tvDesc);


        imageView.setImageResource(fragmentPage.get(position).getImage());
        tvTitle.setText(fragmentPage.get(position).getTitle());
        tvDesc.setText(fragmentPage.get(position).getDesc());

        if (TextUtils.equals(tvTitle.getText(), view.getResources().getText(R.string.sign_in))) {
            tvTitle.setTextColor(view.getResources().getColor(R.color.colorGreen));
            tvTitle.setEnabled(true);
        } else {
            tvTitle.setTextColor(view.getResources().getColor(R.color.colorDarkGrey));
            tvTitle.setEnabled(false);
        }
        container.addView(view, 0);
        return view;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
