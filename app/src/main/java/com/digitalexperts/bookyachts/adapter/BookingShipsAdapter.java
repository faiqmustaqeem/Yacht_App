package com.digitalexperts.bookyachts.adapter;


import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.digitalexperts.bookyachts.R;
import com.digitalexperts.bookyachts.models.BookingHistoryModel;

import java.util.ArrayList;

/**
     * Created by hp on 1/31/2017.
     */
    public class BookingShipsAdapter extends RecyclerView.Adapter<BookingShipsAdapter.MyViewHolder>
{
    ArrayList<BookingHistoryModel>  bookingHistoryModelsArrayList = new ArrayList<>();
    Activity activity;


    public class MyViewHolder extends RecyclerView.ViewHolder
    {


        private LinearLayout liMain;
        private TextView tvYachtId , tvBookingDate,tvBookingTime,tvTripDuration,tvtotalAmount,tvAmountPaid , tvAmountRemaining ,tvDetails;




        public MyViewHolder(View view)
        {
            super(view);


            liMain = (LinearLayout) view.findViewById(R.id.liMain);

            //
            tvYachtId =(TextView)view.findViewById(R.id.txtyacht_title);
            tvBookingDate = (TextView) view.findViewById(R.id.tvBookingDate);
            tvBookingTime = (TextView) view.findViewById(R.id.tvBookingTime);
            tvTripDuration = (TextView) view.findViewById(R.id.tvTripDuration);
          //  tvSubTotal = (TextView) view.findViewById(R.id.tvSubTotal);
          //  tvDiscount = (TextView) view.findViewById(R.id.tvDiscount);
          //  tvFacilities = (TextView) view.findViewById(R.id.tvFacilities);
            tvtotalAmount = (TextView) view.findViewById(R.id.tvtotalAmount);
            tvAmountPaid=(TextView)view.findViewById(R.id.tvAmountPaid);
            tvAmountRemaining=(TextView)view.findViewById(R.id.tvAmountRemaining);


           // tvDetails = (TextView) view.findViewById(R.id.tvDetails);


        }
    }

    public BookingShipsAdapter(Activity activity, ArrayList<BookingHistoryModel>  bookingHistoryModelsArrayList)
    {

        this.activity = activity;
        this.bookingHistoryModelsArrayList = bookingHistoryModelsArrayList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_booking_ships_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {


        final BookingHistoryModel bookingHistoryModel = bookingHistoryModelsArrayList.get(position);
        holder.tvYachtId.setText("Yacht Title : "+ bookingHistoryModel.getYachtTitle());
        holder.tvBookingDate.setText("Booking Date : "+bookingHistoryModel.getBookingDate());
      //  holder.tvSubTotal.setText("Sub Total  "+bookingHistoryModel.getSubTotal());
      //  holder.tvDiscount.setText("Discount  "+bookingHistoryModel.getDiscount());
        holder.tvBookingTime.setText("Booking Time : "+bookingHistoryModel.getBookingTime());
       // holder.tvFacilities.setText(bookingHistoryModel.getFacilities());

        holder.tvtotalAmount.setText("Total Amount : "+bookingHistoryModel.getTotalAmount());
        holder.tvTripDuration.setText("Trip Duration "+bookingHistoryModel.getDuration()+ " Hour");
        holder.tvAmountPaid.setText("Amount Paid : "+bookingHistoryModel.getAmountPaid());
        holder.tvAmountRemaining.setText("Amount Remaining : "+bookingHistoryModel.getAmountRemaining());



//        holder.tvDetails.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//
////                AppConstants.yachtsModel.setId(bookingHistoryModel.getYachtId());
////                Intent intent = new Intent(activity,ShipDetailsActivity.class);
////                intent.putExtra("hideButtons","hideButtons");
////                activity.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return bookingHistoryModelsArrayList.size();
    }

}