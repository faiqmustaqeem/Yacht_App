package com.digitalexperts.bookyachts.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.transition.Visibility;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.digitalexperts.bookyachts.R;
import com.digitalexperts.bookyachts.customClasses.AppConstants;
import com.digitalexperts.bookyachts.customClasses.AppController;
import com.digitalexperts.bookyachts.interfaces.WebServices;
import com.digitalexperts.bookyachts.models.APIError;
import com.digitalexperts.bookyachts.models.ErrorUtils;
import com.digitalexperts.bookyachts.models.UserModel;
import com.digitalexperts.bookyachts.payment_utility.AvenuesParams;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserInfo extends AppCompatActivity {

    Activity activity;
    @BindView(R.id.txtFirstName)
    EditText txtFirstName;

    @BindView(R.id.txtLastName)
    EditText txtLastName;

    @BindView(R.id.txtEmailAddress)
    EditText txtEmailAddress;

    @BindView(R.id.txtPassword)
    EditText txtPassword;

    @BindView(R.id.txtConfirmPass)
    EditText txtConfirmPass;

    @BindView(R.id.txtPhoneNumber)
    EditText txtPhoneNumber;

    @BindView(R.id.btnRegister)
    Button btnRegister;

    @BindView(R.id.spin_kit)
    SpinKitView spinKit;

    @BindView(R.id.spinner_country)
    Spinner spinnerCountry;

    @BindView(R.id.spinner_states)
    Spinner spinnerState;

    @BindView(R.id.spinner_city)
    Spinner spinnerCity;

    @BindView(R.id.txtAddress1)
    EditText txtAddress1;

    @BindView(R.id.txtAddress2)
    EditText txtAddress2;

    @BindView(R.id.txtPostalCode)
    EditText txtPostalCode;

    @BindView(R.id.already_have_account)
    TextView already_have_account;

    String registerStatus="";

    String goToActivity;
    // String
    final HashMap<String,String> listCountryModel=new HashMap<>();
    final HashMap<String,String> listStateModel=new HashMap<>();
    final HashMap<String,String> listCityModel=new HashMap<>();

    String countryName, countryId;
    String stateName , stateId;
    String cityName , cityId;

    private String accessCode, merchantId, currency, amount, orderId, rsaKeyUrl, redirectUrl, cancelUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        ButterKnife.bind(this);


        activity = this;
        countryName ="" ;
        countryId="";
        stateName="" ;
        stateId="";
        cityName="" ;
        cityId="";

        Bundle bundle = getIntent().getExtras();
      //  if(bundle != null)
//        {
//            goToActivity = bundle.getString("goToActivity");
//        }
        // spinnerCountry.setOnItemSelectedListener(this);
        // spinnerState.setOnItemSelectedListener(this);
        // spinnerCity.setOnItemSelectedListener(this);
        // hiding action bar
        getSupportActionBar().setTitle("User Details");
        // getSupportActionBar()

        getCountries();
        //getCountries();


        txtPassword.setText(randomString(8));

        already_have_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(UserInfo.this,HaveAccounnt.class);
                startActivity(i);
              //  activity.finish();

            }
        });

        // spinnerCountry.setItems(getCountries());



    }

    private void getCountries()
    {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WebServices service = retrofit.create(WebServices.class);

        HashMap<String, String> body = new HashMap<>();


        spinKit.setVisibility(View.VISIBLE);

        service.fetchCountries(body).enqueue(new Callback<Object>()
        {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response)
            {

                Gson gson = new Gson();
                String json = gson.toJson(response.body());

                if (response.isSuccessful()) {
                    try {

                        JSONObject jsonObject = new JSONObject(json);

                        JSONObject resultObject = jsonObject.getJSONObject("result");

                        String status = resultObject.getString("status");

                        String message = resultObject.getString("response");

                        if (status.equals("success")) {

                            JSONObject dataObject = jsonObject.getJSONObject("data");

                            JSONArray countriesDataArray = dataObject.getJSONArray("countries");
                            List<String> countries=new ArrayList<String>();
                            listCountryModel.clear();
                            for(int i=0 ; i < countriesDataArray.length() ; i++)
                            {
                                JSONObject country=countriesDataArray.getJSONObject(i);
                                String name=country.getString("name");
                                countries.add(name);
                                Log.e("names",name);
                                String id=country.getString("id");

                                listCountryModel.put(name,id);
                            }


                            spinKit.setVisibility(View.GONE);
                            //
                            ArrayAdapter adapter = new ArrayAdapter(UserInfo.this,android.R.layout.simple_spinner_item,countries);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerCountry.setAdapter(adapter);
                            spinnerCountry.setSelected(true);
                            spinnerCountry.setSelection(228);
                            spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    countryName=adapterView.getItemAtPosition(i).toString();
                                    Log.e("country",countryName);
                                    Log.e("hashmap_size",String.valueOf(listCountryModel.size()));
                                    countryId=listCountryModel.get(countryName).toString();

                                    Log.e("countryId", countryId);

                                    if(!countryId.equals("")) {
                                        Log.e("country", countryId);
                                        getStates(countryId);
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });

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
                        Toast.makeText(UserInfo.this, jObjError.getJSONObject("result").getString("response") , Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(UserInfo.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });



    }
    private void getStates(final String country_id)
    {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WebServices service = retrofit.create(WebServices.class);

        HashMap<String, String> body = new HashMap<>();
        body.put("country_id",country_id);

        spinKit.setVisibility(View.VISIBLE);
        service.fetchStates(body).enqueue(new Callback<Object>()
        {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response)
            {

                Gson gson = new Gson();
                String json = gson.toJson(response.body());

                if (response.isSuccessful()) {
                    try {

                        JSONObject jsonObject = new JSONObject(json);

                        JSONObject resultObject = jsonObject.getJSONObject("result");

                        String status = resultObject.getString("status");

                        String message = resultObject.getString("response");
                        if (status.equals("success")) {

                            JSONObject dataObject = jsonObject.getJSONObject("data");

                            JSONArray statesDataArray = dataObject.getJSONArray("states");
                            List<String> states=new ArrayList<>();
                            listStateModel.clear();
                            for(int i=0 ; i < statesDataArray.length() ; i++)
                            {
                                JSONObject state=statesDataArray.getJSONObject(i);
                                String name=state.getString("name");
                                states.add(name);
                                String id=state.getString("id");

                                listStateModel.put(name , id);

                            }
                            ArrayAdapter adapter = new ArrayAdapter(UserInfo.this,android.R.layout.simple_spinner_item,states);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerState.setAdapter(adapter);
                            spinnerState.setSelected(true);
                            spinnerState.setSelection(0);
                            spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    stateName=adapterView.getItemAtPosition(i).toString();
                                    stateId=listStateModel.get(stateName);
                                    if(!stateId.equals("")) {
                                      if(!country_id.equals("229"))
                                      {
                                          findViewById(R.id.spinnerLayout3).setVisibility(View.VISIBLE);
                                          getCities(stateId);
                                      }
                                      else
                                      {
                                          findViewById(R.id.spinnerLayout3).setVisibility(View.GONE);
                                      }
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });
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
                        Toast.makeText(UserInfo.this, jObjError.getJSONObject("result").getString("response") , Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(UserInfo.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });

    }


    private void getCities(String state_id)
    {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WebServices service = retrofit.create(WebServices.class);

        HashMap<String, String> body = new HashMap<>();
        body.put("state_id",state_id);

        spinKit.setVisibility(View.VISIBLE);
        service.fetchCities(body).enqueue(new Callback<Object>()
        {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response)
            {

                Gson gson = new Gson();
                String json = gson.toJson(response.body());

                if (response.isSuccessful()) {
                    try {

                        JSONObject jsonObject = new JSONObject(json);

                        JSONObject resultObject = jsonObject.getJSONObject("result");

                        String status = resultObject.getString("status");

                        String message = resultObject.getString("response");

                        if (status.equals("success")) {

                            JSONObject dataObject = jsonObject.getJSONObject("data");

                            JSONArray citiesDataArray = dataObject.getJSONArray("cities");
                            List<String> cities=new ArrayList<>();
                            listCityModel.clear();
                            for(int i=0 ; i < citiesDataArray.length() ; i++)
                            {
                                JSONObject city=citiesDataArray.getJSONObject(i);
                                String name=city.getString("name");
                                cities.add(name);
                                String id=city.getString("id");

                                listCityModel.put(name , id);

                            }
                            ArrayAdapter adapter = new ArrayAdapter(UserInfo.this,android.R.layout.simple_spinner_item,cities);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerCity.setAdapter(adapter);
                            spinnerCity.setSelected(true);
                            spinnerCity.setSelection(0);
                            spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    cityName=adapterView.getItemAtPosition(i).toString();
                                    cityId=listCityModel.get(cityName);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });
                            spinKit.setVisibility(View.GONE);



                        }
                        else {
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
                        Toast.makeText(UserInfo.this, jObjError.getJSONObject("result").getString("response") , Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(UserInfo.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });

    }

    private void register() {
        if (checkValidation()) {
            spinKit.setVisibility(View.VISIBLE);
            btnRegister.setText("Please Wait..");
           final Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(AppConstants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            WebServices service = retrofit.create(WebServices.class);

            HashMap<String, String> body = new HashMap<>();
            body.put("first_name",txtFirstName.getText().toString());
            body.put("last_name",txtLastName.getText().toString());
            body.put("country_id",countryId);
            body.put("state_id",stateId);
            body.put("city_id",cityId);
            body.put("email", txtEmailAddress.getText().toString());
            body.put("address_1",txtAddress1.getText().toString());
            body.put("address_2",txtAddress2.getText().toString());
            body.put("postal_code",txtPostalCode.getText().toString());
            body.put("phone_number",txtPhoneNumber.getText().toString());
            body.put("password", txtPassword.getText().toString());
            body.put("signup_type", "2");
            body.put("gcm_key", AppController.getInstance().getPrefManger().getValue("firebaseId", "").toString());

//            Log.e("gcm_key",AppController.getInstance().getPrefManger().getValue("firebaseId", "").toString());
                Log.e("user_body",body.toString());
            service.registerUser(body).enqueue(new Callback<Object>()
            {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response)
                {

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                        Log.e("log_user",json);
                    if (response.isSuccessful()) {
                        try {

                            JSONObject jsonObject = new JSONObject(json);

                            JSONObject resultObject = jsonObject.getJSONObject("result");

                            String status = resultObject.getString("status");

                            String message = resultObject.getString("response");

                            if (status.equals("success")) {
                                Log.e("log_user","1");
                                JSONObject dataObject = jsonObject.getJSONObject("data");
                                Log.e("log_user","2");

                                JSONObject userDataObject = dataObject.getJSONObject("user_data");
                                Log.e("log_user","3");

                                UserModel userModel = AppController.getInstance().getPrefManger().getUserProfile();
                                Log.e("log_user","4");

                                if (userModel == null)
                                {
                                    userModel = new UserModel();
                                }
                                userModel.setUserId(userDataObject.getString("user_id"));

                                userModel.setName(txtFirstName.getText().toString());

                                userModel.setAuthKey(dataObject.getString("api_secret"));

                                userModel.setEmail(txtEmailAddress.getText().toString());
                                userModel.setCountry(countryName);
                                userModel.setState(stateName);
                                userModel.setCity(cityName);
                                userModel.setPostalCode(txtPostalCode.getText().toString());
                                userModel.setContactNo(txtPhoneNumber.getText().toString());
                                userModel.setAddress(txtAddress1.getText().toString());

                                AppController.getInstance().getPrefManger().setUserProfile(userModel);
                                Log.e("log_user","5");
                                registerStatus="successful";

                                    bookNow();

                                spinKit.setVisibility(View.GONE);

                               // btnRegister.setText("Register");

                            } else {
                                spinKit.setVisibility(View.GONE);
                                Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                          //  btnRegister.setText("Register");
                            spinKit.setVisibility(View.GONE);

                            Toast.makeText(activity, " Error", Toast.LENGTH_LONG).show();

                            e.printStackTrace();
                        }
                    } else {
                       // btnRegister.setText("Register");
                        spinKit.setVisibility(View.GONE);

                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            Toast.makeText(UserInfo.this, jObjError.getJSONObject("result").getString("response") , Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(UserInfo.this, e.getMessage(), Toast.LENGTH_LONG).show();
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
        } else if (!Pattern.compile("[a-zA-Z ]{3,}").matcher(txtFirstName.getText().toString().trim()).matches()) {
            Toast.makeText(this, "Enter Correct First Name", Toast.LENGTH_LONG).show();
            return false;
        }
        else if (!Pattern.compile("[a-zA-Z ]{3,}").matcher(txtLastName.getText().toString().trim()).matches()) {
            Toast.makeText(this, "Enter Correct Last Name", Toast.LENGTH_LONG).show();
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(txtEmailAddress.getText().toString()).matches()) {

            Toast.makeText(this, "Enter Correct Email", Toast.LENGTH_LONG).show();
            return false;
        }
        else if (txtPhoneNumber.getText().toString().equals("")) {

            Toast.makeText(this, "Enter Contact Number", Toast.LENGTH_LONG).show();
            return false;
        }
        else if (txtAddress1.getText().toString().equals("")) {

            Toast.makeText(this, "Enter Address", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(countryId.equals("")){
            Toast.makeText(this , "Select Country",Toast.LENGTH_LONG);
            return false;
        }
        else if(stateId.equals("")){
            Toast.makeText(this , "Select State",Toast.LENGTH_LONG);
            return false;
        }



        return true;
    }

    @OnClick(R.id.btnRegister)
    public void onClick() {
        if(registerStatus.equals("")) {
            register();
        }
        else if(registerStatus.equals("successful"))
        {
            bookNow();
        }
        //bookNow();
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

                                JSONObject dataObject=jsonObject.getJSONObject("data");

                                orderId  = dataObject.getString("order_id").substring(0,dataObject.getString("order_id").length()-2); //get from reply of api
                                AppConstants.order_id=orderId;
                                Log.e("order_id",orderId);
                                accessCode = "AVBU02EL68AF26UBFA";
                                merchantId = "43560";
                                currency = "AED";
                                amount = String.valueOf(AppConstants.amountPayingNow); // from amount paying now
                                rsaKeyUrl = "https://bookyachts.ae/appresponse/GetRSA.php";
                                redirectUrl = "https://bookyachts.ae/appresponse/ccavResponseHandler.php";
                                cancelUrl = "https://bookyachts.ae/appresponse/ccavResponseHandler.php";

                                spinKit.setVisibility(View.GONE);
                                goToWebView();



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
     String randomString( int len ){
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }








}



