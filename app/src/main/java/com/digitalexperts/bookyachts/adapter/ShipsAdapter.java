package com.digitalexperts.bookyachts.adapter;


import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.digitalexperts.bookyachts.R;
import com.digitalexperts.bookyachts.activity.ShipDetailsActivity;
import com.digitalexperts.bookyachts.customClasses.AppConstants;
import com.digitalexperts.bookyachts.models.YachtsModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hp on 1/31/2017.
 */
public class ShipsAdapter extends RecyclerView.Adapter<ShipsAdapter.MyViewHolder> {
    public static ArrayList<YachtsModel> yachtsModelsArrayList = new ArrayList<>();
    public static int selectedPosition;
    Activity activity;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.progressBar)
        ProgressBar progressBar;
        @BindView(R.id.ivShipImage)
        ImageView ivShipImage;
        @BindView(R.id.tvYachtTitle)
        TextView tvYachtTitle;
        @BindView(R.id.tvGuestNumber)
        TextView tvGuestNumber;
        /*@BindView(R.id.tvTripDuration)
        TextView tvTripDuration;*/
        /*@BindView(R.id.tvCapacity)
        TextView tvCapacity;*/
        /*@BindView(R.id.tvSize)
        TextView tvSize;*/
        @BindView(R.id.tvPrice)
        TextView tvPrice;
        /*@BindView(R.id.tvHours)
        TextView tvHours;*/

        @BindView(R.id.tvViewMore)
        TextView tvViewMore;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }

    public ShipsAdapter(Activity activity, ArrayList<YachtsModel> yachtsModelsArrayList) {

        this.activity = activity;
        this.yachtsModelsArrayList = yachtsModelsArrayList;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_ships_item_two, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.progressBar.setVisibility(View.VISIBLE);

        YachtsModel yachtsModel = yachtsModelsArrayList.get(position);

        holder.tvYachtTitle.setText(yachtsModel.getTitle().substring(0, 2) + "ft");
//        holder.tvCapacity.setText("Capacity : " + yachtsModel.getMax_guest() + " Guests + " + yachtsModel.getCapacity() + " Captain");
        holder.tvGuestNumber.setText(yachtsModel.getMax_guest());
//        holder.tvPrice.setText(yachtsModel.getCurrency() + " " + yachtsModel.getPrice());
        holder.tvPrice.setText(yachtsModel.getPrice());

        Picasso.with(activity)
                .setIndicatorsEnabled(true);

        Picasso.with(activity)
                .load("https://bookyachts.ae/googleYacht/uploads/images/" + yachtsModel.getPicturePath())

                // .placeholder(R.drawable.loader)
                .into(holder.ivShipImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {

                    }
                });

//        holder.tvSize.setText("Size : " + yachtsModel.getSize());
//        holder.tvTripDuration.setText("Trip Duration : Minimum " + yachtsModel.getTripDuration() + " Hour");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedPosition = position;
                AppConstants.yachtsModel = yachtsModelsArrayList.get(position);
                Log.e("test", "yachtsModel " + yachtsModelsArrayList.get(position).getId());
                activity.startActivity(new Intent(activity, ShipDetailsActivity.class));
            }
        });
        holder.tvViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedPosition = position;
                AppConstants.yachtsModel = yachtsModelsArrayList.get(position);
                activity.startActivity(new Intent(activity, ShipDetailsActivity.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return yachtsModelsArrayList.size();
    }

}