package com.digitalexperts.bookyachts.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.digitalexperts.bookyachts.R;
import com.digitalexperts.bookyachts.models.HotDealsModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Codiansoft on 10/28/2017.
 */

public class HotDealsRVAdapter extends RecyclerView.Adapter<HotDealsRVAdapter.MyViewHolder> {
    ArrayList<HotDealsModel> hotDealsList;
    Context context;

    public HotDealsRVAdapter(Context context, ArrayList<HotDealsModel> announcementsList) {
        this.hotDealsList = announcementsList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvDescription)
        TextView tvDescription;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public HotDealsRVAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.hot_deals_rv_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HotDealsRVAdapter.MyViewHolder holder, int position) {
        HotDealsModel dataModel = hotDealsList.get(position);
        holder.tvTitle.setText(dataModel.getTitle());
        holder.tvDescription.setText(dataModel.getDescription());
    }

    @Override
    public int getItemCount() {
        return hotDealsList.size();
    }
}
