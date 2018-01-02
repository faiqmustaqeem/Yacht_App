package com.digitalexperts.bookyachts.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.digitalexperts.bookyachts.R;

import java.util.List;

/**
 * Created by Codiansoft on 10/23/2017.
 */

public class RVHoursAdapter extends RecyclerView.Adapter<RVHoursAdapter.MyViewHolder> {

    List<String> allHours;
    Context context;

    public RVHoursAdapter(List<String> allHours, Context context) {
        this.allHours = allHours;
        this.context = context;
    }

    @Override
    public RVHoursAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_hour_item,parent,false);
        MyViewHolder viewHolder=new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RVHoursAdapter.MyViewHolder holder, int position) {
        holder.tvHours.setText(allHours.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return allHours.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        protected TextView tvHours;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvHours= (TextView) itemView.findViewById(R.id.tvHours);
        }
    }
}