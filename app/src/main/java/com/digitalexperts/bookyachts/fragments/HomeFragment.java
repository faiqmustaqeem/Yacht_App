package com.digitalexperts.bookyachts.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.gson.Gson;
import com.digitalexperts.bookyachts.R;
import com.digitalexperts.bookyachts.activity.PaymentFormActivity;
import com.digitalexperts.bookyachts.activity.ShipDetailsActivity;
import com.digitalexperts.bookyachts.adapter.ShipsAdapter;
import com.digitalexperts.bookyachts.customClasses.AppConstants;
import com.digitalexperts.bookyachts.customClasses.AppController;
import com.digitalexperts.bookyachts.interfaces.WebServices;
import com.digitalexperts.bookyachts.models.YachtsModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment
{


    @BindView(R.id.rvAllYachts)
    ShimmerRecyclerView rvAllYachts;
    @BindView(R.id.spin_kit)
    SpinKitView spinKit;

    ArrayList<YachtsModel> yachtsModelsArrayList = new ArrayList<>();

    public HomeFragment()
    {
        // Required empty public constructor
    }



    @Override
    public void onActivityCreated( Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment, fragment and interfaces
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        try {
            getAllYachts();
           // getActivity().startActivity(new Intent(getActivity(), ShipDetailsActivity.class));
        }
        catch (Exception e)
        {

        }
        return view;
    }
    private void getAllYachts()
    {
        yachtsModelsArrayList.clear();

        if (AppConstants.isOnline(getActivity()))
        {
            spinKit.setVisibility(View.VISIBLE);
            Log.e("test","1");
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(AppConstants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Log.e("test","2");
            WebServices service = retrofit.create(WebServices.class);
            Log.e("test","3");
            HashMap<String, String> body = new HashMap<>();
//            body.put("api_secret", AppController.getInstance().getPrefManger().getUserProfile().getAuthKey());
            Log.e("test","4");
            service.getAllYachts(body).enqueue(new Callback<Object>()
            {

                @Override
                public void onResponse(Call<Object> call, Response<Object> response)
                {
                    Log.e("test","5");
                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    Log.e("test","6");
                    Log.e("test",json.toString());

                    if (!json.equals("") || json != null || !json.isEmpty())
                    {
                        try
                        {
                            Log.e("test","7");
                            JSONObject jsonObject = new JSONObject(json);

                            JSONObject resultObject = jsonObject.getJSONObject("result");

                           String status = resultObject.getString("status");

                            String message = resultObject.getString("response");

                            if (status.equals("success"))
                            {

                                JSONObject dataObject = jsonObject.getJSONObject("data");

                                for(int i =0 ; i< dataObject.getJSONArray("yachts").length(); i++)
                                {
                                    YachtsModel yachtsModel = new YachtsModel();

                                    JSONObject yachtObject = dataObject.getJSONArray("yachts").getJSONObject(i);

                                    yachtsModel.setId(yachtObject.getString("id"));
                                    yachtsModel.setLatitude(yachtObject.getString("latitude"));
                                    yachtsModel.setLocation(yachtObject.getString("location"));
                                    yachtsModel.setLongitude(yachtObject.getString("longitude"));
                                    yachtsModel.setCapacity(yachtObject.getString("capacity"));
                                    yachtsModel.setMax_guest(yachtObject.getString("max_guest"));
                                    yachtsModel.setCurrency(yachtObject.getString("currency"));
                                    yachtsModel.setSize(yachtObject.getString("size"));
                                    yachtsModel.setTripDuration(yachtObject.getString("trip_duration"));
                                    yachtsModel.setPrice(yachtObject.getString("price"));
                                    yachtsModel.setTitle(yachtObject.getString("title"));
                                    yachtsModel.setPicturePath(yachtObject.getString("picture_path"));


                                    yachtsModelsArrayList.add(yachtsModel);

                                }
                                RecyclerView.LayoutManager layoutManager;
                                layoutManager = new LinearLayoutManager(getActivity());
                                rvAllYachts.setLayoutManager(layoutManager);
                                ShipsAdapter adapter = new ShipsAdapter(getActivity(),yachtsModelsArrayList);
                                rvAllYachts.setAdapter(adapter);


                                spinKit.setVisibility(View.GONE);

                            } else {
                                spinKit.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            spinKit.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();

                            e.printStackTrace();
                        }
                    } else {
                        spinKit.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Internet Error", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t)
                {

                }
            });
        } else
        {

        }

    }

}
