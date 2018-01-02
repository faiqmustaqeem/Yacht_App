package com.digitalexperts.bookyachts.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.digitalexperts.bookyachts.MapsActivity;
import com.digitalexperts.bookyachts.R;
import com.digitalexperts.bookyachts.RecyclerTouchListener;
import com.digitalexperts.bookyachts.ResideMenu.ResideMenu;
import com.digitalexperts.bookyachts.ResideMenu.ResideMenuItem;
import com.digitalexperts.bookyachts.adapter.CustomPagerAdapter;
import com.digitalexperts.bookyachts.adapter.YachtImagesAdapter;
import com.digitalexperts.bookyachts.customClasses.AppConstants;
import com.digitalexperts.bookyachts.customClasses.AppController;
import com.digitalexperts.bookyachts.dialogs.CallBackDialog;
import com.digitalexperts.bookyachts.dialogs.ContactUsDialog;
import com.digitalexperts.bookyachts.interfaces.WebServices;
import com.digitalexperts.bookyachts.models.BookYachtModel;
import com.digitalexperts.bookyachts.models.HotSlotModel;
import com.digitalexperts.bookyachts.models.SubFacilityModel;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.digitalexperts.bookyachts.adapter.ShipsAdapter.selectedPosition;
import static com.digitalexperts.bookyachts.adapter.ShipsAdapter.yachtsModelsArrayList;
import static com.digitalexperts.bookyachts.customClasses.AppConstants.yachtsHotSlotsArrayList;
import static com.digitalexperts.bookyachts.customClasses.AppConstants.yachtsModel;

public class ShipDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    public static Activity activity;

    // this arraylist stores yacht slider images come from server
    ArrayList<String> yachtSliderArrayList = new ArrayList();

    // Initializing Required view  by Butter Knife
    @BindView(R.id.tvBookNow1)
    TextView tvBookNow1;
    @BindView(R.id.spin_kit)
    SpinKitView spinKit;
    @BindView(R.id.ivYachtImage)
    ImageView ivYachtImage;
    @BindView(R.id.ivYachtMainImage)
    ImageView ivYachtMainImage;
    @BindView(R.id.ivYachtVideo)
    ImageView ivYachtVideo;
    @BindView(R.id.ivYachtLocation)
    ImageView ivYachtLocation;
    @BindView(R.id.tvYachtTitle)
    TextView tvYachtTitle;
    @BindView(R.id.tvYachtGuest)
    TextView tvYachtGuest;
    @BindView(R.id.tvGuestNumber)
    TextView tvGuestNumber;
    @BindView(R.id.tvYachtDuration)
    TextView tvYachtDuration;

    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.activity_sip_details)
    LinearLayout activitySipDetails;
    @BindView(R.id.slider)
    ViewPager slider;

    @BindView(R.id.indicator)
    CircleIndicator indicator;
    @BindView(R.id.rvYachtImages)
    RecyclerView rvYachtImages;


    @BindView(R.id.ivPreviousYacht)
    ImageView ivPreviousYacht;
    @BindView(R.id.ivNextYacht)
    ImageView ivNextYacht;

    @BindView(R.id.ivPreviousPictureYacht)
    ImageView ivPreviousPictureYacht;
    @BindView(R.id.ivNextPictureYacht)
    ImageView ivNextPictureYacht;
    @BindView(R.id.tvfacilities)
    TextView tvfacilities;


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
    private ResideMenu resideMenu;
    private ResideMenuItem itemBookings;
    private ResideMenuItem itemUpdateProfile;


    YachtImagesAdapter imagesAdapter;

    private String shipVideoUrl = "";

    CustomPagerAdapter mCustomPagerAdapter;

    // --> Start onCreate() method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Hide the status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sip_details);
        ButterKnife.bind(this);
        initUI();

        setUpMenu();

        // hiding action bar
        getSupportActionBar().hide();

        AppConstants.selectedFacilities = new ArrayList<>();

        // setting total to zero
        AppConstants.subFacilityTotal = 0.0f;

        AppConstants.yachtsHotSlotsArrayList = new ArrayList();
        AppConstants.bookYachtModel = new BookYachtModel();
        AppConstants.listDataChild = new HashMap<>();

        AppConstants.inclusiveFacilities = "";
        AppConstants.exclusiveFacilities = "";
        AppConstants.discountAmount = 0.0f;
        AppConstants.totalAmount = 0.0f;
        AppConstants.indexOfHotSlot = -1;
        AppConstants.discountPercentage = 0;

        activity = this;
