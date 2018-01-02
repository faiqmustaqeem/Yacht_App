package com.digitalexperts.bookyachts.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.digitalexperts.bookyachts.R;
import com.digitalexperts.bookyachts.customClasses.AppConstants;
import com.digitalexperts.bookyachts.customClasses.AppController;
import com.digitalexperts.bookyachts.interfaces.WebServices;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StatusActivity extends AppCompatActivity {

	String status;
	SpinKitView spinKit;

    Activity activity;
	@Override
	public void onCreate(Bundle bundle) {

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		super.onCreate(bundle);
		setContentView(R.layout.activity_status);
		getSupportActionBar().setTitle("Book Yacht");
		Intent mainIntent = getIntent();
        activity=this;
		 status=mainIntent.getStringExtra("transStatus");

		spinKit=(SpinKitView)findViewById(R.id.spin_kit);



		if(status.equals("Transaction Successful!"))
		{
			changeStatus();
		}
        else if(status.equals("Transaction Declined!"))
        {
            MaterialStyledDialog materialStyledDialog= new MaterialStyledDialog.Builder(StatusActivity.this)

                    .setTitle("Sorry")
                    .setDescription("your Transaction has been declined , you will also receive Email with Details ")
                    .setPositiveText("OK")
					.setCancelable(false)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            AppConstants.order_id="0";
                            AppConstants.remainingAmount=0;
                            Intent i=new Intent(activity,DashboardActivity.class);
                            startActivity(i);
                            activity.finish();
                        }
                    })
                    .show();
			materialStyledDialog.setCanceledOnTouchOutside(false);
			materialStyledDialog.setCancelable(false);


        }
        else if(status.equals("Transaction Cancelled!"))
        {
            MaterialStyledDialog materialStyledDialog= new MaterialStyledDialog.Builder(StatusActivity.this)

                    .setTitle("Sorry")
                    .setDescription("your Transaction has been Canclled , you will also receive Email with Details ")
                    .setPositiveText("OK")
					.setCancelable(false)
					.onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            AppConstants.order_id="0";
                            AppConstants.remainingAmount=0;
                            Intent i=new Intent(activity,DashboardActivity.class);
                            startActivity(i);
                            activity.finish();
                        }
                    })
                    .show();
			materialStyledDialog.setCanceledOnTouchOutside(false);
			materialStyledDialog.setCancelable(false);


		}
        else if(status.equals("Status Not Known!"))
        {
            MaterialStyledDialog materialStyledDialog= new MaterialStyledDialog.Builder(StatusActivity.this)

                    .setTitle("Sorry")
                    .setDescription("your Transaction has been Canclled , you will also receive Email with Details ")
                    .setPositiveText("OK")
					.setCancelable(false)
					.onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            AppConstants.order_id="0";
                            AppConstants.remainingAmount=0;
                            Intent i=new Intent(activity,DashboardActivity.class);
                            startActivity(i);
                            activity.finish();
                        }
                    })

                    .show();
			materialStyledDialog.setCanceledOnTouchOutside(false);
			materialStyledDialog.setCancelable(false);
        }
//		TextView tv4 = (TextView) findViewById(R.id.textView1);
//		tv4.setText(mainIntent.getStringExtra("transStatus"));


	}
	void changeStatus()
	{
		spinKit.setVisibility(View.VISIBLE);


		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(AppConstants.BASE_URL)
				.addConverterFactory(GsonConverterFactory.create())
				.build();

		WebServices service = retrofit.create(WebServices.class);

		HashMap<String, String> body = new HashMap<>();


		body.put("order_id", AppConstants.order_id);
        body.put("remaining_amount", String.valueOf(AppConstants.remainingAmount));


		Log.e("body_payment",body.toString());

		service.statusChange(body).enqueue(new Callback<Object>() {
			@Override
			public void onResponse(Call<Object> call, Response<Object> response) {

				Gson gson = new Gson();
				String json = gson.toJson(response.body());


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
							MaterialStyledDialog materialStyledDialog= new MaterialStyledDialog.Builder(StatusActivity.this)

									.setTitle("Congratulation")
									.setDescription("your Order ID is "+AppConstants.order_id+". your order has been confirmed , you will also receive Confirmation Email with Details ")
									.setPositiveText("OK")
									.setCancelable(false)

									.onPositive(new MaterialDialog.SingleButtonCallback() {
										@Override
										public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    AppConstants.order_id="0";
                                            AppConstants.remainingAmount=0;
                                            Intent i=new Intent(activity,DashboardActivity.class);
                                            startActivity(i);
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


	public void showToast(String msg) {
		Toast.makeText(this, "Toast: " + msg, Toast.LENGTH_LONG).show();
	}
} 