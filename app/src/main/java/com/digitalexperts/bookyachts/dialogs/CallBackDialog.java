package com.digitalexperts.bookyachts.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.gson.Gson;
import com.digitalexperts.bookyachts.R;
import com.digitalexperts.bookyachts.customClasses.AppConstants;
import com.digitalexperts.bookyachts.interfaces.WebServices;

import org.json.JSONException;
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

/**
 * Created by Irtiza on 3/26/2017.
 */

public class CallBackDialog {

    // --> Start binding required views
    Activity activity;
    @BindView(R.id.txtContactNumber)
    EditText txtContactNumber;
    @BindView(R.id.btnSend)
    Button btnSend;
    Dialog dialog;
    @BindView(R.id.spin_kit)
    SpinKitView spinKit;

    // <-- end binding required views


    // Constructor
    public CallBackDialog(Activity activity) {
        this.activity = activity;
        showChangePasswordDialog();
    }
    
    // Showing Dialog
    private void showChangePasswordDialog() {

        View view = activity.getLayoutInflater().inflate(R.layout.dialog_callback, null);
        dialog = new Dialog(activity);
        dialog.setContentView(view);
        ButterKnife.bind(this, view);
        //mergeViews(view);


        dialog.getWindow().getAttributes().windowAnimations = R.style.customDialog;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }// <-- End of showChangePasswordDialog() method

    @OnClick(R.id.btnSend)
    public void onClick() {
        sendCallNumber();

    }


    private void sendCallNumber() {
        if (checkValidation()) {

            spinKit.setVisibility(View.VISIBLE);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(AppConstants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            WebServices service = retrofit.create(WebServices.class);


            HashMap<String, String> body = new HashMap<>();

            body.put("number", txtContactNumber.getText().toString());

            service.callBack(body).enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    Log.e("test",json);
                    if (response.isSuccessful()) {
                        try {
                            Log.e("test","1");
                            JSONObject jsonObject = new JSONObject(json);
                            Log.e("test","2");
                            JSONObject resultObject = jsonObject.getJSONObject("result");
                            String status = resultObject.getString("status");
                            Log.e("test","3");
                            String message = resultObject.getString("response");
                            Log.e("test","44");
                            if (status.equals("success")) {
                                Log.e("test","5");
                                spinKit.setVisibility(View.GONE);
                                Log.e("test","6");
                                Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
                                Log.e("test","7");
                                dialog.dismiss();
                            } else {
                                spinKit.setVisibility(View.GONE);
                                Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            spinKit.setVisibility(View.GONE);
                            Toast.makeText(activity, "Error", Toast.LENGTH_LONG).show();

                            e.printStackTrace();
                        }
                    } else {
                        spinKit.setVisibility(View.GONE);
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            Toast.makeText(activity, jObjError.getJSONObject("result").getString("response").toString() , Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {

                }
            });
        }
    }// <-- End of login() method

    //
//
    private boolean checkValidation() {

        if (!AppConstants.isOnline(activity)) {
            Toast.makeText(activity, "Please Connect Your Internet", Toast.LENGTH_LONG).show();
            // new CustomAlertDialog(this,"Alert","Please Connect Your Internet");
            return false;
        }

        if (txtContactNumber.getText().toString().trim().length() == 0) {

            Toast.makeText(activity, "Enter Contact", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


}

