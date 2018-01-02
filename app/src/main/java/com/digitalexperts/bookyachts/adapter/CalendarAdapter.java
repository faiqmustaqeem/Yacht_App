package com.digitalexperts.bookyachts.adapter;

/**
 * Created by hp on 5/15/2017.
 */

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.digitalexperts.bookyachts.R;
import com.digitalexperts.bookyachts.customClasses.AppConstants;
import com.digitalexperts.bookyachts.customClasses.CalendarCollection;
import com.digitalexperts.bookyachts.models.HotSlotModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import static com.digitalexperts.bookyachts.activity.BookingActivity.static_etDate;
import static com.digitalexperts.bookyachts.customClasses.AppConstants.bookingDate;


public class CalendarAdapter extends BaseAdapter {
    private Context context;

    private java.util.Calendar month;
    public GregorianCalendar pmonth;
    /**
     * calendar instance for previous month for getting complete view
     */
    public GregorianCalendar pmonthmaxset;
    private GregorianCalendar selectedDate;
    int firstDay;
    int maxWeeknumber;
    int maxP;
    int calMaxP;
    int lastWeekDay;
    int leftDays;
    int mnthlength;
    String itemvalue, curentDateString;
    DateFormat df;

    private ArrayList<String> items;
    public static List<String> day_string;
    private View previousView;
    public ArrayList<CalendarCollection> date_collection_arr = new ArrayList(1);

    public CalendarAdapter(Context context, GregorianCalendar monthCalendar, ArrayList<CalendarCollection> date_collection_arr) {
        this.date_collection_arr = date_collection_arr;
        CalendarAdapter.day_string = new ArrayList<String>();
        Locale.setDefault(Locale.US);
        month = monthCalendar;
        Log.e("monthCalendar",monthCalendar.toString());
        selectedDate = (GregorianCalendar) monthCalendar.clone();

        this.context = context;
        month.set(GregorianCalendar.DAY_OF_MONTH,1);

        this.items = new ArrayList<String>();
        df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        curentDateString = df.format(selectedDate.getTime());
        refreshDays();

    }

    public void setItems(ArrayList<String> items) {
        for (int i = 0; i != items.size(); i++) {
            if (items.get(i).length() == 1) {
                items.set(i, "0" + items.get(i));
            }
        }
        this.items = items;
    }

    public int getCount() {
        return day_string.size();
    }

