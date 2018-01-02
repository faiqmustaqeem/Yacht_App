package com.digitalexperts.bookyachts.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.gson.Gson;
import com.digitalexperts.bookyachts.R;
import com.digitalexperts.bookyachts.customClasses.AppConstants;
import com.digitalexperts.bookyachts.customClasses.AppController;
import com.digitalexperts.bookyachts.interfaces.WebServices;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.regex.Matcher;
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
 * Created by Codiansoft on 10/28/2017.
 */

public class ContactUsDialog {
    // --> Start binding required views
    Activity activity;

    Dialog dialog;
    @BindView(R.id.spin_kit)
    SpinKitView spinKit;
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etMessage)
    EditText etMessage;
    @BindView(R.id.tvContactEmail)
    TextView tvContactEmail;
    @BindView(R.id.tvContactNumber1)
    TextView tvContactNumber1;

    @BindView(R.id.textView10)
    TextView textView10;

    @BindView(R.id.textView11)
    TextView textView11;

    @BindView(R.id.textView12)
    TextView textView12;

    // <-- end binding required views

    // Constructor
    public ContactUsDialog(Activity activity) {
        this.activity = activity;
        initUIandShow();
        fetchContactDetails();
    }

    private void fetchContactDetails() {
        if (checkValidation()) {

            spinKit.setVisibility(View.VISIBLE);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(AppConstants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            WebServices service = retrofit.create(WebServices.class);

            HashMap<String, String> body = new HashMap<>();

           // body.put("api_secret", AppController.getInstance().getPrefManger().getUserProfile().getAuthKey());
           // Log.e("authkey1",AppController.getInstance().getPrefManger().getUserProfile().getAuthKey());

            service.getContactDetails(body).enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    Log.e("test1", json);
                    // problem , always null in json here
                    //if ( (!json.equals("") && json != null && !json.isEmpty()))
                    if(response.isSuccessful())
                    {
                        Log.e("jsonflag",json);
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            JSONObject resultObject = jsonObject.getJSONObject("result");
                            String status = resultObject.getString("status");

                            String message = resultObject.getString("response");
                            if (status.equals("success")) {
                                JSONObject data = jsonObject.getJSONArray("data").getJSONObject(0);
                                tvContactEmail.setText(data.getString("email"));
                                tvContactNumber1.setText(data.getString("contact_phone"));

                                if(data.getString("email").equals("") && data.getString("contact_phone").equals(""))
                                {
                                    textView10.setVisibility(View.INVISIBLE);
                                    textView11.setVisibility(View.INVISIBLE);
                                    textView12.setVisibility(View.INVISIBLE);

                                }
                                spinKit.setVisibility(View.GONE);
                            } else {
                                textView10.setVisibility(View.INVISIBLE);
                                textView11.setVisibility(View.INVISIBLE);
                                textView12.setVisibility(View.INVISIBLE);
                                spinKit.setVisibility(View.GONE);
                                Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            spinKit.setVisibility(View.GONE);

                            textView10.setVisibility(View.INVISIBLE);
                            textView11.setVisibility(View.INVISIBLE);
                            textView12.setVisibility(View.INVISIBLE);
                           // Toast.makeText(activity, "Error", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    } else {
                        spinKit.setVisibility(View.GONE);
                        textView10.setVisibility(View.INVISIBLE);
                        textView11.setVisibility(View.INVISIBLE);
                        textView12.setVisibility(View.INVISIBLE);
                        Toast.makeText(activity, "json Error", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                  //  Toast.makeText(activity, "failure", Toast.LENGTH_LONG).show();

                }
            });
        }
    }

    // Showing Dialog
    private void initUIandShow() {
        View view = activity.getLayoutInflater().inflate(R.layout.dialog_contact_us, null);
        dialog = new Dialog(activity);

        dialog.setContentView(view);
        ButterKnife.bind(this, view);
        //mergeViews(view);

        try {
            etName.setText(AppController.getInstance().getPrefManger().getUserProfile().getName());
            etEmail.setText(AppController.getInstance().getPrefManger().getUserProfile().getEmail());

        } catch (NullPointerException e) {
            etName.setText("");
            etEmail.setText("");
        }

        dialog.getWindow().getAttributes().windowAnimations = R.style.customDialog;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    }// <-- End of initUIandShow() method

    @OnClick({R.id.tvSubmit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvSubmit:
                submitMessage();
                break;
        }
    }

    private void submitMessage() {
        if (isValidInputs()) {
            spinKit.setVisibility(View.VISIBLE);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(AppConstants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            WebServices service = retrofit.create(WebServices.class);

            HashMap<String, String> body = new HashMap<>();

            body.put("name", etName.getText().toString());
            body.put("email", etEmail.getText().toString());
            body.put("message", etMessage.getText().toString());
            // working fine
            service.submitMessage(body).enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    Log.e("test", json);
                    if (response.isSuccessful()){
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            JSONObject resultObject = jsonObject.getJSONObject("result");
                            String status = resultObject.getString("status");
                            String message = resultObject.getString("response");

                            if (status.equals("success")) {
                                Toast.makeText(activity, "Thank you for your message", Toast.LENGTH_SHORT).show();
                                Toast.makeText(activity, "We will respond to you soon", Toast.LENGTH_SHORT).show();
                                spinKit.setVisibility(View.GONE);
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
        return true;
    }

    private boolean isValidInputs() {
        if (etEmail.getText().toString().equals("")) {
            Toast.makeText(activity, "Enter Email", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if( ! isValidEmail(etEmail.getText().toString()))
        {
            Toast.makeText(activity, "please Enter Correct Email", Toast.LENGTH_SHORT).show();
            etEmail.setText("");
            return false;
        }
        else if (etName.getText().toString().equals("")) {
            Toast.makeText(activity, "Enter Name", Toast.LENGTH_SHORT).show();
            return false;
        } else if (etMessage.getText().toString().equals("")) {
            Toast.makeText(activity, "Enter Message", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!AppConstants.isOnline(activity)) {
            Toast.makeText(activity, "Please Connect Your Internet", Toast.LENGTH_LONG).show();
            // new CustomAlertDialog(this,"Alert","Please Connect Your Internet");
            return false;
        }
        return true;
    }
    public  boolean isValidEmail(String emailStr) {
         final Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }
}
