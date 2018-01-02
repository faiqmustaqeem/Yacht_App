package com.digitalexperts.bookyachts.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.maps.model.Dash;
import com.google.gson.Gson;
import com.digitalexperts.bookyachts.R;
import com.digitalexperts.bookyachts.ResideMenu.ResideMenu;
import com.digitalexperts.bookyachts.ResideMenu.ResideMenuItem;
import com.digitalexperts.bookyachts.adapter.ShipsAdapter;
import com.digitalexperts.bookyachts.customClasses.AppConstants;
import com.digitalexperts.bookyachts.customClasses.AppController;
import com.digitalexperts.bookyachts.dialogs.CallBackDialog;
import com.digitalexperts.bookyachts.dialogs.ContactUsDialog;
import com.digitalexperts.bookyachts.interfaces.WebServices;
import com.digitalexperts.bookyachts.models.YachtsModel;

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

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {
    Activity activity;
    ArrayList<YachtsModel> yachtsModelsArrayList = new ArrayList<>();
    ArrayList<YachtsModel> searchYachtsModelsArrayList = new ArrayList<>();
    ShipsAdapter adapter;

    @BindView(R.id.ivNavigationIcon)
    ImageView ivNavigationIcon;
    @BindView(R.id.ivSearchYacht)
    ImageView ivSearchYacht;
    @BindView(R.id.txtSearch)
    EditText txtSearch;
    @BindView(R.id.liSearch)
    LinearLayout liSearch;
    @BindView(R.id.rvAllYachts)
    ShimmerRecyclerView rvAllYachts;
    @BindView(R.id.spin_kit)
    SpinKitView spinKit;
    @BindView(R.id.ivCallBack)
    ImageView ivCallBack;
    @BindView(R.id.ivContactUs)
    ImageView ivContactUs;
    @BindView(R.id.ll_dashboard)
    LinearLayout ll_dashboard;

    private ResideMenu resideMenu;
    private ResideMenuItem itemBookings;
    private ResideMenuItem itemUpdateProfile;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    private String navigationSelectedOption = "Home";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Hide the status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        ButterKnife.bind(this);

        // hiding action bar
        getSupportActionBar().hide();

        activity = this;
        fragmentManager = getSupportFragmentManager();

        setUpMenu();
        RecyclerView.LayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(activity);
        rvAllYachts.setLayoutManager(layoutManager);

        ivNavigationIcon.setClickable(false);
        ivSearchYacht.setClickable(false);

        getAllYachts();
        search();
    }

    private void getAllYachts() {
        yachtsModelsArrayList.clear();

        if (AppConstants.isOnline(activity)) {
            spinKit.setVisibility(View.VISIBLE);
            Log.e("test", "1");
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(AppConstants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Log.e("test", "2");
            WebServices service = retrofit.create(WebServices.class);
            Log.e("test", "3");
            HashMap<String, String> body = new HashMap<>();


            Log.e("test", "4");
            service.getAllYachts(body).enqueue(new Callback<Object>() {

                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    Log.e("test", "5");
                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    Log.e("test", "6");
                    Log.e("test_json_dashboard", json.toString());

                    if (response.isSuccessful()) {
                        try {
                            Log.e("test", "7");
                            JSONObject jsonObject = new JSONObject(json);

                            JSONObject resultObject = jsonObject.getJSONObject("result");

                            String status = resultObject.getString("status");

                            String message = resultObject.getString("response");

                            if (status.equals("success")) {

                                JSONObject dataObject = jsonObject.getJSONObject("data");

                                for (int i = 0; i < dataObject.getJSONArray("yachts").length(); i++) {

                                    YachtsModel yachtsModel = new YachtsModel();

                                    JSONObject yachtObject = dataObject.getJSONArray("yachts").getJSONObject(i);

                                    yachtsModel.setId(yachtObject.getString("id"));
                                    yachtsModel.setLatitude(yachtObject.getString("latitude"));
                                    yachtsModel.setLocation(yachtObject.getString("location"));
                                    yachtsModel.setLongitude(yachtObject.getString("longitude"));
                                    yachtsModel.setCapacity(yachtObject.getString("capacity"));
                                    yachtsModel.setMax_guest(yachtObject.getString("max_guest"));
                                    yachtsModel.setCurrency(yachtObject.getString("currency"));
                                    yachtsModel.setSize(yachtObject.getString("size"));
                                    yachtsModel.setTripDuration(yachtObject.getString("trip_duration"));
                                    yachtsModel.setPrice(yachtObject.getString("price"));
                                    yachtsModel.setTitle(yachtObject.getString("title"));
                                    yachtsModel.setPicturePath(yachtObject.getString("picture_path"));
                                    yachtsModel.setDiscountedPrice(yachtObject.getString("discounted_price"));
                                    yachtsModel.setFacilities(yachtObject.getString("inclusive_facilities"));

                                    yachtsModelsArrayList.add(yachtsModel);

                                    ivNavigationIcon.setClickable(true);
                                    ivSearchYacht.setClickable(true);

                                }

                                adapter = new ShipsAdapter(activity, yachtsModelsArrayList);
                                rvAllYachts.setAdapter(adapter);


                                spinKit.setVisibility(View.GONE);

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
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            Toast.makeText(DashboardActivity.this, jObjError.getJSONObject("result").getString("response") , Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(DashboardActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    spinKit.setVisibility(View.GONE);
                    Toast.makeText(activity, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {

        }
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
                /*
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            ll_dashboard.setBackgroundDrawable( getResources().getDrawable(R.drawable.splash_bg_two) );
        } else {
            ll_dashboard.setBackground( getResources().getDrawable(R.drawable.splash_bg_two));
        }*/
            }
        });

        findViewById(R.id.ivSearchYacht).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (liSearch.getVisibility() == View.VISIBLE) {
                    liSearch.setVisibility(View.GONE);
                } else if (liSearch.getVisibility() == View.GONE) {
                    liSearch.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void search() {
        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchYachtsModelsArrayList.clear();

                for (int i = 0; i < yachtsModelsArrayList.size(); i++) {
                    //String key = taskModelLArrayList.keySet().toArray()[i] + "";

                    if (yachtsModelsArrayList.get(i).getTitle().toLowerCase().contains(s.toString().toLowerCase())) {
                        searchYachtsModelsArrayList.add(yachtsModelsArrayList.get(i));

                    }
                }
                rvAllYachts.removeAllViewsInLayout();
                adapter = new ShipsAdapter(activity, searchYachtsModelsArrayList);
                rvAllYachts.setAdapter(adapter);
            }
        });
    }

    @OnClick({R.id.ivNavigationIcon, R.id.ivSearchYacht, R.id.ivCallBack, R.id.ivContactUs, R.id.ivHotDeals})
    public void onClick(View view) {
        if (view == itemBookings) {

            // changeFragment(new SettingsFragment());
            if (AppController.getInstance().getPrefManger().getUserProfile() != null) {
                //switchFragmentWithBackStack(new BookingHistoryFragment());

                resideMenu.closeMenu();
                startActivity(new Intent(activity, BookingHistoryActivity.class));

            } else {
                Toast.makeText(this, "Log in To View Booking History", Toast.LENGTH_SHORT).show();
                resideMenu.closeMenu();
                startActivity(new Intent(activity, LoginActivity.class).putExtra("goToActivity", "back"));
            }
        } else if (view == itemUpdateProfile) {
            // changeFragment(new ProfileUpdate());
            if (AppController.getInstance().getPrefManger().getUserProfile() != null) {
                resideMenu.closeMenu();
                startActivity(new Intent(activity, UserProfileActivity.class));
            } else {
                Toast.makeText(this, "Log in To View Profile", Toast.LENGTH_SHORT).show();
                resideMenu.closeMenu();
                startActivity(new Intent(activity, LoginActivity.class).putExtra("goToActivity", "back"));
            }
        }

        switch (view.getId()) {
            case R.id.ivCallBack:
                new CallBackDialog(activity);
                break;
            case R.id.ivContactUs:

            new ContactUsDialog(activity);

                break;
            case R.id.ivHotDeals:
                Intent i = new Intent(DashboardActivity.this, HotDealsActivity.class);
                startActivity(i);
                break;
        }
    }
}
