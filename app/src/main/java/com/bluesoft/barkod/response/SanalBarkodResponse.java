package com.bluesoft.barkod.response;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SanalBarkodResponse {

	@SerializedName("spoolNo")
	private String spoolNo;
	@SerializedName("devreAdi")
	private String devreAdi;
	@SerializedName("duzey")
	private String duzey;

	public String getSpoolNo() {
		return spoolNo;
	}

	public void setSpoolNo(String spoolNo) {
		this.spoolNo = spoolNo;
	}

	public String getDevreAdi() {
		return devreAdi;
	}

	public void setDevreAdi(String devreAdi) {
		this.devreAdi = devreAdi;
	}

	public String getDuzey() {
		return duzey;
	}

	public void setDuzey(String duzey) {
		this.duzey = duzey;
	}

}
