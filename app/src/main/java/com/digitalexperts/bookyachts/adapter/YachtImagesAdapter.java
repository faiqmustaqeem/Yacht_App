package com.digitalexperts.bookyachts.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.digitalexperts.bookyachts.R;

import java.util.ArrayList;

import static com.digitalexperts.bookyachts.customClasses.AppConstants.yachtsModel;

/**
 * Created by Codiansoft on 10/18/2017.
 */

public class YachtImagesAdapter extends RecyclerView.Adapter<YachtImagesAdapter.MyViewHolder> {

    private final ArrayList<String> images;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivImage;

        public MyViewHolder(View view) {
            super(view);

            ivImage = (ImageView) view.findViewById(R.id.ivYachtImage);
        }
    }

    public YachtImagesAdapter(ArrayList<String> images, Context context) {
        this.images = images;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.yacht_images_rv_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        String imagePath = images.get(position);

/*        Picasso.with(context)
                .load("https://googleyachts.com/googleYacht/uploads/images/" + imagePath)
                .placeholder(R.drawable.demo_one)
                .resize(holder.ivImage.getMeasuredWidth(), holder.ivImage.getMeasuredHeight())
                .centerCrop()
                .into(holder.ivImage);*/

        Glide.with(context).load("https://bookyachts.ae/googleYacht/uploads/images/" + imagePath).centerCrop().into(holder.ivImage);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }
}