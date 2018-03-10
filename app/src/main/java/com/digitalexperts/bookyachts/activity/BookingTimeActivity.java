package com.digitalexperts.bookyachts.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.digitalexperts.bookyachts.models.Booking;
import com.google.gson.Gson;
import com.digitalexperts.bookyachts.R;
import com.digitalexperts.bookyachts.adapter.AllHoursGVadapter;
import com.digitalexperts.bookyachts.adapter.RVHoursAdapter;
import com.digitalexperts.bookyachts.customClasses.AppConstants;
import com.digitalexperts.bookyachts.customClasses.AppController;
import com.digitalexperts.bookyachts.interfaces.WebServices;
import com.digitalexperts.bookyachts.models.BookingHoursModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

import static com.digitalexperts.bookyachts.customClasses.AppConstants.bookYachtModel;
import static com.digitalexperts.bookyachts.customClasses.AppConstants.bookingDate;
import static com.digitalexperts.bookyachts.customClasses.AppConstants.bookingDuration;
import static com.digitalexperts.bookyachts.customClasses.AppConstants.bookingStartTime;

public class BookingTimeActivity extends AppCompatActivity {
    @BindView(R.id.tvNewPrice)
    TextView tvNewPrice;

    @BindView(R.id.tvOldPrice)
    TextView tvOldPrice;

    @BindView(R.id.txtSelectYacht)
    TextView tvSelectYacht;



    @BindView(R.id.etDate)
    EditText etDate;
    @BindView(R.id.etStartTime)
    EditText etStartTime;

    @BindView(R.id.tvDay)
    TextView tvDay;

    @BindView(R.id.ivBackMonth)
    ImageView ivBackDay;
    @BindView(R.id.ivForwardMonth)
    ImageView ivForwardDay;
    @BindView(R.id.spinner_duration)
    Spinner durationSpinner;



    public static EditText static_etStartTime;

  //  @BindView(R.id.tvDuration)
   // EditText tvDuration;
  //  public static EditText static_tvDuration;

    ProgressDialog progressDialog;

/*    @BindView(R.id.rvTimings1)
    RecyclerView rvTimings1;
    @BindView(R.id.rvTimings2)
    RecyclerView rvTimings2;
    @BindView(R.id.rvTimings3)
    RecyclerView rvTimings3;
    @BindView(R.id.rvTimings4)
    RecyclerView rvTimings4;*/

    @BindView(R.id.gvAllHours)
    GridView gvAllHours;
    public static GridView static_gvAllHours;


    List<BookingHoursModel> allHours = new ArrayList<BookingHoursModel>();
//    RVHoursAdapter hoursAdapter;
    AllHoursGVadapter mAdapter;

    Activity activity;

    boolean isAvailable=false;
    String durationStr="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_time);
        ButterKnife.bind(this);

        activity=this;

       // tvSelectYacht.setText();
        tvSelectYacht.setText(AppConstants.yachtsModel.getTitle().substring(0, 2) + "ft");

           tvOldPrice.setText(AppConstants.yachtsModel.getPrice());

        tvNewPrice.setText(AppConstants.yachtsModel.getDiscountedPrice());
        AppConstants.yachtsModel.setPrice(AppConstants.yachtsModel.getDiscountedPrice());

        initUI();

        static_gvAllHours = gvAllHours;

        fetchBookedSlots();
        tvDay.setText(getDateInWords(AppConstants.bookYachtModel.getStartDate()));


        ivForwardDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newDate=addOneDay(AppConstants.bookYachtModel.getStartDate());
                AppConstants.bookYachtModel.setStartDate(newDate);
                initializeAllHours();
                tvDay.setText(getDateInWords(newDate));
                etDate.setText(newDate);
                fetchBookedSlots();
            }
        });

        ivBackDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newDate=subtractOneDay(AppConstants.bookYachtModel.getStartDate());
                AppConstants.bookYachtModel.setStartDate(newDate);
                initializeAllHours();
                tvDay.setText(getDateInWords(newDate));
                etDate.setText(newDate);
                fetchBookedSlots();
            }
        });
        List<String> hours_list=new ArrayList<>();
        hours_list.add("2");
        hours_list.add("3");
        hours_list.add("4");
        hours_list.add("5");
        hours_list.add("6");
        hours_list.add("7");
        hours_list.add("8");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, hours_list);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        durationSpinner.setAdapter(adapter);
        durationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                durationStr=adapterView.getItemAtPosition(i).toString();
                AppConstants.bookYachtModel.setDuration(durationStr);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
             durationStr="2";
                AppConstants.bookYachtModel.setDuration(durationStr);
            }
        });

    }

    private void fetchBookedSlots() {
        progressDialog = new ProgressDialog(BookingTimeActivity.this);
        progressDialog.setMessage("Loading...");
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
            Log.e("body",body.toString());
            service.fetchBookedTimes(body).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Gson gson = new Gson();
                String json = gson.toJson(response.body());
                Log.e("json_BokingTime",json);
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        JSONObject resultObject = jsonObject.getJSONObject("result");
                        String status = resultObject.getString("status");
                        String message = resultObject.getString("response");
                        if (status.equals("success")) {

                            JSONObject data = jsonObject.getJSONObject("data");
                            Log.e("booked_times","2");
                            if(message.equals("Yacht Booking Times Received"))
                            {
                                JSONObject booked_times = data.getJSONObject("booked_times");
                                Log.e("booked_times","3");

                                JSONArray times = booked_times.getJSONArray("times");
                             //   JSONArray duration = booked_times.getJSONArray("duration");
                                JSONArray endTimes = booked_times.getJSONArray("end_time");



                                Log.e("booked_times_", booked_times.toString());
                                //  Log.e("allHours",);
                                if (times.length() > 0) {
                                    for (int i = 0; i < allHours.size(); i++) {

                                        String time = allHours.get(i).getTimeText();
                                        Log.e("time_test", time);
                                        for (int j = 0; j < times.length(); j++) {

                                            if (time.equals(times.getString(j))) {
                                                int duration=getDuration(times.getString(j),endTimes.getString(j));

                                                for (int k = i; (k < i + duration) && (k < allHours.size()); k++) {
                                                    allHours.get(k).setIsAlreadyBooked("true");
                                                }
                                                //   allHours.get(i).setIsAlreadyBooked("true");
                                                Log.e("allHours " + i, allHours.get(i).getTimeText());
                                            }
                                        }
                                        // break;
                                    }
                                }
                            }

                            mAdapter.notifyDataSetChanged();
                            progressDialog.dismiss();



                        } else {

                            progressDialog.dismiss();
                           // mAdapter.notifyDataSetChanged();
                            Toast.makeText(BookingTimeActivity.this, message, Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {

                        progressDialog.dismiss();
                        Toast.makeText(BookingTimeActivity.this, "Error", Toast.LENGTH_LONG).show();

                        e.printStackTrace();
                    }
                } else
                    {

                    progressDialog.dismiss();

                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(BookingTimeActivity.this, jObjError.getJSONObject("result").getString("response") , Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(BookingTimeActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }

    public int getDuration(String start , String end)
    {
        String time1_24=convert12HoursFormatTo24Hours(start);
        String time2_24=convert12HoursFormatTo24Hours(end);

        String str_h1= time1_24.substring(0,2);
        String str_h2=time2_24.substring(0,2);

        int start_h=Integer.parseInt(str_h1);
        int end_h=Integer.parseInt(str_h2);

        int duration=0;

        if( start_h < end_h)
        {
            duration=end_h-start_h;
        }
        else {
            duration=(24-start_h)+end_h;
        }
        return duration;

    }
    private void initUI() {
        static_etStartTime = etStartTime;
      //  static_tvDuration = tvDuration;
        initializeAllHours();



        etDate.setText(bookYachtModel.getStartDate());

    }


    private void checkTimeAvailability() {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgressNumberFormat(null);
        progressDialog.setProgressPercentFormat(null);
        progressDialog.show();


        //

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WebServices service = retrofit.create(WebServices.class);

        HashMap<String, String> body = new HashMap<>();

        body.put("yacht_id", AppConstants.yachtsModel.getId());
        Log.e("test", "yacht_id " + AppConstants.yachtsModel.getId());

      //  body.put("api_secret", AppController.getInstance().getPrefManger().getUserProfile().getAuthKey());
       // Log.e("test", "api_secret " + AppController.getInstance().getPrefManger().getUserProfile().getAuthKey());

        body.put("booking_date", AppConstants.bookYachtModel.getStartDate());
        Log.e("test", "booking_date " + AppConstants.bookYachtModel.getStartDate());

        body.put("booking_time",convert12HoursFormatTo24Hours(AppConstants.bookYachtModel.getStartTime()));
        Log.e("test", "booking_time " + convert12HoursFormatTo24Hours(AppConstants.bookYachtModel.getStartTime()));

        body.put("duration", AppConstants.bookYachtModel.getDuration());
        // Log.e("test","booking_date "+AppConstants.bookYachtModel.getStartDate());


        service.checkTimeAvailability(body).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                Gson gson = new Gson();
                String json = gson.toJson(response.body());
                Log.e("json_payment", json);

                progressDialog.dismiss();
                if (response.isSuccessful())
                {
                    try {

                        JSONObject jsonObject = new JSONObject(json);

                        JSONObject resultObject = jsonObject.getJSONObject("result");

                        String status = resultObject.getString("status");

                        String message = resultObject.getString("response");

                        if (status.equals("success") || status.equals("error")) {

                            if(message.equals("Yacht is available for this time"))
                            {
                                isAvailable=true;
                            }
                            else {
                                isAvailable=false;
                            }

                            if (isAvailable) {
                                // booking proceed
                                Intent i = new Intent(BookingTimeActivity.this, ConfirmBookingDetailsActivity.class);
                                startActivity(i);
                                finish();

                            }
                            else {
                                // show dialog , booking not available for this date and time
                                MaterialDialog dialog=new MaterialDialog.Builder(activity)
                                        .title("Sorry")
                                        .content("Booking not available for this date and time , please select other day or time !")
                                        .positiveText("OK")
                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                dialog.dismiss();
                                            }
                                        })
                                        .show();
                            }


                        }
                        else
                        {
                            MaterialDialog dialog=new MaterialDialog.Builder(activity)
                                    .title("Sorry")
                                    .content("Booking not available for this date and time , please select other day or time !")
                                    .positiveText("OK")
                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .show();

                          //  Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {

                        Log.e("ErrorMessage", e.getMessage());
//                        Toast.makeText(activity, "Error new ", Toast.LENGTH_LONG).show();

                        e.printStackTrace();
                    }
                } else {

                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(activity, jObjError.getJSONObject("result").getString("response") , Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(activity,t.toString(),Toast.LENGTH_LONG).show();
            }


        });
        //

    }

    public String addOneDay(String date)
{
    String dt = date;  // Start date
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Calendar c = Calendar.getInstance();
    try {
        c.setTime(sdf.parse(dt));
    } catch (ParseException e) {
        e.printStackTrace();
    }
    c.add(Calendar.DATE, 1);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
    String output = sdf1.format(c.getTime());
    return output;
}
    public String subtractOneDay(String date)
    {
        String dt = date;  // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dt));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, -1);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        String output = sdf1.format(c.getTime());
        return output;
    }
    private void initializeAllHours() {
        allHours.clear();

        allHours.add(new BookingHoursModel("false", "false", "12:00 AM"));
        allHours.add(new BookingHoursModel("false", "false", "01:00 AM"));
        allHours.add(new BookingHoursModel("false", "false", "02:00 AM"));
        allHours.add(new BookingHoursModel("false", "false", "03:00 AM"));
        allHours.add(new BookingHoursModel("false", "false", "04:00 AM"));
        allHours.add(new BookingHoursModel("false", "false", "05:00 AM"));

        allHours.add(new BookingHoursModel("false", "false", "06:00 AM"));
        allHours.add(new BookingHoursModel("false", "false", "07:00 AM"));
        allHours.add(new BookingHoursModel("false", "false", "08:00 AM"));
        allHours.add(new BookingHoursModel("false", "false", "09:00 AM"));
        allHours.add(new BookingHoursModel("false", "false", "10:00 AM"));
        allHours.add(new BookingHoursModel("false", "false", "11:00 AM"));

        allHours.add(new BookingHoursModel("false", "false", "12:00 PM"));
        allHours.add(new BookingHoursModel("false", "false", "01:00 PM"));
        allHours.add(new BookingHoursModel("false", "false", "02:00 PM"));
        allHours.add(new BookingHoursModel("false", "false", "03:00 PM"));
        allHours.add(new BookingHoursModel("false", "false", "04:00 PM"));
        allHours.add(new BookingHoursModel("false", "false", "05:00 PM"));

        allHours.add(new BookingHoursModel("false", "false", "06:00 PM"));
        allHours.add(new BookingHoursModel("false", "false", "07:00 PM"));
        allHours.add(new BookingHoursModel("false", "false", "08:00 PM"));
        allHours.add(new BookingHoursModel("false", "false", "09:00 PM"));
        allHours.add(new BookingHoursModel("false", "false", "10:00 PM"));
        allHours.add(new BookingHoursModel("false", "false", "11:00 PM"));

        mAdapter = new AllHoursGVadapter(this, allHours);

        gvAllHours.setAdapter(mAdapter);
    }

    @OnClick({R.id.tvConfirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvConfirm:
                if (etStartTime.getText().toString().equals("")) {
                    Toast.makeText(this, "Set your trip start time and end time", Toast.LENGTH_SHORT).show();
                } else if (durationStr.equals("")) {
                    Toast.makeText(this, "Set your trip duration", Toast.LENGTH_SHORT).show();
                } else {
                    checkTimeAvailability();


                }
        }
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
    private String convert24HoursFormatTo12Hours(String time24) {
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
    public String getDateInWords(String date)
    {
        String month=date.substring(5,7);
        String day=date.substring(8,10);
        String[] monthArray=new String[]{"January" , "Feburary","March" , "April", "May" , "June" , "July" , "August" , "September" , "October" ,"NOvember" , "December"};

        String newDate=day +" "+ monthArray[Integer.parseInt(month)-1];
        return newDate;
    }
}