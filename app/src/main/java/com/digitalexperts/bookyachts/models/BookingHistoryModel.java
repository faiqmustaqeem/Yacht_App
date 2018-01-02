package com.digitalexperts.bookyachts.models;

/**
 * Created by hp on 9/7/2017.
 */

public class BookingHistoryModel
{
    private String yachtId ;
    private String bookingDate ;
    private String bookingTime;
    private String duration;
    private String discount;
    private String subTotal;
    private String totalAmount;
    private String PaymentType;
    private String paymentStatus;
    private String bookingStatus;

    private String yachtTitle;
    private String amountPaid;
    private String amountRemaining;

    public String getYachtId() {
        return yachtId;
    }

    public void setYachtId(String yachtId) {
        this.yachtId = yachtId;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getBookingTime()
    {
        return bookingTime;
    }

    public void setBookingTime(String bookingTime)
    {
        this.bookingTime = bookingTime;
    }

    public String getDuration()
    {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDiscount()
    {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentType() {
        return PaymentType;
    }

    public void setPaymentType(String paymentType) {
        PaymentType = paymentType;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getFacilities() {
        return facilities;
    }

    public void setFacilities(String facilities) {
        this.facilities = facilities;
    }

    private String facilities;

    public String getYachtTitle() {
        return yachtTitle;
    }

    public void setYachtTitle(String yachtTitle) {
        this.yachtTitle = yachtTitle;
    }

    public String getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(String amountPaid) {
        this.amountPaid = amountPaid;
    }

    public String getAmountRemaining() {
        return amountRemaining;
    }

    public void setAmountRemaining(String amountRemaining) {
        this.amountRemaining = amountRemaining;
    }
}
