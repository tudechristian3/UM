package com.ultramega.shop.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by User-PC on 10/23/2017.
 */

public class AmazonCredentials {
    @SerializedName("APIKey")
    @Expose
    private String APIKey;
    @SerializedName("APISecretKey")
    @Expose
    private String APISecretKey;

    public AmazonCredentials(String APIKey, String APISecretKey) {
        this.APIKey = APIKey;
        this.APISecretKey = APISecretKey;
    }

    public String getAPIKey() {
        return APIKey;
    }

    public String getAPISecretKey() {
        return APISecretKey;
    }
}
