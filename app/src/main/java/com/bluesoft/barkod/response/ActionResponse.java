package com.bluesoft.barkod.response;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ActionResponse {

	@SerializedName("actionList")
	private List<String> actionList;
	@SerializedName("message")
	private String message;z
	@SerializedName("code")
	private int code;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public List<String> getActionList() {
		return actionList;
	}

	public void setActionList(List<String> actionList) {
		this.actionList = actionList;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
