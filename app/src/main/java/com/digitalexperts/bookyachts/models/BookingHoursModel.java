package com.digitalexperts.bookyachts.models;

/**
 * Created by Codiansoft on 10/24/2017.
 */

public class BookingHoursModel {
    String isAlreadyBooked, isSelected, timeText;

    public BookingHoursModel(String isAlreadyBooked, String isSelected, String timeText) {
        this.isAlreadyBooked = isAlreadyBooked;
        this.isSelected = isSelected;
        this.timeText = timeText;
    }

    public String getIsAlreadyBooked() {
        return isAlreadyBooked;
    }

    public void setIsAlreadyBooked(String isAlreadyBooked) {
        this.isAlreadyBooked = isAlreadyBooked;
    }

    public String getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(String isSelected) {
        this.isSelected = isSelected;
    }

    public String getTimeText() {
        return timeText;
    }

    public void setTimeText(String timeText) {
        this.timeText = timeText;
    }
}
