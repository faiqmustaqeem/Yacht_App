package com.digitalexperts.bookyachts.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.gson.Gson;
import com.digitalexperts.bookyachts.R;
import com.digitalexperts.bookyachts.customClasses.AppConstants;
import com.digitalexperts.bookyachts.customClasses.AppController;
import com.digitalexperts.bookyachts.interfaces.WebServices;
import com.digitalexperts.bookyachts.models.UserModel;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    Activity activity;
    @BindView(R.id.txtEmailAddress)
    EditText txtEmailAddress;
    @BindView(R.id.txtPassword)
    EditText txtPassword;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.tvSignUp)
    TextView tvSignUp;
    @BindView(R.id.spin_kit)
    SpinKitView spinKit;

    String goToActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Hide the status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        activity = this;

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            goToActivity = bundle.getString("goToActivity");
        }

        // hiding action bar
        getSupportActionBar().setTitle("Login");

    }

    @OnClick({R.id.btnLogin, R.id.tvSignUp})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                loginUser();
                break;
            case R.id.tvSignUp:
                Intent intent = new Intent(activity, RegisterUserActivity.class);
                if (goToActivity.equals("bookNow")) {
                  //  intent.putExtra("goToActivity", "bookNow");
                } else {
                    //intent.putExtra("goToActivity", "back");
                }
                startActivity(intent);
                finish();
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

                                //JSONObject dataObject = jsonObject.getJSONObject("data");
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



                                if (goToActivity.equals("bookNow"))
                                {
                                    Intent intent = new Intent(activity, BookingActivity.class);
                                    // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                                else if (goToActivity.equals("back"))
                                {

                                }

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
                            Toast.makeText(LoginActivity.this, jObjError.getJSONObject("result").getString("response") , Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
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
}