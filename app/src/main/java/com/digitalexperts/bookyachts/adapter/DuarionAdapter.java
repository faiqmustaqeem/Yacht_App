package com.digitalexperts.bookyachts.adapter;


import android.app.Activity;
import android.app.Dialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.digitalexperts.bookyachts.R;
import com.digitalexperts.bookyachts.customClasses.AppConstants;
import com.digitalexperts.bookyachts.interfaces.ShowDataInterface;
import com.digitalexperts.bookyachts.models.HotSlotModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hp on 1/31/2017.
 */
public class DuarionAdapter extends RecyclerView.Adapter<DuarionAdapter.MyViewHolder>
{
    ArrayList<String> stringArrayList = new ArrayList<>();
    Activity activity;
    HotSlotModel hotSlotModel;
    ShowDataInterface showDataInterface;
    Dialog dialog;


    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tvItem)
        TextView tvItem;
        @BindView(R.id.card)
        CardView card;
        @BindView(R.id.tvDiscount)
        TextView tvDiscount;
        @BindView(R.id.liMain)
        LinearLayout liMain;


        public MyViewHolder(View view)
        {
            super(view);
            ButterKnife.bind(this, view);

        }
    }

    public DuarionAdapter(Activity activity,Dialog dialog)
    {

        this.activity = activity;
        this.dialog = dialog;
        showDataInterface = (ShowDataInterface) activity;
        setDurationSlots();
    }

    private void setDurationSlots()
    {
        int start = 1;
        int end = 2;
        //String  startAmOrPm="AM";
       // String endAmOrPm ="AM";

        for (int i = 0; i <= 22; i++)
        {
            stringArrayList.add(start +":00"+" to " + end+":00");
            start++;
            end++;

//            if(start>11)
//            {
//                startAmOrPm="PM";
//            }
//            if(end > 11)
//            {
//                endAmOrPm="PM";
//            }
        }

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_duration_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {

        holder.tvItem.setText(stringArrayList.get(position).toString());

        if(AppConstants.indexOfHotSlot > -1)
        {
            hotSlotModel = AppConstants.yachtsHotSlotsArrayList.get(AppConstants.indexOfHotSlot);

            if(stringArrayList.get(position).toString().startsWith(hotSlotModel.getStart_time()))
            {
                holder.liMain.setVisibility(View.VISIBLE);
                holder.tvDiscount.setText(" Discount "+hotSlotModel.getDiscount()+"%");
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                showDataInterface.showData(stringArrayList.get(position).toString());

                if(hotSlotModel != null)
                {
                    AppConstants.discountPercentage = Integer.parseInt(hotSlotModel.getDiscount());
                }

                dialog.dismiss();
            }
        });

    }

    @Override
    public int getItemCount() {
        return stringArrayList.size();
    }

}