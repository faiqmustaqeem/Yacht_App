package com.digitalexperts.bookyachts.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.digitalexperts.bookyachts.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SuccessActivity extends AppCompatActivity {

   // @BindView(R.id.btn_reset_yes)
  //  Button btn_yes;

   // @BindView(R.id.btn_reset_no)
   // Button btn_no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

     //   ButterKnife.bind(this);

//        btn_yes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//
//        btn_no.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
    }
}
