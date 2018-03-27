package com.digitalexperts.bookyachts.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.digitalexperts.bookyachts.ResideMenu.ResideMenu;
import com.digitalexperts.bookyachts.ResideMenu.ResideMenuItem;
import com.digitalexperts.bookyachts.dialogs.CallBackDialog;
import com.digitalexperts.bookyachts.dialogs.ContactUsDialog;
import com.digitalexperts.bookyachts.payment_utility.AvenuesParams;
import com.digitalexperts.bookyachts.payment_utility.ServiceUtility;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.ybq.android.spinkit.SpinKitView;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.google.gson.Gson;
import com.digitalexperts.bookyachts.R;
import com.digitalexperts.bookyachts.customClasses.AppConstants;
import com.digitalexperts.bookyachts.customClasses.AppController;
import com.digitalexperts.bookyachts.interfaces.WebServices;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype.Fadein;

public class PaymentFormActivity extends AppCompatActivity implements View.OnClickListener  {
//
//    @BindView(R.id.rbPay20Percent)
//    RadioButton rbPay20Percent;
//    @BindView(R.id.rbPayFull)
//    RadioButton rbPayFull;
    @BindView(R.id.txtCardNumber)
    EditText txtCardNumber;
    @BindView(R.id.txtMonth)
    EditText txtMonth;
    @BindView(R.id.txtYear)
    EditText txtYear;
    @BindView(R.id.txtCvvCode)
    EditText txtCvvCode;
    @BindView(R.id.tvCallNow)
    TextView tvCallNow;

    Activity activity;

    @BindView(R.id.btnPayNow)
    Button btnPayNow;

    @BindView(R.id.spin_kit)
    SpinKitView spinKit;

    @BindView(R.id.tvPrice)
    TextView tvPrice;

    @BindView(R.id.tvDiscount)
    TextView tvDiscount;

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

    private String selectedPay;

    LayoutInflater inflater;
    View customView;
    private String accessCode, merchantId, currency, amount, orderId, rsaKeyUrl, redirectUrl, cancelUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Hide the status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_form);
        ButterKnife.bind(this);

        activity = this;

         inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         customView = inflater.inflate(R.layout.activity_success, null);



        // hiding action bar
        getSupportActionBar().hide();

       double total = AppConstants.totalAmount;



//        tvShowTotal.setText(total + " + " + AppConstants.subFacilityTotal);

//        tvPrice.setText(AppConstants.yachtsModel.getCurrency() + " " + (AppConstants.subFacilityTotal + total));

        tvPrice.setText(AppConstants.yachtsModel.getCurrency() + " " + (AppConstants.amountPayingNow));


//        if (AppConstants.discountPercentage > 0) {
//            tvDiscount.setVisibility(View.VISIBLE);
//            tvDiscount.setText("Discount " + AppConstants.discountPercentage + "%");
//            total = total + AppConstants.subFacilityTotal;
//
//            AppConstants.discountAmount = (total) * (AppConstants.discountPercentage/100);
//
//            AppConstants.totalAmount = total - AppConstants.discountAmount;
         //   tvPrice.setText(AppConstants.yachtsModel.getCurrency() + " " + AppConstants.totalAmount);
//        }


        setUpMenu();

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

    @OnClick({R.id.rbPay20Percent, R.id.rbPayFull, R.id.btnPayNow, R.id.tvCallNow, R.id.ivNavigationIcon, R.id.ivCallBack, R.id.ivContactUs, R.id.ivHotDeals, R.id.ivHome})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rbPay20Percent:
                break;
            case R.id.rbPayFull:
                break;
            case R.id.btnPayNow:
                bookNow();
                break;
            case R.id.tvCallNow:

                new CallBackDialog(PaymentFormActivity.this);

