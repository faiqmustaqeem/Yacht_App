package com.digitalexperts.bookyachts.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.gson.Gson;
import com.digitalexperts.bookyachts.R;
import com.digitalexperts.bookyachts.adapter.BookingShipsAdapter;
import com.digitalexperts.bookyachts.customClasses.AppConstants;
import com.digitalexperts.bookyachts.customClasses.AppController;
import com.digitalexperts.bookyachts.interfaces.WebServices;
import com.digitalexperts.bookyachts.models.BookingHistoryModel;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BookingHistoryActivity extends AppCompatActivity
{

    @BindView(R.id.rvAllBookings)
    ShimmerRecyclerView rvAllBookings;
    @BindView(R.id.spin_kit)
    SpinKitView spinKit;
    ArrayList<BookingHistoryModel>  bookingHistoryModelsArrayList = new ArrayList<>();
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Hide the status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history);
        ButterKnife.bind(this);

        getSupportActionBar().setTitle("     Booking History");


        activity = this;
        getBookingHistory();
    }


    private void getBookingHistory()
    {
        if (AppConstants.isOnline(activity))
        {
            spinKit.setVisibility(View.VISIBLE);
            AppConstants.yachtsHotSlotsArrayList.clear();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(AppConstants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            WebServices service = retrofit.create(WebServices.class);

            HashMap<String, String> body = new HashMap<>();
            body.put("api_secret", AppController.getInstance().getPrefManger().getUserProfile().getAuthKey());


            service.bookingHistory(body).enqueue(new Callback<Object>()
            {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response)
                {

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());

                     Log.e("test",json);

                    if (response.isSuccessful())
                    {
                        try {

                            JSONObject jsonObject = new JSONObject(json);

                            JSONObject resultObject = jsonObject.getJSONObject("result");

                            String status = resultObject.getString("status");

                            String message = resultObject.getString("response");

                            if (status.equals("success"))
                            {

                                JSONObject dataObject = jsonObject.getJSONObject("data");

                                for (int i = 0; i < dataObject.getJSONArray("booking").length(); i++)
                                {

                                    JSONObject bookingObject = dataObject.getJSONArray("booking").getJSONObject(i);

                                    BookingHistoryModel bookingHistoryModel = new BookingHistoryModel();
                                 //   bookingHistoryModel.setDiscount(bookingObject.getString("discount"));
                                    bookingHistoryModel.setBookingDate(bookingObject.getString("booking_date"));
                                    bookingHistoryModel.setBookingTime(convert24HoursFormatTo12Hours(bookingObject.getString("booking_time")));
                                   // bookingHistoryModel.setBookingStatus(bookingObject.getString("booking_status"));
                                    bookingHistoryModel.setDuration(bookingObject.getString("duration"));
                                    //bookingHistoryModel.setFacilities(bookingObject.getString("facilities"));
                                    //bookingHistoryModel.setSubTotal(bookingObject.getString("sub_total"));
                                    bookingHistoryModel.setTotalAmount(bookingObject.getString("total_amount"));
                                //    bookingHistoryModel.setYachtId(bookingObject.getString("yacht_id"));
                                //    bookingHistoryModel.setPaymentStatus(bookingObject.getString("payment_status"));
                                 //   bookingHistoryModel.setPaymentType(bookingObject.getString("payment_type"));
                                    bookingHistoryModel.setAmountPaid(bookingObject.getString("amount_paying_now"));
                                    bookingHistoryModel.setAmountRemaining(bookingObject.getString("amount_remaining"));
                                    bookingHistoryModel.setYachtTitle(bookingObject.getString("title"));
                                    bookingHistoryModelsArrayList.add(bookingHistoryModel);
                                }


                                spinKit.setVisibility(View.GONE);


                                if(dataObject.getJSONArray("booking").length() > 0) {
                                    RecyclerView.LayoutManager layoutManager;
                                    layoutManager = new LinearLayoutManager(activity);
                                    rvAllBookings.setLayoutManager(layoutManager);
                                    BookingShipsAdapter adapter = new BookingShipsAdapter(activity, bookingHistoryModelsArrayList);
                                    rvAllBookings.setAdapter(adapter);
                                }
                                else
                                {
                                    Toast.makeText(activity , "No History Found ",Toast.LENGTH_LONG).show();
                                }

                            } else {
                                spinKit.setVisibility(View.GONE);
                                Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            spinKit.setVisibility(View.GONE);
                            Toast.makeText(activity, "Error", Toast.LENGTH_LONG).show();

                            e.printStackTrace();
                        }
                    } else {
                        spinKit.setVisibility(View.GONE);
                        Toast.makeText(activity, "Internet Error", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {

                }
            });
        } else {

        }

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

