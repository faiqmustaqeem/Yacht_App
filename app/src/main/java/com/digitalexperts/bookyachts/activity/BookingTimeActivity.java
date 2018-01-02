package com.digitalexperts.bookyachts.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import static com.digitalexperts.bookyachts.customClasses.AppConstants.bookingDate;
import static com.digitalexperts.bookyachts.customClasses.AppConstants.bookingDuration;

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



    public static EditText static_etStartTime;

    @BindView(R.id.tvDuration)
    EditText tvDuration;
    public static EditText static_tvDuration;

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

    List<BookingHoursModel> allHours;
    RVHoursAdapter hoursAdapter;
    AllHoursGVadapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_time);
        ButterKnife.bind(this);

       // tvSelectYacht.setText();
        tvSelectYacht.setText(AppConstants.yachtsModel.getTitle().substring(0, 2) + "ft");

           tvOldPrice.setText(AppConstants.yachtsModel.getPrice());

        tvNewPrice.setText(AppConstants.yachtsModel.getDiscountedPrice());
        AppConstants.yachtsModel.setPrice(AppConstants.yachtsModel.getDiscountedPrice());

        initUI();

        static_gvAllHours = gvAllHours;

        fetchBookedSlots();
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
                                JSONArray duration = booked_times.getJSONArray("duration");



                                Log.e("booked_times_", booked_times.toString());
                                //  Log.e("allHours",);
                                if (times.length() > 0) {
                                    for (int i = 0; i < allHours.size(); i++) {

                                        String time = allHours.get(i).getTimeText();
                                        Log.e("time_test", time);
                                        for (int j = 0; j < times.length(); j++) {
                                            if (time.equals(times.getString(j))) {
                                                for (int k = i; k < i + Integer.parseInt(duration.getString(j)); k++) {
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

    private void initUI() {
        static_etStartTime = etStartTime;
        static_tvDuration = tvDuration;
        initializeAllHours();

/*        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvTimings1.setLayoutManager(layoutManager1);
        hoursAdapter = new RVHoursAdapter(allHours.subList(0, 6), BookingTimeActivity.this);
        rvTimings1.setAdapter(hoursAdapter);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        layoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvTimings2.setLayoutManager(layoutManager2);
        hoursAdapter = new RVHoursAdapter(allHours.subList(6, 12), BookingTimeActivity.this);
        rvTimings2.setAdapter(hoursAdapter);

        LinearLayoutManager layoutManager3 = new LinearLayoutManager(this);
        layoutManager3.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvTimings3.setLayoutManager(layoutManager3);
        hoursAdapter = new RVHoursAdapter(allHours.subList(12, 18), BookingTimeActivity.this);
        rvTimings3.setAdapter(hoursAdapter);

        LinearLayoutManager layoutManager4 = new LinearLayoutManager(this);
        layoutManager4.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvTimings4.setLayoutManager(layoutManager4);
        hoursAdapter = new RVHoursAdapter(allHours.subList(18, 24), BookingTimeActivity.this);
        rvTimings4.setAdapter(hoursAdapter);*/

//        tvNewPrice.setText(AppConstants.discountPercentage+"");



        etDate.setText(bookingDate);
        mAdapter = new AllHoursGVadapter(this, allHours);

        gvAllHours.setAdapter(mAdapter);

        gvAllHours.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*Toast.makeText(BookingTimeActivity.this, ""+allHours.get(0).getTimeText(), Toast.LENGTH_SHORT).show();
                allHours.remove(0);
                mAdapter.notifyDataSetChanged();*/
            }
        });
    }

    private void initializeAllHours() {
        allHours = new ArrayList<BookingHoursModel>();

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
    }

    @OnClick({R.id.tvConfirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvConfirm:
                if (etStartTime.getText().toString().equals("")) {
                    Toast.makeText(this, "Set your trip start time and end time", Toast.LENGTH_SHORT).show();
                } else if (tvDuration.getText().toString().equals("")) {
                    Toast.makeText(this, "Set your trip end time", Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(BookingTimeActivity.this, ConfirmBookingDetailsActivity.class);
                    startActivity(i);
                    finish();
                    break;
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
}