package com.bluesoft.barkod.response;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AkisHavuz {

	@SerializedName("id")
	private Long id;
	@SerializedName("name")
	private String name;
	@SerializedName("priorty")
	private int priorty;
	@SerializedName("ack")
	private String ack;
	@SerializedName("state")
	private String state;
	@SerializedName("tip")
	private String tip;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPriorty() {
		return priorty;
	}

	public void setPriorty(int priorty) {
		this.priorty = priorty;
	}

	public String getAck() {
		return ack;
	}

	public void setAck(String ack) {
		this.ack = ack;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}
}
