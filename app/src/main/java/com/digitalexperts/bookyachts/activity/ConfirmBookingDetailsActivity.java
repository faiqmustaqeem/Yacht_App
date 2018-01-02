package com.digitalexperts.bookyachts.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.digitalexperts.bookyachts.R;
import com.digitalexperts.bookyachts.customClasses.AppConstants;
import com.digitalexperts.bookyachts.customClasses.AppController;
import com.digitalexperts.bookyachts.interfaces.WebServices;
import com.digitalexperts.bookyachts.payment_utility.AvenuesParams;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.gson.Gson;

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

import static com.digitalexperts.bookyachts.customClasses.AppConstants.bookingDate;
import static com.digitalexperts.bookyachts.customClasses.AppConstants.bookingDuration;
import static com.digitalexperts.bookyachts.customClasses.AppConstants.bookingStartTime;
import static com.digitalexperts.bookyachts.customClasses.AppConstants.bookingStartTimeIndex;
import static com.digitalexperts.bookyachts.customClasses.AppConstants.discountPercentage;

public class ConfirmBookingDetailsActivity extends AppCompatActivity {
    @BindView(R.id.tvConfirm)
    TextView tvConfirm;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.txtSelectYacht)
    TextView txtSelectYacht;


    @BindView(R.id.etDate)
    EditText etDate;
    @BindView(R.id.etStartTime)
    EditText etStartTime;
    @BindView(R.id.etDuration)
    EditText etDuration;
    @BindView(R.id.etTotalAmount)
    EditText etTotalAmount;

    @BindView(R.id.etPercent)
    EditText etPercent;

    @BindView(R.id.etRemaining)
    EditText etRemaining;

    @BindView(R.id.spin_kit)
    SpinKitView spinKit;

    Activity activity;
    private String accessCode, merchantId, currency, amount, orderId, rsaKeyUrl, redirectUrl, cancelUrl;

    double payingNow=0;
    double total=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_booking_details);
        ButterKnife.bind(this);
        activity=this;
        getSupportActionBar().setTitle("CONFIRM BOOKING");

        etStartTime.setText(bookingStartTime);

        etDuration.setText(AppConstants.bookYachtModel.getDuration()+" HOURS");

        String s=AppConstants.yachtsModel.getTitle();
       if(!s.equals("")) {
           txtSelectYacht.setText(s.substring(0, 2) + "ft");
       }

            tvPrice.setText(AppConstants.yachtsModel.getPrice());



        etDate.setText(bookingDate);

        total =Double.parseDouble(AppConstants.yachtsModel.getPrice())* Double.parseDouble(AppConstants.bookYachtModel.getDuration());
        Log.e("total",total+"");

        AppConstants.totalAmount = total;
        etTotalAmount.setText(String.valueOf(total)+" AED");


        String percent=AppConstants.pay20Percent ? "20" : "100";

        etPercent.setText(percent+" %");






        if(percent.equals("20")) {
             payingNow = getPercentAmount(total,20 );
        }
        else
        {
            payingNow=total;
        }

        AppConstants.amountPayingNow=payingNow;
        double remaining=total-payingNow;
        AppConstants.remainingAmount=remaining;

        etRemaining.setText(String.valueOf(remaining)+" AED" );

    }

double getPercentAmount(double amount , double percent)
{
    double payingNow=amount*(percent/100);
    return payingNow;
}
    @OnClick({R.id.tvConfirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvConfirm:

                if(AppController.getInstance().getPrefManger().getUserProfile()!=null) {

                    bookNow();
                }
                else
                {
                    Intent i = new Intent(ConfirmBookingDetailsActivity.this, UserInfo.class);
                    startActivity(i);
                    this.finish();
                }
                break;
        }
    }
    private boolean validation() {
        if (!AppConstants.isOnline(activity)) {
            Toast.makeText(activity, "Please Connect Internet", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
    private void bookNow() {
        if (validation())
        {
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

            body.put("payment_status", "Unpaid");
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

          //  body.put("card_expiry", txtMonth.getText().toString()+ "/"+txtYear.getText().toString());

          //  body.put("card_number", txtCardNumber.getText().toString());

           // body.put("cvv_code", txtCvvCode.getText().toString());

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
                                Log.e("tag","3");
                                JSONObject dataObject=jsonObject.getJSONObject("data");

                                orderId  = dataObject.getString("order_id").substring(0,dataObject.getString("order_id").length()-2); //get from reply of api
                                AppConstants.order_id=orderId;
                                accessCode = "AVBU02EL68AF26UBFA";
                                merchantId = "43560";
                                currency = "AED";
                                amount = String.valueOf(payingNow); // from amount paying now
                                rsaKeyUrl = "https://bookyachts.ae/appresponse/GetRSA.php";
                                redirectUrl = "https://bookyachts.ae/appresponse/ccavResponseHandler.php";
                                cancelUrl = "https://bookyachts.ae/appresponse/ccavResponseHandler.php";

                                if(spinKit.isShown())
                                spinKit.setVisibility(View.GONE);

                                Log.e("tag","4");
                                   goToWebView();



                            } else if (status.equals("error"))
                            {
                                spinKit.setVisibility(View.GONE);
                                Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            spinKit.setVisibility(View.GONE);

                            Log.e("ErrorMessage", e.getMessage());
                            Toast.makeText(activity, "Error new ", Toast.LENGTH_LONG).show();

                            e.printStackTrace();
                        }
                    } else {
                        spinKit.setVisibility(View.GONE);
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




    public void goToWebView() {
        //Mandatory parameters. Other parameters can be added if required.
    Log.e("tag","1"+" access_code="+accessCode +" order_id="+orderId);
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

            intent.putExtra("billing_name",AppController.getInstance().getPrefManger().getUserProfile().getName());
            intent.putExtra("billing_city",AppController.getInstance().getPrefManger().getUserProfile().getCity());
            intent.putExtra("billing_state",AppController.getInstance().getPrefManger().getUserProfile().getState());
            intent.putExtra("billing_country",AppController.getInstance().getPrefManger().getUserProfile().getCountry());
            intent.putExtra("billing_zip",AppController.getInstance().getPrefManger().getUserProfile().getPostalCode());
            intent.putExtra("billing_tel",AppController.getInstance().getPrefManger().getUserProfile().getContactNo());
            intent.putExtra("billing_email",AppController.getInstance().getPrefManger().getUserProfile().getEmail());
            intent.putExtra("billing_address",AppController.getInstance().getPrefManger().getUserProfile().getAddress());
            Log.e("tag","2");
            startActivity(intent);
            activity.finish();

        }else{
            showToast("All parameters are mandatory.");
        }
    }
    public void showToast(String msg) {
        Toast.makeText(activity, "Toast: " + msg, Toast.LENGTH_LONG).show();
    }


}