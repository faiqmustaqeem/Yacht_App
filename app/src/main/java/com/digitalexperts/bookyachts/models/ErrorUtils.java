package com.digitalexperts.bookyachts.models;



import com.digitalexperts.bookyachts.customClasses.AppConstants;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;


/**
 * Created by faiq on 17/12/2017.
 */

public class ErrorUtils {

    public static APIError parseError(Response<?> response, Retrofit retrofit) {
        APIError error = null;

        try {
            Converter<ResponseBody, APIError> converter = retrofit.responseBodyConverter(APIError.class, new Annotation[0]);
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return error;
    }
}
