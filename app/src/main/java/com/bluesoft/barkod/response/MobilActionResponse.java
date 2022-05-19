package com.bluesoft.barkod.response;

import com.google.gson.annotations.SerializedName;

public class MobilActionResponse {

    @SerializedName("actionMessage")
    private String actionMessage;
    @SerializedName("code")
    private int code;
    @SerializedName("actionCode")
    private String actionCode;

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public String getActionMessage() {
        return actionMessage;
    }

    public void setActionMessage(String actionMessage) {
        this.actionMessage = actionMessage;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
