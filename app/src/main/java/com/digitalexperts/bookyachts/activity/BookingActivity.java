package com.digitalexperts.bookyachts.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.digitalexperts.bookyachts.models.BookYachtModel;
import com.digitalexperts.bookyachts.models.Booking;
import com.google.gson.Gson;
import com.digitalexperts.bookyachts.R;
import com.digitalexperts.bookyachts.ResideMenu.ResideMenu;
import com.digitalexperts.bookyachts.ResideMenu.ResideMenuItem;
import com.digitalexperts.bookyachts.adapter.CalendarAdapter;
import com.digitalexperts.bookyachts.customClasses.AppConstants;
import com.digitalexperts.bookyachts.customClasses.AppController;
import com.digitalexperts.bookyachts.customClasses.CalendarCollection;
import com.digitalexperts.bookyachts.dialogs.CallBackDialog;
import com.digitalexperts.bookyachts.dialogs.ContactUsDialog;
import com.digitalexperts.bookyachts.interfaces.ShowDataInterface;
import com.digitalexperts.bookyachts.interfaces.WebServices;
import com.digitalexperts.bookyachts.models.HotSlotModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.digitalexperts.bookyachts.customClasses.AppConstants.bookingDuration;
import static com.digitalexperts.bookyachts.customClasses.AppConstants.bookingStartTime;


public class BookingActivity extends AppCompatActivity implements ShowDataInterface, View.OnClickListener {
    public GregorianCalendar cal_month, cal_month_copy;
    @BindView(R.id.ivBackMonth)
    ImageButton ivBackMonth;
    @BindView(R.id.tvMonth)
    TextView tvMonth;
    @BindView(R.id.ivForwardMonth)
    ImageButton ivForwardMonth;
    @BindView(R.id.gvCalendar)
    GridView gvCalendar;
    @BindView(R.id.txtName)
    EditText txtName;
    @BindView(R.id.txtPhoneNumber)
    EditText txtPhoneNumber;
    @BindView(R.id.txtEmail)
    EditText txtEmail;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.txtSelectYacht)
    TextView txtSelectYacht;
    @BindView(R.id.etDate)
    EditText etDate;

    @BindView(R.id.txtGuest)
    TextView txtGuest;
    @BindView(R.id.btnProceedtoTimings)
    Button btnProceedtoTimings;
//    @BindView(R.id.txtStartTime)
//    EditText txtStartTime;
    @BindView(R.id.txtDuration)
    EditText txtDuration;
    @BindView(R.id.tvViewFacilities)
    TextView tvViewFacilities;
    @BindView(R.id.tvInclusiveFacilities)
    TextView tvInclusiveFacilities;
    @BindView(R.id.tvExclusiveFacilities)
    TextView tvExclusiveFacilities;
    @BindView(R.id.liViewFacilities)
    LinearLayout liViewFacilities;
    @BindView(R.id.tvAddOnFacilities)
    TextView tvAddOnFacilities;
    @BindView(R.id.liHotslots)
    LinearLayout liHotslots;
    private CalendarAdapter calAdapter;
    GregorianCalendar previousCalMonth;
    String period = "";

    ProgressDialog progressDialog;

    Activity activity;

    public static EditText static_etDate;

    boolean isAvailable = true;


    //Bottom navigation views
    @BindView(R.id.ivNavigationIcon)
    ImageView ivNavigationIcon;
    @BindView(R.id.ivHome)
    ImageView ivHome;
    @BindView(R.id.ivCallBack)
    ImageView ivCallBack;
    @BindView(R.id.ivHotDeals)
    ImageView ivHotDeals;
    @BindView(R.id.ivContactUs)
    ImageView ivContactUs;

    @BindView(R.id.pay20Percent)
    RadioButton pay20Percent;

    @BindView(R.id.pay100Percent)
    RadioButton pay100Percent;

    private ResideMenu resideMenu;
    private ResideMenuItem itemBookings;
    private ResideMenuItem itemUpdateProfile;
    boolean isTimeSet=false;

    String hour="01",AM_PM="AM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Hide the status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        ButterKnife.bind(this);

        // hiding action bar
        getSupportActionBar().hide();
        static_etDate = etDate;

        AppConstants.bookYachtModel.setStartDate("");
        AppConstants.bookYachtModel=new BookYachtModel();

        AppConstants.exclusiveFacilities = "";

        AppConstants.duration = 0;
        AppConstants.totalAmount = 0;
        AppConstants.discountAmount = 0;
        AppConstants.discountPercentage = 0;
        AppConstants.subFacilityTotal = 0;
        AppConstants.indexOfHotSlot = -1;
        AppConstants.pay20Percent=false;

         AppConstants.discountAmount = 0.0;
        AppConstants.amountPayingNow = 0.0;
        AppConstants.totalAmount = 0.0;
        AppConstants.remainingAmount = 0.0;
        AppConstants.duration = 0;



        AppConstants.bookingDate = "";
        AppConstants.bookingStartTimeIndex=0;
        AppConstants.bookingDuration=0;
        AppConstants.bookingStartTime="";
        AppConstants.order_id="0";


        cal_month = (GregorianCalendar) GregorianCalendar.getInstance();

        Log.e("cal_month",cal_month.toString());
        cal_month_copy = (GregorianCalendar) cal_month.clone();
        calAdapter = new CalendarAdapter(this, cal_month, CalendarCollection.date_collection_arr);
        gvCalendar.setAdapter(calAdapter);

        tvMonth.setText(DateFormat.format("MMMM yyyy", cal_month));

        activity = this;

        tvPrice.setText(AppConstants.yachtsModel.getPrice());
        txtSelectYacht.setText(AppConstants.yachtsModel.getTitle().substring(0, 2) + "ft");
