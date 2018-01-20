package com.digitalexperts.bookyachts.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.digitalexperts.bookyachts.R;
import com.digitalexperts.bookyachts.customClasses.AppConstants;
import com.digitalexperts.bookyachts.customClasses.AppController;
import com.digitalexperts.bookyachts.interfaces.WebServices;
import com.digitalexperts.bookyachts.models.UserModel;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForgotPassword extends AppCompatActivity {

    Activity activity;
    @BindView(R.id.txtEmailAddress)
    EditText txtEmailAddress;

    @BindView(R.id.btn)
    Button btn;

    @BindView(R.id.spin_kit)
    SpinKitView spinKit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
        activity = this;

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmailtoResetPassowrd();
            }
        });
    }
    void sendEmailtoResetPassowrd()
    {
        if(validate())
        {

            spinKit.setVisibility(View.VISIBLE);
            btn.setText("Please Wait..");
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(AppConstants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            WebServices service = retrofit.create(WebServices.class);

            HashMap<String, String> body = new HashMap<>();
            body.put("email", txtEmailAddress.getText().toString());
            //  body.put("password", txtPassword.getText().toString());


            service.sendEmail(body).enqueue(new Callback<Object>() {
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


                                spinKit.setVisibility(View.GONE);
                               // Toast.makeText(activity , message , Toast.LENGTH_LONG).show();
                                MaterialStyledDialog materialStyledDialog= new MaterialStyledDialog.Builder(ForgotPassword.this)
                                        .setTitle("Success")
                                        .setDescription(message)
                                        //.setDescription("What can we improve? Your feedback is always welcome.")
                                        //.setCustomView(customView)
                                        .setPositiveText("OK")
                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                //Intent intent = new Intent(activity, DashboardActivity.class);
                                                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                               // startActivity(intent);
                                                //activity.finish();

                                            }
                                        })
                                        .show();

                                btn.setText("Send Email to reset passowrd");

                            } else {
                                btn.setText("Send Email to reset passowrd");

                                spinKit.setVisibility(View.GONE);
                                Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            btn.setText("Send Email to reset passowrd");

                            spinKit.setVisibility(View.GONE);
                            Toast.makeText(activity, "Error", Toast.LENGTH_LONG).show();

                            e.printStackTrace();
                        }
                    } else {
                        btn.setText("Send Email to reset passowrd");
                        spinKit.setVisibility(View.GONE);
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            Toast.makeText(ForgotPassword.this, jObjError.getJSONObject("result").getString("response") , Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(ForgotPassword.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {

                }
            });
        }

    }
    boolean validate()
    {
        if (!Patterns.EMAIL_ADDRESS.matcher(txtEmailAddress.getText().toString()).matches()) {

            Toast.makeText(this, "Enter Correct Email", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

}
