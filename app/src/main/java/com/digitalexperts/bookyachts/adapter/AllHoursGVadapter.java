package com.digitalexperts.bookyachts.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.digitalexperts.bookyachts.R;
import com.digitalexperts.bookyachts.activity.BookingTimeActivity;
import com.digitalexperts.bookyachts.customClasses.AppConstants;
import com.digitalexperts.bookyachts.models.BookingHoursModel;

import java.util.List;

import static com.digitalexperts.bookyachts.activity.BookingTimeActivity.static_etStartTime;
import static com.digitalexperts.bookyachts.activity.BookingTimeActivity.static_gvAllHours;
//import static com.digitalexperts.bookyachts.activity.BookingTimeActivity.static_tvDuration;
import static com.digitalexperts.bookyachts.customClasses.AppConstants.bookingDuration;
import static com.digitalexperts.bookyachts.customClasses.AppConstants.bookingStartTime;
import static com.digitalexperts.bookyachts.customClasses.AppConstants.bookingStartTimeIndex;

/**
 * Created by Codiansoft on 10/24/2017.
 */

public class AllHoursGVadapter extends BaseAdapter {
    List<BookingHoursModel> allHours;
    Context context;
    int timeSelectCount = 0;

    public AllHoursGVadapter(Context context, List<BookingHoursModel> allHours) {
        this.context = context;
        this.allHours = allHours;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {

            gridView = new View(context);


            Log.e("test_allhours","test");
            // get layout from mobile.xml
            gridView = inflater.inflate(R.layout.rv_hour_item, null);

            final BookingHoursModel bookingHoursModel = allHours.get(position);

            // set value into textview
            final TextView tvHours = (TextView) gridView
                    .findViewById(R.id.tvHours);
            tvHours.setText(bookingHoursModel.getTimeText());

            if (bookingHoursModel.getIsSelected().equals("false")) {
//                tvHours.setBackgroundColor(context.getResources().getColor(android.R.color.white));
              //  tvHours.setBackground(context.getResources().getDrawable(R.drawable.calendar_selecter));
                tvHours.setBackgroundColor(context.getResources().getColor(R.color.light_green));
            } else {
                tvHours.setBackgroundColor(context.getResources().getColor(R.color.light_blue));
            }

            if (bookingHoursModel.getIsAlreadyBooked().equals("true")) {
                tvHours.setBackgroundColor(context.getResources().getColor(R.color.red));
            }


            tvHours.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bookingHoursModel.getIsAlreadyBooked().equals("true")) {
                        Toast.makeText(context, "Already booked", Toast.LENGTH_SHORT).show();
                    } else {
//                        if (timeSelectCount == 0)
                        {
                            timeSelectCount++;
                            for (int i = 0; i < allHours.size(); i++) {
                                allHours.get(i).setIsSelected("false");
                            }

                            bookingHoursModel.setIsSelected("true");
                            bookingStartTimeIndex = position;
                            // AppConstants.duration = Integer.parseInt(txtDuration.getText().toString());
                            // AppConstants.bookYachtModel.setDuration(txtDuration.getText().toString());
                            // bookingDuration = Integer.parseInt(txtDuration.getText().toString());

                            tvHours.setBackgroundColor(context.getResources().getColor(R.color.light_blue));
                            AppConstants.bookYachtModel.setStartTime( bookingHoursModel.getTimeText());
                            static_etStartTime.setText(AppConstants.bookYachtModel.getStartTime());
                           // static_tvDuration.setText("");

                            notifyDataSetInvalidated();
                            static_gvAllHours.invalidateViews();


//                            Toast.makeText(context, "Select end time", Toast.LENGTH_SHORT).show();

                        }
//                        else if (timeSelectCount == 1) {
//                            //position > (bookingStartTimeIndex + 1
//                            if (position >= (bookingStartTimeIndex + 1)) {
//
//                                boolean validRange = true;
//                                for (int i = bookingStartTimeIndex; i < position + 1; i++) {
//                                    if (allHours.get(i).getIsAlreadyBooked().equals("true")) {
//                                        validRange = false;
//                                        break;
//                                    }
//                                }
//
//                                if (validRange) {
//                                    int duration=position - bookingStartTimeIndex+1;
//                                    if(duration > 8)
//                                    {
//                                        Toast.makeText(context, "You can select Maximum 8 hours", Toast.LENGTH_SHORT).show();
//                                    }
//                                    else {
//
//                                        timeSelectCount = 0;
//                                        for (int i = bookingStartTimeIndex; i < (position + 1); i++) {
////                                    bookingHoursModel.setIsSelected("true");
//                                            allHours.get(i).setIsSelected("true");
//                                        }
//                                        tvHours.setBackgroundColor(context.getResources().getColor(R.color.light_blue));
//
//                                        AppConstants.bookYachtModel.setDuration(String.valueOf(position - bookingStartTimeIndex+1));
//                                        AppConstants.duration=position - bookingStartTimeIndex+1;
//                                        static_tvDuration.setText(AppConstants.bookYachtModel.getDuration() + " HOURS");
//                                    }
//                                } else {
//                                    Toast.makeText(context, "You can not include booked hours", Toast.LENGTH_SHORT).show();
//                                }
//
//                            } else if (position == bookingStartTimeIndex) {
//                                for (int i = 0; i < allHours.size(); i++) {
//                                    allHours.get(i).setIsSelected("false");
//                                }
//                                timeSelectCount = 0;
//                                static_etStartTime.setText("");
//                                static_tvDuration.setText("");
//                                Toast.makeText(context, "Select start time", Toast.LENGTH_SHORT).show();
//                            } else
//                                {
//                                Toast.makeText(context, "Not valid! Minimum duration must be of 2 hours", Toast.LENGTH_SHORT).show();
//                            }
//                            notifyDataSetInvalidated();
//                            static_gvAllHours.invalidateViews();
//                        }
                        AllHoursGVadapter.this.notifyDataSetChanged();
                        notifyDataSetChanged();
                        notifyDataSetInvalidated();
                        static_gvAllHours.invalidateViews();
                    }
                }
            });

        } else {
            gridView = (View) convertView;

            final BookingHoursModel bookingHoursModel = allHours.get(position);

            // set value into textview
            final TextView tvHours = (TextView) gridView
                    .findViewById(R.id.tvHours);
            tvHours.setText(bookingHoursModel.getTimeText());

            if (bookingHoursModel.getIsSelected().equals("false")) {
//                tvHours.setBackgroundColor(context.getResources().getColor(android.R.color.white));
               // tvHours.setBackground(context.getResources().getDrawable(R.drawable.calendar_selecter));
                tvHours.setBackgroundColor(context.getResources().getColor(R.color.light_green));
            } else {
                tvHours.setBackgroundColor(context.getResources().getColor(R.color.light_blue));
            }

            if (bookingHoursModel.getIsAlreadyBooked().equals("true")) {
                tvHours.setBackgroundColor(context.getResources().getColor(R.color.red));
            }

        }
        return gridView;
    }

    @Override
    public int getCount() {
        return allHours.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}