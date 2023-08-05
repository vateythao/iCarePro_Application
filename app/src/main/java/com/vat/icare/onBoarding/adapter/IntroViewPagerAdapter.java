package com.vat.icare.onBoarding.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.vat.icare.R;
import com.vat.icare.onBoarding.OnBoardingData;

import java.util.List;

/**
 * Created by Dell on 24,July,2023
 */
public class IntroViewPagerAdapter extends PagerAdapter {

    Context context;
    List<OnBoardingData> onBoardingData;

    public IntroViewPagerAdapter(Context context, List<OnBoardingData> onBoardingData) {
        this.context = context;
        this.onBoardingData = onBoardingData;

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_for_onboarding, null);


        ImageView image = view.findViewById(R.id.imageView);
        TextView textViewtitle = view.findViewById(R.id.textViewtitle);
        TextView description = view.findViewById(R.id.description);


        textViewtitle.setText(onBoardingData.get(position).getTitle());
        description.setText(onBoardingData.get(position).getDecs());
        image.setImageResource(onBoardingData.get(position).getImage());

        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return onBoardingData.size();
    }

    @Override
    public boolean isViewFromObject(View v, Object obj) {
        // TODO Auto-generated method stub
        return v == ((View) obj);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub
        ((ViewPager) container).removeView((View) object);
    }
}
