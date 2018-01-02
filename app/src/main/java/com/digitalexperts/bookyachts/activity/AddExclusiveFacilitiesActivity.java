package com.digitalexperts.bookyachts.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.digitalexperts.bookyachts.R;
import com.digitalexperts.bookyachts.adapter.ExpandableListAdapter;
import com.digitalexperts.bookyachts.customClasses.AppConstants;
import com.digitalexperts.bookyachts.interfaces.ShowDataInterface;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddExclusiveFacilitiesActivity extends AppCompatActivity implements ShowDataInterface {

    @BindView(R.id.expListView)
    ExpandableListView expListView;

    Activity activity;
    @BindView(R.id.btnBack)
    Button btnBack;
    @BindView(R.id.tvTotal)
    TextView tvTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Hide the status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exclusive_facilities);
        ButterKnife.bind(this);

        activity = this;

        // hiding action bar
        getSupportActionBar().hide();

        ExpandableListAdapter listAdapter = new ExpandableListAdapter(activity, AppConstants.listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
        tvTotal.setText("Total Amount of Exclusive Facilities "+AppConstants.subFacilityTotal+ " "+AppConstants.yachtsModel.getCurrency());
    }

    @OnClick(R.id.btnBack)
    public void onClick() {

        activity.finish();
    }

    @Override
    public void showData(String totalAmount)
    {
         tvTotal.setText("Total Amount of Exclusive Facilities "+totalAmount+ " "+AppConstants.yachtsModel.getCurrency());
    }
}
