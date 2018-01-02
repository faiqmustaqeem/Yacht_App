package com.digitalexperts.bookyachts.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.github.ybq.android.spinkit.SpinKitView;
import com.digitalexperts.bookyachts.R;
import com.digitalexperts.bookyachts.adapter.BookingShipsAdapter;
import com.digitalexperts.bookyachts.adapter.HotSlotAdapter;
import com.digitalexperts.bookyachts.models.HotSlotModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShowHotSlotsActivity extends AppCompatActivity {

    @BindView(R.id.rvAllHotSlots)
    ShimmerRecyclerView rvAllHotSlots;
    @BindView(R.id.spin_kit)
    SpinKitView spinKit;
    @BindView(R.id.btnBack)
    Button btnBack;

    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Hide the status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_hot_slots);
        ButterKnife.bind(this);

        activity = this;
        // hiding action bar
        getSupportActionBar().hide();


        RecyclerView.LayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(activity);
        rvAllHotSlots.setLayoutManager(layoutManager);
        HotSlotAdapter adapter = new HotSlotAdapter(activity);
        rvAllHotSlots.setAdapter(adapter);
    }

    @OnClick(R.id.btnBack)
    public void onClick() {

        activity.finish();

    }
}
