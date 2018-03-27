package com.digitalexperts.bookyachts.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.digitalexperts.bookyachts.ResideMenu.ResideMenu;
import com.digitalexperts.bookyachts.ResideMenu.ResideMenuItem;
import com.digitalexperts.bookyachts.dialogs.CallBackDialog;
import com.digitalexperts.bookyachts.dialogs.ContactUsDialog;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.gson.Gson;
import com.digitalexperts.bookyachts.R;
import com.digitalexperts.bookyachts.adapter.HotDealsRVAdapter;
import com.digitalexperts.bookyachts.customClasses.AppConstants;
import com.digitalexperts.bookyachts.customClasses.AppController;
import com.digitalexperts.bookyachts.interfaces.WebServices;
import com.digitalexperts.bookyachts.models.HotDealsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HotDealsActivity extends AppCompatActivity implements View.OnClickListener {


    @BindView(R.id.tvNoHotDeals)
    TextView tvNoHotDeals;
    @BindView(R.id.rvHotDeals)
    RecyclerView rvHotDeals;
    @BindView(R.id.spin_kit)
    SpinKitView spinKit;
    ArrayList<HotDealsModel> hotDealsList = new ArrayList<HotDealsModel>();
    HotDealsRVAdapter hotDealsRVAdapter;

    private ResideMenu resideMenu;
    private ResideMenuItem itemBookings;
    private ResideMenuItem itemUpdateProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_deals);
        ButterKnife.bind(this);
        getSupportActionBar().hide();

        RecyclerView.LayoutManager mAnnouncementLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvHotDeals.setLayoutManager(mAnnouncementLayoutManager);
        rvHotDeals.setItemAnimator(new DefaultItemAnimator());

        setUpMenu();

        fetchHotDeals();
    }

    private void setUpMenu() {
        // attach to current activity;
        resideMenu = new ResideMenu(this);
        //resideMenu.setUse3D(true);
        resideMenu.setBackground(R.drawable.splash_bg);
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

    private void fetchHotDeals() {
        spinKit.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WebServices service = retrofit.create(WebServices.class);

        HashMap<String, String> body = new HashMap<>();
        /*
        body.put("booking_date", AppConstants.bookYachtModel.getStartTime());
        body.put("yacht_id", AppController.getInstance().getPrefManger().getUserProfile().getAuthKey());
        body.put("api_secret", AppConstants.yachtsModel.getId());
        */
        service.getHotDeals(body).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Gson gson = new Gson();
                String json = gson.toJson(response.body());
                if (!json.equals("") && json != null && !json.isEmpty()) {
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        JSONObject resultObject = jsonObject.getJSONObject("result");
                        String status = resultObject.getString("status");
                        String message = resultObject.getString("response");

                        if (status.equals("success")) {
                            JSONArray data = jsonObject.getJSONArray("data");
                            if (data.length() > 0) {
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject hotDeal = data.getJSONObject(i);
                                    hotDealsList.add(new HotDealsModel(hotDeal.getString("id"), hotDeal.getString("title"), hotDeal.getString("description")));
                                }
                            } else {
                                tvNoHotDeals.setVisibility(View.VISIBLE);
                            }
                            hotDealsRVAdapter = new HotDealsRVAdapter(HotDealsActivity.this, hotDealsList);
                            rvHotDeals.setAdapter(hotDealsRVAdapter);
                            spinKit.setVisibility(View.GONE);

                            if (hotDealsList == null | hotDealsList.size() == 0) {
                                tvNoHotDeals.setVisibility(View.VISIBLE);
                            }

                        } else {
                            spinKit.setVisibility(View.GONE);
                            Toast.makeText(HotDealsActivity.this, message, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        spinKit.setVisibility(View.GONE);
                        Toast.makeText(HotDealsActivity.this, "Error", Toast.LENGTH_LONG).show();

                        e.printStackTrace();
                    }
                } else {
                    spinKit.setVisibility(View.GONE);
                    Toast.makeText(HotDealsActivity.this, "Internet Error", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }


    @OnClick({R.id.ivNavigationIcon, R.id.ivHome , R.id.ivCallBack, R.id.ivContactUs, R.id.ivHotDeals})
    public void onClick(View view) {
        if (view == itemBookings) {

            // changeFragment(new SettingsFragment());
            if (AppController.getInstance().getPrefManger().getUserProfile() != null) {
                //switchFragmentWithBackStack(new BookingHistoryFragment());

                resideMenu.closeMenu();
                startActivity(new Intent(HotDealsActivity.this, BookingHistoryActivity.class));

            } else {
                Toast.makeText(this, "Log in To View Booking History", Toast.LENGTH_SHORT).show();
                resideMenu.closeMenu();
                startActivity(new Intent(HotDealsActivity.this, LoginActivity.class).putExtra("goToActivity", "back"));
            }
        } else if (view == itemUpdateProfile) {
            // changeFragment(new ProfileUpdate());
            if (AppController.getInstance().getPrefManger().getUserProfile() != null) {
                resideMenu.closeMenu();
                startActivity(new Intent(HotDealsActivity.this, UserProfileActivity.class));
            } else {
                Toast.makeText(this, "Log in To View Profile", Toast.LENGTH_SHORT).show();
                resideMenu.closeMenu();
                startActivity(new Intent(HotDealsActivity.this, LoginActivity.class).putExtra("goToActivity", "back"));
            }
        }

        switch (view.getId()) {
            case R.id.ivCallBack:
                new CallBackDialog(HotDealsActivity.this);
                break;
            case R.id.ivContactUs:
                new ContactUsDialog(HotDealsActivity.this);
                break;
            case R.id.ivHome:
                finish();
                break;
        }
    }
}
