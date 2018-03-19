package com.digitalexperts.bookyachts.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.digitalexperts.bookyachts.R;

import java.util.ArrayList;

/**
 * Created by hp on 9/13/2017.
 */

public class CustomPagerAdapter extends PagerAdapter {

    Context mContext;
    ArrayList<String> yachtSliderArrayList;
    LayoutInflater mLayoutInflater;


    public CustomPagerAdapter(Context context, ArrayList<String> yachtSliderArrayList) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.yachtSliderArrayList = yachtSliderArrayList;

    }

    @Override
    public int getCount() {
        return yachtSliderArrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.layout_ship_slider, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.ivShipImage);
       final ProgressBar progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
/*
        Picasso.with(mContext)
                .setIndicatorsEnabled(true);
        Picasso.with(mContext)
                .load("https://bookyachts.com/googleYacht/uploads/images/" + yachtSliderArrayList.get(position).toString())
                //.placeholder(R.drawable.loader)
                .into(imageView, new Callback()
                {
                    @Override
                    public void onSuccess()
                    {
                       progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError()
                    {

                    }
                });*/

        Glide.with(mContext).load("https://bookyachts.ae/googleYacht/uploads/images/" + yachtSliderArrayList.get(position).toString())
                .placeholder(R.drawable.loading)
                .centerCrop()
                .into(imageView);


        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}