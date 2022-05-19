package com.bluesoft.barkod.response;

import com.google.gson.annotations.SerializedName;

public class UserResponse {
    @SerializedName("accessToken")
    private String accessToken;
    @SerializedName("userDetails")
    private UserEntity userDetails;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public UserEntity getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserEntity userDetails) {
        this.userDetails = userDetails;
    }
}
