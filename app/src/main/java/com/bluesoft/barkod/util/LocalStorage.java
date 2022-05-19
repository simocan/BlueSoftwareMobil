package com.bluesoft.barkod.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;

import com.bluesoft.barkod.request.UserRequest;
import com.bluesoft.barkod.response.UserEntity;
import com.bluesoft.barkod.response.UserResponse;
import com.google.gson.Gson;

public class LocalStorage {
    public static final String MyPREFERENCES = "MyPrefs" ;

    public static void logout(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
    }


    private static void saveLocalStorage(Context context,String key ,String data) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, data);
        editor.commit();
    }

    public static void saveLoginUserData(Context context, UserResponse response,UserRequest request) {
        Gson gson = new Gson();
        saveLocalStorage(context,"token",response.getAccessToken());
       // saveLocalStorage(context,"userDetail",gson.toJson(response.getUserDetails()));
        saveLocalStorage(context,"userDetailId",response.getUserDetails().getId());
        saveLocalStorage(context,"request",gson.toJson(request));
    }

    public static int getUserId(Context context) {
        Gson gson = new Gson();
        SharedPreferences preferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String user = preferences.getString("userDetailId", null);
        return Integer.valueOf(user);
    }

    public static UserEntity getUserDetails(Context context) {
        Gson gson = new Gson();
        SharedPreferences preferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String user = preferences.getString("userDetail", null);
        if(user==null) return new UserEntity();
        return gson.fromJson(user, UserEntity.class);
    }

    public static UserRequest getUserRequest(Context context) {
        Gson gson = new Gson();
        SharedPreferences preferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String user = preferences.getString("request", null);
        if(user==null) return new UserRequest();
        return gson.fromJson(user, UserRequest.class);
    }

    public static String getUserToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return preferences.getString("token", null);
    }

    public static String getServerIp(Context context) {
        SharedPreferences preferences =PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("Server_ip", null);
    }

    public static String getServiceUrl(Context context) {
        return   "http://"+getServerIp(context)+":8085/blue/";
    }

}
