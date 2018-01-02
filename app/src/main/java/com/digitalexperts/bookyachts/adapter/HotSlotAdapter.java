package com.digitalexperts.bookyachts.adapter;


import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.digitalexperts.bookyachts.R;
import com.digitalexperts.bookyachts.customClasses.AppConstants;
import com.digitalexperts.bookyachts.models.HotSlotModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hp on 1/31/2017.
 */
public class HotSlotAdapter extends RecyclerView.Adapter<HotSlotAdapter.MyViewHolder> {
    Activity activity;




    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvHotSlot)
        TextView tvHotSlot;
        @BindView(R.id.tvDiscount)
        TextView tvDiscount;
        @BindView(R.id.tvTime)
        TextView tvTime;
        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);


        }
    }

    public HotSlotAdapter(Activity activity) {

        this.activity = activity;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_hotslot_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        HotSlotModel hotSlotModel = AppConstants.yachtsHotSlotsArrayList.get(position);

        holder.tvHotSlot.setText(hotSlotModel.getStart_date());
        holder.tvDiscount.setText(" Discount : "+hotSlotModel.getDiscount()+"%");
        int time = Integer.parseInt(hotSlotModel.getStart_time());
        time++;
        holder.tvTime.setText("Hot Slot Hour : "+hotSlotModel.getStart_time()+ ":00 to "+time+":00");

//
    }

    @Override
    public int getItemCount() {
        return AppConstants.yachtsHotSlotsArrayList.size();
    }

}