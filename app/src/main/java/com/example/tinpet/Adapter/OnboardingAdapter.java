package com.example.tinpet.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.example.tinpet.Anonymus.OnboardingActivity;
import com.example.tinpet.R;

public class OnboardingAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;
    ConstraintLayout constraintlayout;

    int images[] = {
            R.drawable.onboarding_img1,
            R.drawable.onboarding_img2,
            R.drawable.onboarding_img3,
    };


    int tittles[] = {
            R.string.onboarding_welcome_tittle,
            R.string.onboarding_share_tittle,
            R.string.onboarding_connect_tittle,

    };

    int heading[] = {
            R.string.onboarding_welcome,
            R.string.onboarding_share,
            R.string.onboarding_connect,
    };



    public OnboardingAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return heading.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout) object;
    }

    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_onboarding, container,false);

        TextView textViewTitle1 = view.findViewById(R.id.tvOnboardingSlideTitle1);
        TextView textViewText1 = view.findViewById(R.id.tvOnboardingSlideText1);
        TextView textViewTitle2 = view.findViewById(R.id.tvOnboardingSlideTitle2);
        TextView textViewText2 = view.findViewById(R.id.tvOnboardingSlideText2);
        ImageView imageView = view.findViewById(R.id.ivOnboardingSlideImage);

        if (position == 0 || position == 2){
            textViewTitle1.setVisibility(View.VISIBLE);
            textViewTitle1.setText(tittles[position]);
            textViewText1.setTextSize(20);
            textViewText1.setVisibility(View.VISIBLE);
            textViewText1.setText(heading[position]);
            textViewTitle2.setVisibility(View.INVISIBLE);
            textViewText2.setVisibility(View.INVISIBLE);

        }
        if (position == 1){
            textViewTitle2.setVisibility(View.VISIBLE);
            textViewTitle2.setText(tittles[position]);
            textViewText2.setTextSize(20);
            textViewText2.setVisibility(View.VISIBLE);
            textViewText2.setText(heading[position]);
            textViewTitle1.setVisibility(View.INVISIBLE);
            textViewText1.setVisibility(View.INVISIBLE);
        }

        imageView.setImageResource(images[position]);
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout) object);
    }


}
