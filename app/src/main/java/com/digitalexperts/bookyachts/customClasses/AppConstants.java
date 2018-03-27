package com.digitalexperts.bookyachts.customClasses;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.digitalexperts.bookyachts.models.BookYachtModel;
import com.digitalexperts.bookyachts.models.HotSlotModel;
import com.digitalexperts.bookyachts.models.SubFacilityModel;
import com.digitalexperts.bookyachts.models.YachtsModel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Irtiza on 7/29/2017.
 */
public class AppConstants {
//    public static final String BASE_URL = "https://bookyachts.ae/googleYacht/api/";
    public static final String BASE_URL = "https://bookyachts.ae/googleYacht/api/";


    public static YachtsModel yachtsModel = new YachtsModel();

    public static BookYachtModel bookYachtModel = new BookYachtModel();

    public static ArrayList<HotSlotModel> yachtsHotSlotsArrayList = new ArrayList();

    public static ArrayList<String> selectedFacilities = new ArrayList();

    public static float subFacilityTotal = 0.0f;

    public static HashMap<String, ArrayList<SubFacilityModel>> listDataChild;

    public static String inclusiveFacilities = "";
    public static String exclusiveFacilities = "";
    public static double discountAmount = 0.0;
    public static double amountPayingNow = 0.0;
    public static double totalAmount = 0.0;
    public static double remainingAmount = 0.0;
    public static int duration = 0;

    public static int indexOfHotSlot;
    public static double discountPercentage = 0;

    public static String bookingDate = "";
    public static int bookingStartTimeIndex;
    public static int bookingDuration;
    public static String bookingStartTime;
    public static boolean pay20Percent;
    public static String order_id;
 public static  String no_of_guests;
    public static String accessCode = "AVUA02EL69BY16AUYB";
    public static String merchantId = "44675";
    public static String currency = "AED";
    public static String amount = ""; // from amount paying now
    public static String rsaKeyUrl = "https://bookyachts.ae/appresponse/GetRSA.php";
    public static String redirectUrl = "https://bookyachts.ae/appresponse/ccavResponseHandler.php";
    public static String cancelUrl = "https://bookyachts.ae/appresponse/ccavResponseHandler.php";

    public static boolean isOnline(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.isConnected())
            return true;
        return false;
    }
}
