package com.digitalexperts.bookyachts.customClasses;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.digitalexperts.bookyachts.models.UserModel;


public class PrefManager
{


    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    private static Gson GSON = new Gson();
    // Sharedpref file name
    private static final String PREF_NAME = "com.project.yachtapp";


    private static final String KEY_USER = "user";


    public PrefManager(Context context)
    {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);

    }



    public void setGuide(String type)
    {
        editor = pref.edit();

        editor.putString("guide", type);

        // commit changes
        editor.commit();
    }

    public String getGuide()
    {
        return pref.getString("guide", "");
    }



    public void setUserProfile(UserModel obj)
    {

        pref.edit().putString(KEY_USER,GSON.toJson(obj)).apply();

    }

    public  void clear()
    {
        pref.edit().remove(KEY_USER).commit();

    }
    public UserModel getUserProfile()
    {
            String gson = pref.getString(KEY_USER,"");
            if(gson.isEmpty())return null;
            return GSON.fromJson(gson,UserModel.class);

    }

    public void setValue( String key , Object value)
    {

        SharedPreferences.Editor edit = pref.edit();

        if (value instanceof Boolean)
            edit.putBoolean(key, (boolean) value);
        else if (value instanceof Integer)
            edit.putInt(key, (int) value);
        else if (value instanceof String)
            edit.putString(key, (String) value);

        edit.commit();

    }

    public Object getValue(String key,Object value)
    {
        if (value instanceof Boolean)
            return pref.getBoolean(key, (boolean) value);
        else if (value instanceof Integer)
            return pref.getInt(key, (int) value);
        else if (value instanceof String)
            return pref.getString(key, (String) value);

        else
            return null;
    }

    public void setNotificationValue( String key ,int value)
    {

        pref.edit().putInt(key,value).apply();

    }

    public int getNotificationValue(String key)
    {
        int value = pref.getInt(key,0);

        return value;


    }



}