Log.e("priceD",AppConstants.yachtsModel.getDiscountedPrice());
        Log.e("priceUn",AppConstants.yachtsModel.getPrice());

        Picasso.with(activity)
                .setIndicatorsEnabled(true);
        Picasso.with(activity)
                .load("https://bookyachts.ae/googleYacht/uploads/images/" + yachtsModel.getPicturePath())
                .placeholder(R.drawable.loader)
                .into(ivYachtImage);
        Picasso.with(activity)
                .load("https://bookyachts.ae/googleYacht/uploads/images/" + yachtsModel.getPicturePath())
                .placeholder(R.drawable.demo_one)
                .into(ivYachtMainImage);

        // Getting all yachts from server
        getYachtDetails();

        tvYachtTitle.setText(AppConstants.yachtsModel.getTitle().substring(0, 2) + "ft");
//        tvYachtGuest.setText("Max Guest : " + AppConstants.yachtsModel.getMax_guest() + " Guest            Capacity : " + yachtsModel.getMax_guest() + " Guest +" + yachtsModel.getCapacity() + " Captain");
        tvYachtGuest.setText(AppConstants.yachtsModel.getMax_guest());
        tvGuestNumber.setText(AppConstants.yachtsModel.getMax_guest());
        tvYachtDuration.setText("Minimum " + AppConstants.yachtsModel.getTripDuration() + " Hours");
        tvPrice.setText(AppConstants.yachtsModel.getCurrency() + " " + AppConstants.yachtsModel.getPrice() + "/Hour");
        tvfacilities.setText(AppConstants.yachtsModel.getFacilities());
        mCustomPagerAdapter = new CustomPagerAdapter(activity, yachtSliderArrayList);

        slider.setAdapter(mCustomPagerAdapter);
        indicator.setViewPager(slider);
        mCustomPagerAdapter.registerDataSetObserver(indicator.getDataSetObserver());
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

    private void initUI()
    {
        ivPreviousPictureYacht.bringToFront();
        ivNextPictureYacht.bringToFront();
        rvYachtImages = (RecyclerView) findViewById(R.id.rvYachtImages);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvYachtImages.setLayoutManager(layoutManager);

        rvYachtImages.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), rvYachtImages, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                slider.setCurrentItem(position);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }


