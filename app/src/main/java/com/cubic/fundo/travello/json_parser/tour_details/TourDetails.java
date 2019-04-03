package com.cubic.fundo.travello.json_parser.tour_details;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TourDetails {

    @SerializedName("data")
    @Expose
    private List<TourDetailsData> data ;
    @SerializedName("next_url")
    @Expose
    private String nextUrl;

    public List<TourDetailsData> getData() {
        return data;
    }

    public void setData(List<TourDetailsData> data) {
        this.data = data;
    }

    public String getNextUrl() {
        return nextUrl;
    }

    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }

}