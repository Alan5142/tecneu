package com.tecneu.tecneu.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tecneu.tecneu.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserService {
    private final static String TOKEN = "tecneu.TOKEN";

    public static void login(Context context, String username, String password, OnLoginRequest onLoginRequest) throws JSONException {
        assert (onLoginRequest != null);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = context.getString(R.string.api_url) + "/users/login";

        JSONObject request = new JSONObject();
        request.put("username", username);
        request.put("password", password);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, request,
                response -> {
                    try {
                        sharedPreferences.edit().putString(TOKEN, response.getString("token")).apply();
                        Log.d("t", "Value: " + sharedPreferences.getString(TOKEN, ""));
                        onLoginRequest.onSuccess();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    onLoginRequest.onError();
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        queue.add(jsObjRequest);
    }

    public static void closeSession(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().remove(TOKEN).apply();
    }

    public static boolean isLoggedIn(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.contains(TOKEN);
    }
}