//                showCallDialog();
                break;


            //bottom navigation
            case R.id.ivNavigationIcon:
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
                break;
            case R.id.ivCallBack:
                new CallBackDialog(PaymentFormActivity.this);
                break;
            case R.id.ivContactUs:
                new ContactUsDialog(PaymentFormActivity.this);
                break;
            case R.id.ivHotDeals:
                Intent i = new Intent(PaymentFormActivity.this, HotDealsActivity.class);
                startActivity(i);
                break;
            case R.id.ivHome:
                finish();
                ShipDetailsActivity.activity.finish();
                break;
        }
    }

    private void showCallDialog() {
        final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(activity);
        dialogBuilder
                .withTitle("Call")                                  //.withTitle(null)  no title
                .withTitleColor("#FFFFFF")                                  //def
                .withDividerColor("#11000000")                              //def
                .withMessage("Call To Book A Yacht \n +123456789")                     //.withMessage(null)  no Msg
                .withMessageColor("#FFFFFFFF")                              //def  | withMessageColor(int resid)
                .withDialogColor("#FFE74C3C")                               //def  | withDialogColor(int resid)
                .withIcon(getResources().getDrawable(R.drawable.icon_phonenumber))
                .withDuration(1200)                                          //def
                .withEffect(Fadein)                                         //def Effectstype.Slidetop
                .withButton1Text("CALL")                                      //def gone
                .isCancelableOnTouchOutside(true)                           //def    | isCancelable(true)
                //  .setCustomView(R.layout.custom_view,v.getContext())         //.setCustomView(View or ResId,context)
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(activity, "Option Will Apply Soon", Toast.LENGTH_SHORT).show();
                        dialogBuilder.dismiss();
                    }
                }).show();
    }

    private boolean validation() {
        if (!AppConstants.isOnline(activity)) {
            Toast.makeText(activity, "Please Connect Internet", Toast.LENGTH_LONG).show();
            return false;
        } else if (txtCardNumber.getText().toString().trim().length() < 8) {
            Toast.makeText(activity, "Enter Proper Card Number", Toast.LENGTH_LONG).show();
            return false;
        } else if (txtCvvCode.getText().toString().trim().length() < 3) {
            Toast.makeText(activity, "Enter Proper Code", Toast.LENGTH_LONG).show();
            return false;
        } else if (txtMonth.getText().toString().trim().length() == 0) {
            Toast.makeText(activity, "Enter Proper Month", Toast.LENGTH_LONG).show();
            return false;
        } else if (txtYear.getText().toString().trim().length() == 0) {
            Toast.makeText(activity, "Enter Proper Year", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
    public String getEndTime(String startTime , String duration)
    {
        //int i=startTime.indexOf(':');
        String startHours=startTime.substring(0,2);
        String endTime="";
        if((String.valueOf(Integer.parseInt(startHours)+Integer.parseInt(duration))).length() == 1)
            endTime="0"+String.valueOf(Integer.parseInt(startHours)+Integer.parseInt(duration))+":00";
        else
            endTime=String.valueOf(Integer.parseInt(startHours)+Integer.parseInt(duration))+":00";

        return endTime;
    }
    private void bookNow() {
        if (validation()) {
            spinKit.setVisibility(View.VISIBLE);
            AppConstants.yachtsHotSlotsArrayList.clear();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(AppConstants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            WebServices service = retrofit.create(WebServices.class);

            HashMap<String, String> body = new HashMap<>();

            body.put("yacht_id", AppConstants.yachtsModel.getId());
            Log.e("test", "yacht_id " + AppConstants.yachtsModel.getId());

            body.put("api_secret", AppController.getInstance().getPrefManger().getUserProfile().getAuthKey());
            Log.e("test", "api_secret " + AppController.getInstance().getPrefManger().getUserProfile().getAuthKey());

            body.put("booking_date", AppConstants.bookYachtModel.getStartDate());
            Log.e("test", "booking_date " + AppConstants.bookYachtModel.getStartDate());

            body.put("booking_time",convert12HoursFormatTo24Hours(AppConstants.bookYachtModel.getStartTime()));
            Log.e("test", "booking_time " + convert12HoursFormatTo24Hours(AppConstants.bookYachtModel.getStartTime()));

            body.put("duration", AppConstants.bookYachtModel.getDuration());
            // Log.e("test","booking_date "+AppConstants.bookYachtModel.getStartDate());

            body.put("payment_type", "Stripe");
            Log.e("test", "payment_type " + "Stripe");

            body.put("payment_status", "3");
            Log.e("test", "payment_status " + "Unpaid");

            body.put("facilities", AppConstants.inclusiveFacilities);
            Log.e("test", "facilities " + AppConstants.inclusiveFacilities);

            body.put("sub_total", String.valueOf(AppConstants.totalAmount));
            Log.e("test", "sub_total " + AppConstants.yachtsModel.getPrice());

            body.put("facilities_amount", AppConstants.subFacilityTotal + "");
            Log.e("test", "facilities_amount " + AppConstants.subFacilityTotal);

            body.put("discount", AppConstants.discountAmount + "");
            Log.e("test", "discount " + AppConstants.discountAmount);

            body.put("sub_facilities", AppConstants.exclusiveFacilities);
            Log.e("test", "sub_facilities " + AppConstants.exclusiveFacilities);

            body.put("total_amount", AppConstants.totalAmount + "");
            Log.e("test", "total_amount " + AppConstants.totalAmount);

            body.put("amount_paying_now", AppConstants.amountPayingNow + "");

            body.put("amount_remaining", AppConstants.remainingAmount + "");

            body.put("card_expiry", txtMonth.getText().toString()+ "/"+txtYear.getText().toString());

            body.put("card_number", txtCardNumber.getText().toString());

            body.put("cvv_code", txtCvvCode.getText().toString());
            body.put("no_of_guests",AppConstants.no_of_guests);

            Log.e("body_payment",body.toString());

            service.booking(body).enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    Log.e("json_payment", json);

                    if (response.isSuccessful())
                    {
                        try {

                            JSONObject jsonObject = new JSONObject(json);

                            JSONObject resultObject = jsonObject.getJSONObject("result");

                            String status = resultObject.getString("status");

                            String message = resultObject.getString("response");

                            if (status.equals("success")) {

                               // Toast.makeText(activity, message, Toast.LENGTH_LONG).show();

                                spinKit.setVisibility(View.GONE);
                                MaterialStyledDialog  materialStyledDialog= new MaterialStyledDialog.Builder(PaymentFormActivity.this)

                                        .setTitle("Success")
                                        .setDescription(message)
                                        //.setDescription("What can we improve? Your feedback is always welcome.")
                                        //.setCustomView(customView)
                                        .setPositiveText("OK")
                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                Intent intent = new Intent(activity, DashboardActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                                activity.finish();

                                            }
                                        })
                                       .show();

                            } else if (status.equals("error")) {
                                spinKit.setVisibility(View.GONE);
                                Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            spinKit.setVisibility(View.GONE);

                            Toast.makeText(activity, "Error ", Toast.LENGTH_LONG).show();

                            e.printStackTrace();
                        }
                    } else {
                        spinKit.setVisibility(View.GONE);
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            Toast.makeText(PaymentFormActivity.this, jObjError.getJSONObject("result").getString("response") , Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(PaymentFormActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    Toast.makeText(PaymentFormActivity.this,t.toString(),Toast.LENGTH_LONG).show();
                }


            });
        }
        else {

        }

    }

    private String convert12HoursFormatTo24Hours(String time12) {
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

    private void init(){
        accessCode = "AVBU02EL68AF26UBFA";
        merchantId = "43560";
        orderId  = ""; //get from reply of api
        currency = "AED";
        amount = ""; // from amount paying now
        rsaKeyUrl = "https://bookyachts.ae/appresponse/GetRSA.php";
        redirectUrl = "https://bookyachts.ae/appresponse/ccavResponseHandler.php";
        cancelUrl = "https://bookyachts.ae/appresponse/ccavResponseHandler.php";
    }

    //@Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_initial_screen);
//        init();
//
//        //generating order number
//        Integer randomNum = ServiceUtility.randInt(0, 9999999);
//        orderId.setText(randomNum.toString());
//    }

    public void goToWebView() {
        //Mandatory parameters. Other parameters can be added if required.
//        String vAccessCode = ServiceUtility.chkNull(accessCode.toString());
//        String vMerchantId = ServiceUtility.chkNull(merchantId.getText()).toString().trim();
//        String vCurrency = ServiceUtility.chkNull(currency.getText()).toString().trim();
//        String vAmount = ServiceUtility.chkNull(amount.getText()).toString().trim();
        if(!accessCode.equals("") && !merchantId.equals("") && !currency.equals("") && !amount.equals("")){
            Intent intent = new Intent(this,WebViewActivity.class);
            intent.putExtra(AvenuesParams.ACCESS_CODE, accessCode);
            intent.putExtra(AvenuesParams.MERCHANT_ID, merchantId);
            intent.putExtra(AvenuesParams.ORDER_ID, orderId);
            intent.putExtra(AvenuesParams.CURRENCY, currency);
            intent.putExtra(AvenuesParams.AMOUNT, amount);

            intent.putExtra(AvenuesParams.REDIRECT_URL, redirectUrl);
            intent.putExtra(AvenuesParams.CANCEL_URL, cancelUrl);
            intent.putExtra(AvenuesParams.RSA_KEY_URL, rsaKeyUrl);

            startActivity(intent);
        }else{
            showToast("All parameters are mandatory.");
        }
    }

    public void showToast(String msg) {
        Toast.makeText(this, "Toast: " + msg, Toast.LENGTH_LONG).show();
    }
}