//    @OnClick(R.id.tvBookNow)
//    public void onClick(View view) {
//
//        Log.e("booknowclicked","abook now clicked");
//      /*
//        if (AppController.getInstance().getPrefManger().getUserProfile() == null) {
//            startActivity(new Intent(activity, PaymentFormActivity.class).putExtra("goToActivity", "bookNow"));
//        } else {
//            startActivity(new Intent(activity, BookingActivity.class));
//        }
//        */
//
//
//        startActivity(new Intent(activity, BookingActivity.class));
//
//
//    }

    private void getYachtDetails() {
        //  tvBookNow1.setClickable(false);
        AppConstants.selectedFacilities.clear();
        if (AppConstants.isOnline(activity)) {
            spinKit.setVisibility(View.VISIBLE);
            yachtsHotSlotsArrayList.clear();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(AppConstants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            final WebServices service = retrofit.create(WebServices.class);

            HashMap<String, String> body = new HashMap<>();
            body.put("yacht_id", yachtsModel.getId());
            Log.e("yacht_id",yachtsModel.getId());
            //body.put("yacht_id", "1");

            service.yachtDetails(body).enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {

                    Log.e("test", "1");
                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    Log.e("test_json", json);
                    Log.e("test", "2");

                    if (response.isSuccessful()) {
                        try {

                            JSONObject jsonObject = new JSONObject(json);

                            JSONObject resultObject = jsonObject.getJSONObject("result");

                            String status = resultObject.getString("status");

                            String message = resultObject.getString("response");
                            Log.e("response",message);
                            if (status.equals("success")) {

                                JSONObject dataObject = jsonObject.getJSONObject("data");

                                JSONObject yachtObject = dataObject.getJSONObject("yacht");

                                for (int i = 0; i < yachtObject.getJSONArray("sliders").length(); i++) {

                                    if (yachtObject.getJSONArray("sliders").getJSONObject(i).getString("type").equals("img")) {
                                        yachtSliderArrayList.add(yachtObject.getJSONArray("sliders").getJSONObject(i).getString("path"));
                                       // Log.e("sliders",yachtObject.getJSONArray("sliders").getJSONObject(i).getString("path"));
                                    } else {
                                        Log.e("video",yachtObject.getJSONArray("sliders").getJSONObject(i).getString("path"));
                                        shipVideoUrl = "https://bookyachts.ae/googleYacht/uploads/images/" + yachtObject.getJSONArray("sliders").getJSONObject(i).getString("path");
                                    }
                                    if (i == yachtObject.getJSONArray("sliders").length() - 1) {

                                        mCustomPagerAdapter.notifyDataSetChanged();
                                    }

                                }

                                for (int i = 0; i < yachtObject.getJSONArray("hotslot").length(); i++) {

                                    JSONObject yachtHotSlotsObject = yachtObject.getJSONArray("hotslot").getJSONObject(i);

                                    HotSlotModel hotSlotModel = new HotSlotModel();
                                    hotSlotModel.setDiscount(yachtHotSlotsObject.getString("discount"));
                                    hotSlotModel.setStart_date(yachtHotSlotsObject.getString("start_date"));
                                    hotSlotModel.setStart_time(yachtHotSlotsObject.getString("start_time"));
                                    //hotSlotModel.setEnd_date(yachtHotSlotsObject.getString("end_date"));
                                    //hotSlotModel.setEnd_time(yachtHotSlotsObject.getString("end_time"));
                                    yachtsHotSlotsArrayList.add(hotSlotModel);
                                }

                                AppConstants.inclusiveFacilities = yachtObject.getString("inclusive_facilities");

                                if (AppConstants.inclusiveFacilities != null && AppConstants.inclusiveFacilities != "") {
                                    AppConstants.selectedFacilities.add(AppConstants.inclusiveFacilities);
                                }

                                listDataChild = new HashMap<String, ArrayList<SubFacilityModel>>();

                                ArrayList<SubFacilityModel> temp = new ArrayList<SubFacilityModel>();

                             if (yachtObject.getJSONArray("exclusive_facilities").length() != 0) {
                                 if (yachtObject.getJSONArray("exclusive_facilities").length() > 1) {
                                     for (int i = 0; i < yachtObject.getJSONArray("exclusive_facilities").length(); i++) {

                                         JSONObject faciltiesObject1 = yachtObject.getJSONArray("exclusive_facilities").getJSONObject(i);


                                         if (i < yachtObject.getJSONArray("exclusive_facilities").length() - 1) {

                                             JSONObject faciltiesObject2 = yachtObject.getJSONArray("exclusive_facilities").getJSONObject(i + 1);
                                             if (faciltiesObject1.getString("facility_id").equals(faciltiesObject2.getString("facility_id"))) {

                                                 SubFacilityModel subFacilityModel = new SubFacilityModel();
                                                 subFacilityModel.setName(faciltiesObject1.getString("name"));
                                                 subFacilityModel.setSubFacilityName(faciltiesObject1.getString("sub_facility_name"));
                                                 subFacilityModel.setSubFacilityPrice(faciltiesObject1.getString("sub_facility_price"));

                                                 temp.add(subFacilityModel);
                                             } else {

                                                 SubFacilityModel subFacilityModel = new SubFacilityModel();
                                                 subFacilityModel.setName(faciltiesObject1.getString("name"));
                                                 subFacilityModel.setSubFacilityName(faciltiesObject1.getString("sub_facility_name"));
                                                 subFacilityModel.setSubFacilityPrice(faciltiesObject1.getString("sub_facility_price"));

                                                 temp.add(subFacilityModel);
                                                 listDataChild.put(faciltiesObject1.getString("name"), temp);
                                                 temp = new ArrayList<SubFacilityModel>();

                                             }

                                         } else {
                                             Log.e("test", "here 1");
                                             JSONObject faciltiesObject2 = yachtObject.getJSONArray("exclusive_facilities").getJSONObject(i - 1);
                                             if (faciltiesObject1.getString("facility_id").equals(faciltiesObject2.getString("facility_id"))) {
                                                 SubFacilityModel subFacilityModel = new SubFacilityModel();
                                                 subFacilityModel.setName(faciltiesObject1.getString("name"));
                                                 subFacilityModel.setSubFacilityName(faciltiesObject1.getString("sub_facility_name"));
                                                 subFacilityModel.setSubFacilityPrice(faciltiesObject1.getString("sub_facility_price"));

                                                 temp.add(subFacilityModel);
                                                 listDataChild.put(faciltiesObject1.getString("name"), temp);
                                             } else {
                                                 Log.e("test", "here 2");
                                                 temp = new ArrayList<SubFacilityModel>();
                                                 SubFacilityModel subFacilityModel = new SubFacilityModel();
                                                 subFacilityModel.setName(faciltiesObject1.getString("name"));
                                                 subFacilityModel.setSubFacilityName(faciltiesObject1.getString("sub_facility_name"));
                                                 subFacilityModel.setSubFacilityPrice(faciltiesObject1.getString("sub_facility_price"));


                                                 temp.add(subFacilityModel);

                                                 listDataChild.put(faciltiesObject1.getString("name"), temp);
                                             }
                                         }
                                     }
                                 } else {
                                     Log.e("facilities", "pehle");
                                     JSONObject faciltiesObject1 = yachtObject.getJSONArray("exclusive_facilities").getJSONObject(0);
                                     Log.e("facilities", "baad");
                                     SubFacilityModel subFacilityModel = new SubFacilityModel();
                                     subFacilityModel.setName(faciltiesObject1.getString("name"));
                                     subFacilityModel.setSubFacilityName(faciltiesObject1.getString("sub_facility_name"));
                                     subFacilityModel.setSubFacilityPrice(faciltiesObject1.getString("sub_facility_price"));

                                     temp.add(subFacilityModel);
                                     listDataChild.put(faciltiesObject1.getString("name"), temp);
                                 }
                             }
                                AppConstants.listDataChild = listDataChild;
                                Log.e("test", AppConstants.listDataChild.size() + " AppConstants.listDataChild.size()");
                                Log.e("test", "2");
                                spinKit.setVisibility(View.GONE);
                                Log.e("test", "3");
                                if (yachtSliderArrayList.size() > 0) {
                                    Log.e("test", "3.3");
                                    slider.setVisibility(View.VISIBLE);
                                    Log.e("test", "3.4");
                                    ivYachtImage.setVisibility(View.GONE);
                                } else {
                                    Log.e("test", "4");
                                    ivYachtImage.setVisibility(View.VISIBLE);
                                    Log.e("test", "5");
                                    slider.setVisibility(View.GONE);
                                    Log.e("test", "6");
                                }
                                tvBookNow1.setClickable(true);

                            } else {
                                spinKit.setVisibility(View.GONE);
                                Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
                            }
                               // Log.e("agaya","agaya");
                            imagesAdapter = new YachtImagesAdapter(yachtSliderArrayList, ShipDetailsActivity.this);
                            rvYachtImages.setAdapter(imagesAdapter);

                        } catch (Exception e) {
                            spinKit.setVisibility(View.GONE);
                            Toast.makeText(activity, "Error", Toast.LENGTH_LONG).show();

                            e.printStackTrace();
                        }
                    } else {
                        spinKit.setVisibility(View.GONE);
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            Toast.makeText(ShipDetailsActivity.this, jObjError.getJSONObject("result").getString("response") , Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(ShipDetailsActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {

                }
            });
        } else {

        }
    }

    HashMap<String, ArrayList<SubFacilityModel>> listDataChild;

    @OnClick({R.id.ivYachtMainImage, R.id.ivPreviousYacht, R.id.ivNextYacht, /*bottom navigation*/ R.id.ivNavigationIcon, R.id.ivCallBack, R.id.ivContactUs, R.id.ivHotDeals, R.id.ivHome, R.id.ivNextPictureYacht, R.id.ivPreviousPictureYacht,R.id.tvBookNow1})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivYachtMainImage:
                break;

            case R.id.ivYachtVideo:
                if (!shipVideoUrl.equals("")) {
                    startActivity(new Intent(activity, ShowShipVideoActivity.class).putExtra("url", shipVideoUrl));
                } else {
                    Toast.makeText(activity, "No Video Available", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.ivYachtLocation:
                startActivity(new Intent(activity, MapsActivity.class));
                break;
            case R.id.tvHotSlots:
                if (AppConstants.yachtsHotSlotsArrayList.size() > 0) {
                    activity.startActivity(new Intent(activity, ShowHotSlotsActivity.class));
                }
                break;
            case R.id.ivPreviousYacht:
                try {
                    selectedPosition--;
                    AppConstants.yachtsModel = yachtsModelsArrayList.get(selectedPosition);
                    startActivity(new Intent(ShipDetailsActivity.this, ShipDetailsActivity.class));
                    ShipDetailsActivity.this.finish();
                } catch (IndexOutOfBoundsException ie) {
                    selectedPosition++;
                    Toast.makeText(activity, "Its the first Yacht", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ivNextYacht:
                try {
                    selectedPosition++;
                    AppConstants.yachtsModel = yachtsModelsArrayList.get(selectedPosition);
                    startActivity(new Intent(ShipDetailsActivity.this, ShipDetailsActivity.class));
                    ShipDetailsActivity.this.finish();
                } catch (IndexOutOfBoundsException ie) {
                    selectedPosition--;
                    Toast.makeText(activity, "No more Yachts", Toast.LENGTH_SHORT).show();
                }
                break;
/*            case R.id.tvCallback:

                new CallBackDialog(activity);
                break;*/


            //bottom navigation
            case R.id.ivNavigationIcon:
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
                break;
            case R.id.ivCallBack:
                new CallBackDialog(ShipDetailsActivity.this);
                break;
            case R.id.ivContactUs:
                new ContactUsDialog(ShipDetailsActivity.this);
                break;
            case R.id.ivHotDeals:
                Intent i = new Intent(ShipDetailsActivity.this, HotDealsActivity.class);
                startActivity(i);
                break;
            case R.id.ivHome:
                finish();
                break;
            case R.id.ivNextPictureYacht:
                try {
                    slider.setCurrentItem(slider.getCurrentItem() + 1, true);
                } catch (IndexOutOfBoundsException ie) {
                    selectedPosition++;
                    Toast.makeText(activity, "Its the first Picture", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ivPreviousPictureYacht:
                try {
                    slider.setCurrentItem(slider.getCurrentItem() - 1, true);
                } catch (IndexOutOfBoundsException ie) {
                    selectedPosition++;
                    Toast.makeText(activity, "Its the Last Picture", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tvBookNow1:

                Log.e("booknowclicked","book now clicked");
                startActivity(new Intent(activity, BookingActivity.class));
//                if (AppController.getInstance().getPrefManger().getUserProfile()==null)
//                {
//                    startActivity(new Intent(activity, PaymentFormActivity.class).putExtra("goToActivity", "bookNow"));
//                }
//                else
//                {
//                    startActivity(new Intent(activity, BookingActivity.class));
//                }
                break;
        }


    }
}
