package com.digitalexperts.bookyachts.models;

/**
 * Created by faiq on 17/12/2017.
 */

public class APIError {

    private int statusCode;
    private String message;

    public APIError() {
    }

    public int status() {
        return statusCode;
    }

    public String message() {
        return message;
    }
}