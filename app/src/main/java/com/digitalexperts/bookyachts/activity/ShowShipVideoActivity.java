package com.digitalexperts.bookyachts.activity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.github.rtoshiro.view.video.FullscreenVideoLayout;
import com.github.ybq.android.spinkit.SpinKitView;
import com.digitalexperts.bookyachts.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowShipVideoActivity extends AppCompatActivity {

    @BindView(R.id.spin_kit)
    SpinKitView spinKit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide the status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_show_ship);
        ButterKnife.bind(this);

        // hiding action bar
        getSupportActionBar().hide();


        Bundle bundle = getIntent().getExtras();

        spinKit.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                spinKit.setVisibility(View.GONE);
               // Toast.makeText(ShowShipVideoActivity.this, "Please Wait....", Toast.LENGTH_LONG).show();
            }
        },10000);

        FullscreenVideoLayout videoLayout;

        videoLayout = (FullscreenVideoLayout) findViewById(R.id.videoview);
        videoLayout.setActivity(this);

        Uri videoUri = Uri.parse(bundle.getString("url"));
        try {
            videoLayout.setVideoURI(videoUri);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
