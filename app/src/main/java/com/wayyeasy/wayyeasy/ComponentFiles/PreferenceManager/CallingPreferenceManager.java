package com.wayyeasy.wayyeasy.ComponentFiles.PreferenceManager;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.wayyeasy.wayyeasy.ComponentFiles.Constants.Constants;
import com.wayyeasy.wayyeasy.Models.OnlineCalls.call_response_model;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CallingPreferenceManager {

    private SharedPreferences sharedPreferences;

    private String callList = "callList";


    public CallingPreferenceManager() {
    }

    public CallingPreferenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences(Constants.KEY_PREFERENCE_CALL, Context.MODE_PRIVATE);
    }

    public void putBoolean(String key, Boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public Boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, null);
    }

    public void addCallList(List<call_response_model> list) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(callList, new Gson().toJson(list));
        editor.apply();
    }

    public List<call_response_model> getCallList() {
        Type type = new TypeToken<ArrayList<call_response_model>>() {}.getType();
        List<call_response_model> list = new Gson().fromJson(sharedPreferences.getString(callList, null), type);
        return list;
    }

    public void clearPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().commit();
        editor.apply();
    }
}
