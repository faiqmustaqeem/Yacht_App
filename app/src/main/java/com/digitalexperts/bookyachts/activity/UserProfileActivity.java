package com.digitalexperts.bookyachts.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileActivity extends AppCompatActivity {

    Activity activity;
    @BindView(R.id.tvLogout)
    TextView tvLogout;
    @BindView(R.id.btnUpdate)
    Button btnUpdate;
    @BindView(R.id.txtFullName)
    EditText txtFullName;
    @BindView(R.id.txtPassword)
    EditText txtPassword;
    @BindView(R.id.spin_kit)
    SpinKitView spinKit;

    protected void onCreate(Bundle savedInstanceState) {
        // Hide the status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ButterKnife.bind(this);

        // hiding action bar
        getSupportActionBar().hide();

        activity = this;

        txtFullName.setText(AppController.getInstance().getPrefManger().getUserProfile().getName());
    }




    private void updateUser() {
        if (checkValidation()) {
            spinKit.setVisibility(View.VISIBLE);
            btnUpdate.setText("Please Wait..");
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(AppConstants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            WebServices service = retrofit.create(WebServices.class);

            HashMap<String, String> body = new HashMap<>();
            body.put("password", txtPassword.getText().toString());
            body.put("name", txtFullName.getText().toString());
            body.put("api_secret", AppController.getInstance().getPrefManger().getUserProfile().getAuthKey());

            service.updateUser(body).enqueue(new Callback<Object>()
            {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response)
                {
                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());

                    if (response.isSuccessful())
                    {
                        try
                        {
                            JSONObject jsonObject = new JSONObject(json);

                            JSONObject resultObject = jsonObject.getJSONObject("result");

                            String status = resultObject.getString("status");

                            String message = resultObject.getString("response");

                            if (status.equals("success"))
                            {
                                UserModel userModel = AppController.getInstance().getPrefManger().getUserProfile();

                                userModel.setName(txtFullName.getText().toString());

                                AppController.getInstance().getPrefManger().setUserProfile(userModel);

                                Toast.makeText(activity, "Profile Updated", Toast.LENGTH_SHORT).show();

                                btnUpdate.setText("Update");
                                spinKit.setVisibility(View.GONE);
                                activity.finish();

                            }
                            else
                            {
                                btnUpdate.setText("Update");
                                spinKit.setVisibility(View.GONE);
                                Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            btnUpdate.setText("Update");
                            spinKit.setVisibility(View.GONE);
                            Toast.makeText(activity, "Error", Toast.LENGTH_LONG).show();

                            e.printStackTrace();
                        }
                    } else {
                        btnUpdate.setText("Update");
                        spinKit.setVisibility(View.GONE);
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            Toast.makeText(UserProfileActivity.this, jObjError.getJSONObject("result").getString("response") , Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(UserProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    btnUpdate.setText("Update");
                }
            });
        } else {

        }

    }


    private boolean checkValidation() {

        if (!AppConstants.isOnline(activity)) {
            Toast.makeText(activity, "Please Connect Your Internet", Toast.LENGTH_LONG).show();
            return false;
        } else if (!Pattern.compile("[a-zA-Z ]{3,}").matcher(txtFullName.getText().toString().trim()).matches()) {
            Toast.makeText(activity, "Enter Correct Name", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    @OnClick({R.id.tvLogout, R.id.btnUpdate})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvLogout:
                // changeFragment(new ProfileUpdate());
                if (AppController.getInstance().getPrefManger().getUserProfile() != null) {

                    AppController.getInstance().getPrefManger().clear();
                    Intent intent = new Intent(activity, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    activity.finish();
                }
                break;
            case R.id.btnUpdate:
                updateUser();
                break;
        }
    }
}