//       txtGuest.setHint("Max Number Of Guest " + AppConstants.yachtsModel.getMax_guest() + " Guests");

        txtGuest.setHint("Max Pax " + AppConstants.yachtsModel.getMax_guest());
        // setting facilities
        tvInclusiveFacilities.setText(AppConstants.inclusiveFacilities);

        if(AppConstants.selectedFacilities.size() > 1) {
            for (int i = 1; i < AppConstants.selectedFacilities.size(); i++) {
                AppConstants.exclusiveFacilities += AppConstants.selectedFacilities.get(i).toString() + ",";
            }
            tvExclusiveFacilities.setText(AppConstants.exclusiveFacilities);
        } else {
            tvExclusiveFacilities.setText("No Facilities Added");
        }

//        txtGuest.setFilters(new InputFilter[]{new InputFilterMinMax("1", AppConstants.yachtsModel.getMax_guest())});
        // yahan chng kia hai duration , 1 se 2 kia hai min
        txtDuration.setFilters(new InputFilter[]{new InputFilterMinMax("2", "8")});

//        txtStartTime.setOnClickListener(new View.OnClickListener() {
//             @Override
//            public void onClick(View v) {
//                 isTimeSet=true;
//                // TODO Auto-generated method stub
//                Calendar mcurrentTime = Calendar.getInstance();
//                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//              //  int minute = mcurrentTime.get(Calendar.MINUTE);
//                int minute=0;
//                 TimePickerDialog mTimePicker;
//                mTimePicker = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                        String AM_PM ;
//                        if(selectedHour < 12) {
//                            AM_PM = "AM";
//                            if(selectedHour == 0)
//                            {
//                                bookingStartTime = "12" + ":" + "00"+ " "+AM_PM;
//                            }
//                            else if(selectedHour< 10)
//                            {
//                                bookingStartTime = "0"+selectedHour + ":" + "00"+ " "+AM_PM;
//                            }
//                            else
//                            {
//                                bookingStartTime = selectedHour + ":" + "00" + " " + AM_PM;
//                            }
//                        } else
//                            {
//                            AM_PM = "PM";
//                            if ((selectedHour-12) == 0)
//                            {
//                                bookingStartTime = "12" + ":" + "00"+ " "+AM_PM;
//                            }
//                            else if( (selectedHour-12) < 10)
//                            {
//                                bookingStartTime = "0"+(selectedHour-12) + ":" + "00"+ " "+AM_PM;
//                            }
//                            else
//                            {
//                                bookingStartTime = (selectedHour-12) + ":" + "00" + " " + AM_PM;
//                            }
//
//                        }
//
//
//                        txtStartTime.setText(bookingStartTime);
//
//                        if (AppConstants.indexOfHotSlot > -1) {
//                            HotSlotModel hotSlotModel = AppConstants.yachtsHotSlotsArrayList.get(AppConstants.indexOfHotSlot);
//
//                            if (hotSlotModel.getStart_time().equals(selectedHour + "")) {
//                                AppConstants.discountPercentage = Integer.parseInt(hotSlotModel.getDiscount());
//                            } else {
//                                AppConstants.discountPercentage = 0;
//                            }
//                        }
//                    }
//                }, hour, minute, false);
//                mTimePicker.setTitle("Select Time");
//
//                mTimePicker.show();
//            }
//        });

        txtDuration.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (AppConstants.bookYachtModel.getStartDate().equals(""))
                {
                    Toast.makeText(activity, "Please Select Date", Toast.LENGTH_SHORT).show();
                }

            }
        });

        Spinner spiiner_hours=(Spinner)findViewById(R.id.hours_spinner);
        List<String> hours=new ArrayList<>();

        Spinner spinner_AM_PM=(Spinner)findViewById(R.id.am_pm_spinner);
        List<String> AM_PM_list=new ArrayList<>();

        AM_PM_list.add("AM");
        AM_PM_list.add("PM");

        hours.add("01");
        hours.add("02");
        hours.add("03");
        hours.add("04");
        hours.add("05");
        hours.add("06");
        hours.add("07");
        hours.add("08");
        hours.add("09");
        hours.add("10");
        hours.add("11");
        hours.add("12");



        ArrayAdapter adapter1 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,hours);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiiner_hours.setAdapter(adapter1);
        spiiner_hours.setSelected(true);
        spiiner_hours.setSelection(0);
        spiiner_hours.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                hour=adapterView.getItemAtPosition(i).toString();
                bookingStartTime=hour+":00 "+AM_PM;
                Log.e("bookingstarttime",bookingStartTime);
                isTimeSet=true;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        ArrayAdapter adapter2 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,AM_PM_list);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_AM_PM.setAdapter(adapter2);
        spinner_AM_PM.setSelected(true);
        spinner_AM_PM.setSelection(0);
        spinner_AM_PM.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                AM_PM=adapterView.getItemAtPosition(i).toString();
                bookingStartTime=hour+":00 "+AM_PM;
                Log.e("bookingstarttime",bookingStartTime);
                isTimeSet=true;


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        setUpMenu();


    }

    private void setUpMenu() {
        // attach to current activity;
        resideMenu = new ResideMenu(this);
        //resideMenu.setUse3D(true);
        resideMenu.setBackground(R.drawable.splash_bg_two);
        resideMenu.attachToActivity(this);

        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip.
        resideMenu.setScaleValue(0.6f);

        resideMenu.setDirectionDisable(ResideMenu.DIRECTION_LEFT);
        resideMenu.setDirectionDisable(ResideMenu.DIRECTION_RIGHT);

        // create menu items;
        // itemHome = new ResideMenuItem(this, R.drawable.nav_home, "Home");
        itemBookings = new ResideMenuItem(this, R.drawable.icon_booking, "Booking History");
        itemUpdateProfile = new ResideMenuItem(this, R.drawable.icon_profile, "User Profile");

        //itemHome.setOnClickListener(this);
        itemBookings.setOnClickListener(this);
        itemUpdateProfile.setOnClickListener(this);

        // resideMenu.addMenuItem(itemHome, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemBookings, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemUpdateProfile, ResideMenu.DIRECTION_LEFT);

        // You can dsable a direction by setting ->
        // resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);

        findViewById(R.id.ivNavigationIcon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });
    }


    @Override
    public void showData(String data) {
        txtDuration.setText(data);
        AppConstants.bookYachtModel.setStartTime(data);
    }

    public class InputFilterMinMax implements InputFilter {

        private int min, max;

        public InputFilterMinMax(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public InputFilterMinMax(String min, String max) {
            this.min = Integer.parseInt(min);
            this.max = Integer.parseInt(max);
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                try {
                int input = Integer.parseInt(dest.toString() + source.toString());
                if (isInRange(min, max, input))
                    return null;
            } catch (NumberFormatException nfe) {
            }
            return "";
        }

        private boolean isInRange(int a, int b, int c) {
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }
    }

    @Override
    protected void onRestart() {
        AppConstants.exclusiveFacilities = "";
        super.onRestart();
        if (AppConstants.selectedFacilities.size() > 1) {
            for (int i = 1; i < AppConstants.selectedFacilities.size(); i++) {
                if (i == AppConstants.selectedFacilities.size() - 1) {
                    AppConstants.exclusiveFacilities += AppConstants.selectedFacilities.get(i).toString();
                } else {
                    AppConstants.exclusiveFacilities += AppConstants.selectedFacilities.get(i).toString() + ",";
                }
            }
            tvExclusiveFacilities.setText(AppConstants.exclusiveFacilities);
        } else {
            tvExclusiveFacilities.setText("No Facilities Added");
        }

    }

    @OnClick({R.id.ivBackMonth, R.id.liHotslots, R.id.tvAddOnFacilities, R.id.ivForwardMonth, R.id.btnProceedtoTimings, /*bottom navigation*/R.id.ivNavigationIcon, R.id.ivCallBack, R.id.ivContactUs, R.id.ivHotDeals, R.id.ivHome})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvAddOnFacilities:
                //startActivity(new Intent(activity, AddExclusiveFacilitiesActivity.class));
                break;

            case R.id.liHotslots:
               // startActivity(new Intent(activity, ShowHotSlotsActivity.class));
                break;

            case R.id.ivBackMonth:
                setPreviousMonth();
                refreshCalendar();

                break;
            case R.id.ivForwardMonth:
                setNextMonth();
                refreshCalendar();
                break;

            case R.id.btnProceedtoTimings:
                if (etDate.getText().toString().equals("")) {
                    Toast.makeText(BookingActivity.this, "Select a date", Toast.LENGTH_SHORT).show();
                } else if (txtDuration.getText().toString().equals("1")) {
                    Toast.makeText(activity, "Minimum duration must be 2 hours", Toast.LENGTH_SHORT).show();
                } else if (txtDuration.getText().toString().equals("")) {
                    Toast.makeText(activity, "Please mention your trip duration", Toast.LENGTH_SHORT).show();
                }
                else if (isTimeSet==false) {
                    Toast.makeText(activity, "Please select start Time", Toast.LENGTH_SHORT).show();
                }
                else {
                    checkTimeAvailability();
                }
                break;

            //bottom navigation
            case R.id.ivNavigationIcon:
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
                break;
            case R.id.ivCallBack:
                new CallBackDialog(BookingActivity.this);
                break;
            case R.id.ivContactUs:
                new ContactUsDialog(BookingActivity.this);
                break;
            case R.id.ivHotDeals:
                Intent i = new Intent(BookingActivity.this, HotDealsActivity.class);
                startActivity(i);
                break;
            case R.id.ivHome:
                finish();
                ShipDetailsActivity.activity.finish();

                break;
        }
    }

    private void checkTimeAvailability() {
        progressDialog = new ProgressDialog(BookingActivity.this);
        progressDialog.setTitle("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgressNumberFormat(null);
        progressDialog.setProgressPercentFormat(null);
        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WebServices service = retrofit.create(WebServices.class);

        HashMap<String, String> body = new HashMap<>();

        body.put("yacht_id", AppConstants.yachtsModel.getId());
        body.put("booking_date", AppConstants.bookYachtModel.getStartDate());



        service.fetchBookedTimes(body).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Gson gson = new Gson();
                String json = gson.toJson(response.body());

                if (response.isSuccessful()) {
                    try {

                        JSONObject jsonObject = new JSONObject(json);
                        JSONObject resultObject = jsonObject.getJSONObject("result");
                        String status = resultObject.getString("status");
                        String message = resultObject.getString("response");
                        if (status.equals("success")) {

                            Log.e("booked_times","1");

                            JSONObject data = jsonObject.getJSONObject("data");
                            Log.e("booked_times","2");
                            if(message.equals("Yacht Booking Times Received")){
                                JSONObject booked_times = data.getJSONObject("booked_times");
                                Log.e("booked_times","3");
                                  //if(booked_times.has("times"))

                                JSONArray times = booked_times.getJSONArray("times");
                                JSONArray duration = booked_times.getJSONArray("duration");
                               // JSONArray discount = booked_times.getJSONArray("discount");


                                Log.e("booked_times", "4");
                                Log.e("booked_times", booked_times.toString());

                                if (times.length() > 0) {
                                    for (int j = 0; j < times.length(); j++) {
                                        String old24=convert12HoursFormatTo24Hours(times.getString(j)).substring(0,2);
                                        String new24=convert12HoursFormatTo24Hours(bookingStartTime).substring(0,2);
                                        Log.e("str24",old24);
                                        Log.e("str24",new24);
                                        Booking oldBooking=new Booking(Integer.parseInt(old24),Integer.parseInt(duration.getString(j)));
                                        Booking newBooking=new Booking(Integer.parseInt(new24),Integer.parseInt(txtDuration.getText().toString()));
                                       if(!canBook(oldBooking,newBooking))
                                       {
                                           isAvailable=false;
                                          // int d = Integer.parseInt(discount.getString(j).substring(0, 2));
                                           // AppConstants.discountPercentage = d;
                                           Log.e("canbook","true");
                                            break;
                                       }
                                       else
                                       {
                                           isAvailable=true;
                                          // AppConstants.discountPercentage = 0;
                                           Log.e("canbook","false");
                                       }


                                    }
                                } else {
                                    isAvailable = true;
                                }
                            }
                            else
                            {
                                isAvailable=true;
                            }

                            progressDialog.dismiss();

                            if (isAvailable) {
                                AppConstants.discountPercentage=0;

                                AppConstants.bookYachtModel.setDuration(txtDuration.getText().toString());
                                Log.e("available",txtDuration.getText().toString());
                                bookingDuration = Integer.parseInt(txtDuration.getText().toString());
                                AppConstants.bookYachtModel.setStartTime(bookingStartTime);
                                Log.e("yahan_agaya","1");
                                startActivity(new Intent(activity, ConfirmBookingDetailsActivity.class));

                                Log.e("yahan_agaya","2");

                            }
                            else {



                                startActivity(new Intent(activity, BookingTimeActivity.class));
                            }
                            activity.finish();
/*
                            //go to confirm booking activity
                            Intent i = new Intent(BookingTimeActivity.this, ConfirmBookingDetailsActivity.class);
                            startActivity(i);*/

                        } else {

                            progressDialog.dismiss();
                            Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {

                        progressDialog.dismiss();
                        Toast.makeText(activity, "Error", Toast.LENGTH_LONG).show();
                        Log.e("error",e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    progressDialog.dismiss();
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(BookingActivity.this, jObjError.getJSONObject("result").getString("response") , Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(BookingActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(activity, "Internet Error", Toast.LENGTH_LONG).show();

            }
        });
    }

    protected void setNextMonth() {
        int a = cal_month.get(GregorianCalendar.MONTH);
        int b = cal_month.getActualMaximum(GregorianCalendar.MONTH);
        if (a == b) {
            cal_month.set((cal_month.get(GregorianCalendar.YEAR) + 1),
                    cal_month.getActualMinimum(GregorianCalendar.MONTH), 1);
        } else {
            cal_month.set(GregorianCalendar.MONTH,
                    cal_month.get(GregorianCalendar.MONTH) + 1);
        }

    }

    protected void setPreviousMonth() {
        int a = cal_month.get(GregorianCalendar.MONTH);
        int b = cal_month.getActualMaximum(GregorianCalendar.MONTH);

        if (a == Calendar.getInstance().get(Calendar.MONTH) && cal_month.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)) {

        } else {
            cal_month.set(GregorianCalendar.MONTH, cal_month.get(GregorianCalendar.MONTH) - 1);
        }
    }

    public void refreshCalendar() {
        calAdapter.refreshDays();
        // cal_adapter.notifyDataSetChanged();
        calAdapter = new CalendarAdapter(this, cal_month, CalendarCollection.date_collection_arr);
        gvCalendar.setAdapter(calAdapter);
        tvMonth.setText(DateFormat.format("MMMM yyyy", cal_month));
    }

    @OnClick(R.id.tvViewFacilities)
    public void onClick() {
        if (liViewFacilities.getVisibility() == View.GONE) {
            liViewFacilities.setVisibility(View.VISIBLE);
        } else {
            liViewFacilities.setVisibility(View.GONE);
        }
    }
    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.pay20Percent:
                if (checked)
                {
                    AppConstants.pay20Percent=true;
                }
                    break;
            case R.id.pay100Percent:
                if (checked)
                {
                    AppConstants.pay20Percent=false;
                }
                    break;
        }
    }
    public  boolean canBook(Booking oldBooking , Booking newBooking)
    {
        if(newBooking.startTime== oldBooking.startTime)
        {
            return false;
        }
        else if( newBooking.startTime+newBooking.duration > oldBooking.startTime
                &&newBooking.startTime+newBooking.duration < oldBooking.startTime+oldBooking.duration)
        {
            return false;
        }
        else if(newBooking.startTime > oldBooking.startTime
                &&newBooking.startTime <oldBooking.startTime+oldBooking.duration)
        {
            return false;
        }
        else if(newBooking.startTime < oldBooking.startTime
                && newBooking.startTime+newBooking.duration >= oldBooking.startTime+oldBooking.duration)
        {
            return false;
        }
        else if(newBooking.startTime >= oldBooking.startTime
                && newBooking.startTime+newBooking.duration <= oldBooking.startTime+oldBooking.duration)
        {
            return false;
        }
        return true;
    }

    private  String convert12HoursFormatTo24Hours(String time12) {
        String time24 = "";

        SimpleDateFormat date12Format = new SimpleDateFormat("hh:mm a");

        SimpleDateFormat date24Format = new SimpleDateFormat("HH:mm");
        try {
            time24=(date24Format.format(date12Format.parse(time12)));
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return time24;
    }
    private  String convert24HoursFormatTo12Hours(String time24) {
        String time12 = "";

        SimpleDateFormat date12Format = new SimpleDateFormat("hh:mm a");

        SimpleDateFormat date24Format = new SimpleDateFormat("HH:mm");
        try {
            time12=(date12Format.format(date24Format.parse(time24)));
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return time12;
    }
}