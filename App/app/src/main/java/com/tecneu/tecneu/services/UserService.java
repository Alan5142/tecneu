package com.tecneu.tecneu.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.JsonReader;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.tecneu.tecneu.R;
import com.tecneu.tecneu.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserService {
    private final static String TOKEN = "tecneu.TOKEN";
    private final static String USER_TYPE = "tecneu.USER_TYPE";

    public static void login(Context context, String username, String password, OnRequest onLoginRequest) throws JSONException {
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
                        sharedPreferences.edit() // guardar token y tipo de usuario
                                .putString(TOKEN, response.getString("token"))
                                .putString(USER_TYPE, response.getString("userType")).apply();
                        Log.d("t", "Value: " + sharedPreferences.getString(TOKEN, ""));
                        onLoginRequest.onSuccess(null);
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

    public static String getToken(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(TOKEN, "");
    }

    public static String getUserType(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(USER_TYPE, "");
    }

    public static void getAllUsers(Context context, OnRequest onRequest){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = context.getString(R.string.api_url) + "/users";

        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    Gson gson = new Gson();

                    ArrayList<User> users = gson.fromJson(response, new TypeToken<ArrayList<User>>(){}.getType());
                    onRequest.onSuccess(users);
                },
                error -> onRequest.onError()) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + UserService.getToken(context));
                return headers;
            }

        };
        queue.add(jsObjRequest);
    }

    public static void createUser(Context context, String username, String password, String userType, String name, String surname, OnRequest onRequest) throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = context.getString(R.string.api_url) + "/users";
        JSONObject request = new JSONObject();
        request.put("username", username);
        request.put("password", password);
        request.put("userType", userType);
        request.put("surnames", surname);
        request.put("names", name);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, request,
                response -> onRequest.onSuccess(null),
                error -> onRequest.onError()) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                //headers.put("Authorization", "Bearer " + UserService.getToken(context));
                return headers;
            }
        };
        queue.add(jsObjRequest);
    }

    public static void modifyUser(Context context, User user, OnRequest onRequest) throws JSONException {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = context.getString(R.string.api_url) + "/users/" + user.id;
        Gson gson = new Gson();
        JSONObject object = new JSONObject(gson.toJson(user));

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.PUT, url, object,
                response -> onRequest.onSuccess(null),
                error -> onRequest.onError()) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + UserService.getToken(context));
                return headers;
            }

        };
        queue.add(jsObjRequest);
    }

    public static void deleteUser(Context context, int userId, OnRequest onRequest) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = context.getString(R.string.api_url) + "/users/" + userId;

        StringRequest jsObjRequest = new StringRequest(Request.Method.DELETE, url,
                response -> {
                    onRequest.onSuccess(null);
                },
                error -> onRequest.onError()) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + UserService.getToken(context));
                return headers;
            }

        };
        queue.add(jsObjRequest);
    }
}
