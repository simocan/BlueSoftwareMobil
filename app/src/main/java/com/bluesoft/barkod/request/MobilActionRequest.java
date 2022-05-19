package com.bluesoft.barkod.request;

public class MobilActionRequest {

    private int personelId;
    private String sanalBarkod;
    private String action;

    public int getPersonelId() {
        return personelId;
    }

    public void setPersonelId(int personelId) {
        this.personelId = personelId;
    }

    public String getSanalBarkod() {
        return sanalBarkod;
    }

    public void setSanalBarkod(String sanalBarkod) {
        this.sanalBarkod = sanalBarkod;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
