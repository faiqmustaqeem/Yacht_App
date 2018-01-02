package com.digitalexperts.bookyachts.models;

import com.digitalexperts.bookyachts.customClasses.AppConstants;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by faiq on 17/12/2017.
 */

public class ServiceGenerator {
    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(AppConstants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());
    private static OkHttpClient httpClient = new OkHttpClient();
    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = builder.client(httpClient).build();
        return retrofit.create(serviceClass);
    }
}