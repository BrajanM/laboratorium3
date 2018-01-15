package com.laboratorium.wilczek.laboratorium3;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Sylwia on 1/6/2018.
 */

public class DogListResponse {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private List<String> message;

    public String getStatus() {
        return status;
    }

    public List<String> getMessage() {
        return message;
    }
}
