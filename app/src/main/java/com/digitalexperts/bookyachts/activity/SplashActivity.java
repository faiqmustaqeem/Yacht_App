package com.digitalexperts.bookyachts.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.digitalexperts.bookyachts.R;

public class SplashActivity extends AppCompatActivity
{

    Activity activity;

    // -->Start onCreate() method
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Hide the status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        activity = this;
//        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//        String DEVICE_COUNTRY = manager.getNetworkCountryIso();
//        Log.e("test",DEVICE_COUNTRY);

        // hiding action bar
       getSupportActionBar().hide();

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
//                if(AppController.getInstance().getPrefManger().getUserProfile() == null)
//                {
//                    startActivity(new Intent(activity,LoginActivity.class));
//                }
//                else
//                {
                    startActivity(new Intent(activity,DashboardActivity.class));
               // }

                finish();
            }
        },2000);


    }// <--End onCreate() method
}
