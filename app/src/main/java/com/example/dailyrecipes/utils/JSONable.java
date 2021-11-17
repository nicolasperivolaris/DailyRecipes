package com.example.dailyrecipes.utils;

import org.json.JSONException;
import org.json.JSONObject;

public interface JSONable<T> {
    JSONObject convertToJSON() throws JSONException;

}
