package com.digitalexperts.bookyachts.models;

/**
 * Created by Codiansoft on 10/28/2017.
 */

public class HotDealsModel {
    String id, title, description;

    public HotDealsModel(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}