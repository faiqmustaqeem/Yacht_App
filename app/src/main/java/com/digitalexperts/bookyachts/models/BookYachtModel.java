package com.digitalexperts.bookyachts.models;

/**
 * Created by hp on 9/7/2017.
 */

public class BookYachtModel
{
   String phoneNumber;
   String startTime;
   String bookingDuration = "2";
   String startDate;



   public String getStartDate() {
      return startDate;
   }

   public void setStartDate(String startDate) {
      this.startDate = startDate;
   }

   public String getDuration() {
      return duration;
   }

   public void setDuration(String duration) {
      this.duration = duration;
   }

   String duration="2";


   public String getPhoneNumber()
   {
      return phoneNumber;
   }

   public void setPhoneNumber(String phoneNumber)
   {
      this.phoneNumber = phoneNumber;
   }

   public String getStartTime()
   {
      return startTime;
   }

   public void setStartTime(String startTime)
   {
      this.startTime = startTime;
   }

   public String getBookingDuration() {
      return bookingDuration;
   }

   public void setBookingDuration(String bookingDuration) {
      this.bookingDuration = bookingDuration;
   }

   public String getFacilities() {
      return facilities;
   }

   public void setFacilities(String facilities) {
      this.facilities = facilities;
   }

   String facilities ;



}
