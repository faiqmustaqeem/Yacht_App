package com.digitalexperts.bookyachts.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.digitalexperts.bookyachts.R;
import com.digitalexperts.bookyachts.customClasses.AppConstants;
import com.digitalexperts.bookyachts.customClasses.AppController;
import com.digitalexperts.bookyachts.interfaces.WebServices;
import com.digitalexperts.bookyachts.models.UserModel;
import com.digitalexperts.bookyachts.payment_utility.AvenuesParams;
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


public class HaveAccounnt extends AppCompatActivity {
    Activity activity;
    @BindView(R.id.txtEmailAddress)
    EditText txtEmailAddress;
    @BindView(R.id.txtPassword)
    EditText txtPassword;
    @BindView(R.id.btnLogin)
    Button btnLogin;

    @BindView(R.id.spin_kit)
    SpinKitView spinKit;

    @BindView(R.id.tvMemberForgetPassword)
    TextView tvMemberForgetPassword;

    String goToActivity;
    private String accessCode, merchantId, currency, amount, orderId, rsaKeyUrl, redirectUrl, cancelUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_have_accounnt);
        ButterKnife.bind(this);

        activity = this;

       // Bundle bundle = getIntent().getExtras();
//        if (bundle != null) {
//            goToActivity = bundle.getString("goToActivity");
//        }

        // hiding action bar
        getSupportActionBar().setTitle("Login");
    }
    @OnClick({R.id.btnLogin,R.id.tvMemberForgetPassword})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                loginUser();
                break;

            case R.id.tvMemberForgetPassword:
                    Intent i=new Intent(activity , ForgotPassword.class);
                    startActivity(i);

                break;

        }
    }

    private void loginUser() {
        if (checkValidation()) {
            spinKit.setVisibility(View.VISIBLE);
            btnLogin.setText("Please Wait..");
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(AppConstants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            WebServices service = retrofit.create(WebServices.class);

            HashMap<String, String> body = new HashMap<>();
            body.put("email", txtEmailAddress.getText().toString());
            body.put("password", txtPassword.getText().toString());
            body.put("signin_type", "2");
            body.put("gcm_key", AppController.getInstance().getPrefManger().getValue("firebaseId", "").toString());


            service.loginUser(body).enqueue(new Callback<Object>() {
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


                                JSONObject userDataObject = resultObject.getJSONObject("user_data");
                                UserModel userModel;
                                userModel = new UserModel();
                                userModel.setUserId(userDataObject.getString("user_id"));
                                userModel.setName(userDataObject.getString("name"));
                                userModel.setAuthKey(userDataObject.getString("api_secret"));
                                userModel.setEmail(userDataObject.getString("email"));
                                userModel.setCountry(userDataObject.getString("country_name"));
                                userModel.setState(userDataObject.getString("state_name"));
                                if(userDataObject.getString("country_name").equals("United Arab Emirates"))
                                {
                                    userModel.setCity(userDataObject.getString("state_name"));
                                }
                                else {
                                    userModel.setCity(userDataObject.getString("city_name"));
                                }

                                userModel.setPostalCode(userDataObject.getString("postal_code"));
                                userModel.setContactNo(userDataObject.getString("phone_number"));
                                userModel.setAddress(userDataObject.getString("address_1"));


                                AppController.getInstance().getPrefManger().setUserProfile(userModel);


                                bookNow();

                                Toast.makeText(activity, "Login Successfully", Toast.LENGTH_SHORT).show();

                                spinKit.setVisibility(View.GONE);
                                activity.finish();
                                btnLogin.setText("Login");

                            } else {
                                btnLogin.setText("Login");
                                spinKit.setVisibility(View.GONE);
                                Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            btnLogin.setText("Login");
                            spinKit.setVisibility(View.GONE);
                            Toast.makeText(activity, "Error", Toast.LENGTH_LONG).show();

                            e.printStackTrace();
                        }
                    } else {
                        btnLogin.setText("Login");
                        spinKit.setVisibility(View.GONE);

                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            Toast.makeText(HaveAccounnt.this, jObjError.getJSONObject("result").getString("response") , Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(HaveAccounnt.this, e.getMessage(), Toast.LENGTH_LONG).show();
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

    private boolean checkValidation() {

        if (!AppConstants.isOnline(activity)) {
            Toast.makeText(this, "Please Connect Your Internet", Toast.LENGTH_LONG).show();
            return false;
        } else if (txtPassword.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_LONG).show();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(txtEmailAddress.getText().toString()).matches()) {

            Toast.makeText(this, "Enter Correct Email", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    private boolean validation() {
        if (!AppConstants.isOnline(activity)) {
            Toast.makeText(activity, "Please Connect Internet", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
    private void bookNow()
    {
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

                                JSONObject dataObject=jsonObject.getJSONObject("data");

                                orderId  = dataObject.getString("order_id").substring(0,dataObject.getString("order_id").length()-2); //get from reply of api
                                AppConstants.order_id=orderId;
                                Log.e("order_id",orderId);

                                AppConstants.order_id=orderId;
                                accessCode = AppConstants.accessCode;
                                merchantId = AppConstants.merchantId;
                                currency = AppConstants.currency;
                             //   amount = String.valueOf(payingNow); // from amount paying now
                                amount = String.valueOf(AppConstants.amountPayingNow); // from amount paying now
                                rsaKeyUrl = AppConstants.rsaKeyUrl;
                                redirectUrl = AppConstants.redirectUrl;
                                cancelUrl = AppConstants.redirectUrl;

                                spinKit.setVisibility(View.GONE);
                                goToWebView();



                            }
                            else if (status.equals("error")) {
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
        Log.e("tag","go to web start");
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
            startActivity(intent);
            Log.e("tag","end web view");
            activity.finish();

        }else{
            showToast("All parameters are mandatory.");
        }
    }
    public void showToast(String msg) {
        Toast.makeText(activity, "Toast: " + msg, Toast.LENGTH_LONG).show();
    }


}
