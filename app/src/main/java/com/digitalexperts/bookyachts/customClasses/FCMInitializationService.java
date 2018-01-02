package com.digitalexperts.bookyachts.customClasses;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class FCMInitializationService extends FirebaseInstanceIdService
{
    @Override
    public void onTokenRefresh()
    {
        String fcmToken = FirebaseInstanceId.getInstance().getToken();
        Log.e("test",fcmToken) ;
        AppController.getInstance().getPrefManger().setValue("firebaseId", fcmToken);
    }
}