    public Object getItem(int position) {
        return day_string.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new view for each item referenced by the Adapter
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public View getView(int position, final View convertView, ViewGroup parent) {
        View v = convertView;
        final TextView dayView;
        final View viewHotSlot;
        final ImageView ivFlag;
        if (convertView == null) { // if it's not recycled, initialize some
            // attributes
            LayoutInflater vi = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.layout_calendar_item, null);
        }

        dayView = (TextView) v.findViewById(R.id.date);
        viewHotSlot = (View) v.findViewById(R.id.viewHotSlot);
        ivFlag = (ImageView) v.findViewById(R.id.ivFlag);
        dayView.setTag(position);
        String[] separatedTime = day_string.get(position).split("-");

        Calendar cal = Calendar.getInstance();
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        int currentMonth = cal.get(Calendar.MONTH);

        String gridvalue = separatedTime[2].replaceFirst("^0*", "");
        if ((Integer.parseInt(gridvalue) > 1) && (position < firstDay)) {
            //dayView.setTextColor(Color.GRAY);
            dayView.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
            dayView.setBackground(context.getResources().getDrawable(R.drawable.calendar_selecter));
            dayView.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
            dayView.setClickable(false);
            dayView.setFocusable(false);

        } else if ((Integer.parseInt(gridvalue) < 7) && (position > 28)) {
            dayView.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
            dayView.setClickable(false);
            dayView.setFocusable(false);
        } else {
            // setting curent month's days in blue color.
            dayView.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
            dayView.setBackground(context.getResources().getDrawable(R.drawable.calendar_selecter));
            dayView.setText(gridvalue);
        }

        for (int i = 0; i < AppConstants.yachtsHotSlotsArrayList.size(); i++) {
            HotSlotModel hotSlotModel = AppConstants.yachtsHotSlotsArrayList.get(i);

            if (hotSlotModel.getStart_date().equals(day_string.get(position))) {

                ivFlag.setVisibility(View.VISIBLE);
            }
        }

        if (day_string.get(position).equals(curentDateString)) {
            dayView.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
            dayView.setBackground(context.getResources().getDrawable(R.drawable.calendar_selecter));
            previousView = dayView;

            String date = day_string.get(position);


            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date d = f.parse(date);
                // Constants.projectModel.setProjectStartingDate( d.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }

        } else {

        }

        dayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = day_string.get(Integer.parseInt((v).getTag().toString()));
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date currentDate = new Date();
                Date selectedDate1 = null;
                try {
                    selectedDate1 = dateFormat.parse(date);
                    //    currentDate=dateFormat.parse(currentDate.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                long daysDifference = ((selectedDate1.getTime() / (24 * 60 * 60 * 1000) - currentDate.getTime() / (24 * 60 * 60 * 1000)));
                Log.e("date_",String.valueOf(currentDate.getDate()));

                if (daysDifference < -1 ) {
                    Toast.makeText(context, "You can't select previous date", Toast.LENGTH_LONG).show();
                } else {
                    AppConstants.indexOfHotSlot = -1;


                    AppConstants.bookYachtModel.setStartDate(date);
                    for (int i = 0; i < AppConstants.yachtsHotSlotsArrayList.size(); i++) {
                        HotSlotModel hotSlotModel = AppConstants.yachtsHotSlotsArrayList.get(i);

                        if (hotSlotModel.getStart_date().equals(date)) {
                            AppConstants.indexOfHotSlot = i;
                            break;
                        } else {
                            AppConstants.indexOfHotSlot = -1;
                        }
                    }
                    Log.e("test", "selected date " + date);

                    static_etDate.setText(date);
                    bookingDate = date;



                    if (previousView != null) {
                        previousView.setBackground(context.getResources().getDrawable(R.drawable.calendar_selecter));
                        dayView.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
                        dayView.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
                    }
                    //Constants.dateSelected = date;

                    SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date d = f.parse(date);
                        //Constants.projectModel.setProjectStartingDate( d.getTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    dayView.setBackground(context.getResources().getDrawable(R.drawable.calendar_item_selecter));
                    dayView.setTextColor(context.getResources().getColor(android.R.color.darker_gray));

                    previousView = v;
                }

            }
        });

        // create date string for comparison
        String date = day_string.get(position);

        if (date.length() == 1) {
            date = "0" + date;
        }
        String monthStr = "" + (month.get(GregorianCalendar.MONTH) + 1);
        if (monthStr.length() == 1) {
            monthStr = "0" + monthStr;
        }

        return v;
    }


    public void refreshDays() {
        // clear items
        items.clear();
        day_string.clear();
        Locale.setDefault(Locale.US);
        pmonth = (GregorianCalendar) month.clone();
        //Log.e("pmonth",pmonth.getTime().toString());

        // month start day. ie; sun, mon, etc
        firstDay = month.get(GregorianCalendar.DAY_OF_WEEK);
        Log.e("firstday",String.valueOf(firstDay));
        // finding number of weeks in current month.
        maxWeeknumber = month.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH);
        Log.e("weeknumber",String.valueOf(maxWeeknumber));
        // allocating maximum row number for the gridview.
        mnthlength = maxWeeknumber * 7;
        Log.e("mnthlength",String.valueOf(mnthlength));
        maxP = getMaxP(); // previous month maximum day 31,30....
        Log.e("maxP",String.valueOf(maxP));
        calMaxP = maxP - (firstDay - 1);// calendar offday starting 24,25 ...
        Log.e("calmaxp",String.valueOf(calMaxP));
        /**
         * Calendar instance for getting a complete gridview including the three
         * month's (previous,current,next) dates.
         */
        pmonthmaxset = (GregorianCalendar) pmonth.clone();
        Log.e("pmonth",pmonth.getTime().toString());
        Log.e("pmonthmaxset",pmonthmaxset.getTime().toString());
        /**
         * setting the start date as previous month's required date.
         */
        pmonthmaxset.set(GregorianCalendar.DAY_OF_MONTH, calMaxP + 1);

        /**
         * filling calendar gridview.
         */
        for (int n = 0; n < mnthlength; n++) {
            itemvalue = df.format(pmonthmaxset.getTime());
            Log.e("itemvalue "+n,String.valueOf(itemvalue));
            pmonthmaxset.add(GregorianCalendar.DATE, 1);
            day_string.add(itemvalue);
        }
    //    Log.e("day_string",day_string.size()+"   "+day_string.toString());
    }

    private int getMaxP() {
        int maxP;
        int a = month.get(GregorianCalendar.MONTH);
        Log.e("aaa",String.valueOf(a));
        int b = month.getActualMaximum(GregorianCalendar.MONTH);
        Log.e("bbb",String.valueOf(b));
      //  if (a == b) {

         //   pmonth.set((month.get(GregorianCalendar.YEAR) - 1),
            //        month.getActualMaximum(GregorianCalendar.MONTH), 1);
       // } else
         {
            pmonth.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) - 1);
        }
        maxP = pmonth.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

        return maxP;
    }
}