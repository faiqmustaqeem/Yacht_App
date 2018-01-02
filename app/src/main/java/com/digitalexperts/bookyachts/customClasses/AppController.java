package com.digitalexperts.bookyachts.customClasses;



import android.app.Application;

import com.google.firebase.FirebaseApp;




public class AppController extends Application
{
    private PrefManager pref;

    public  static  int count = 0;

    private static AppController appController;
    private float scale;

    @Override
    public void onCreate()
    {
        super.onCreate();
        FirebaseApp.initializeApp(getApplicationContext());
        appController = this;



    }

    public static synchronized AppController getInstance()
    {
        return appController;
    }


    public PrefManager getPrefManger()
    {
        if (pref == null)
        {
            pref = new PrefManager(this);
        }

        return pref;
    }



}