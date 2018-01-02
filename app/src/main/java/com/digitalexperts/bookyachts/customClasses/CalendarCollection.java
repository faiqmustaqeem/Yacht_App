package com.digitalexperts.bookyachts.customClasses;

/**
 * Created by hp on 5/15/2017.
 */

import java.util.ArrayList;

public class CalendarCollection {
    public String date="";
    public String event_message="";

    public static ArrayList<CalendarCollection> date_collection_arr = new ArrayList(1);
    public CalendarCollection(String date, String event_message){

        this.date=date;
        this.event_message=event_message;

    }

}
