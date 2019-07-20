package com.tecneu.tecneu.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tecneu.tecneu.R;
import com.tecneu.tecneu.models.Provider;
import com.tecneu.tecneu.models.ProviderProduct;
import com.tecneu.tecneu.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProviderService
{
    public static void createProvider(Context context, String company, String name, String email, long phoneNumber, OnRequest onRequest) throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = context.getString(R.string.api_url) + "/providers";

        JSONObject request = new JSONObject();
        request.put("company", company);
        request.put("name", name);
        request.put("email", email);
        request.put("phoneNumber", phoneNumber);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, request,
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

    public static void getAllProviders(Context context, OnRequest onRequest) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = context.getString(R.string.api_url) + "/providers";

        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

                    ArrayList<Provider> users = gson.fromJson(response, new TypeToken<ArrayList<Provider>>(){}.getType());
                    onRequest.onSuccess(users);
                },
                error -> onRequest.onError()) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + UserService.getToken(context));
                return headers;
            }
        };
        queue.add(jsObjRequest);
    }

    public static void modifyProvider(Context context, Provider provider, List<ProviderProduct> selectedProducts, List<ProviderProduct> productsToDelete, OnRequest onRequest) throws JSONException {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = context.getString(R.string.api_url) + "/providers/" + provider.id;
        Gson gson = new Gson();
        JSONObject object = new JSONObject(gson.toJson(provider));
        JSONArray selectedProductsJson = new JSONArray(gson.toJson(selectedProducts, new TypeToken<List<ProviderProduct>>() {}.getType()));
        JSONArray nonselectedProductsJson = new JSONArray(gson.toJson(productsToDelete, new TypeToken<List<ProviderProduct>>() {}.getType()));

        object.put("selectedProducts", selectedProductsJson);
        object.put("productsToDelete", nonselectedProductsJson);

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

    public static void deleteProvider(Context context, int providerId, OnRequest onRequest) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = context.getString(R.string.api_url) + "/providers/" + providerId; // xd providerss

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

    public static void getProviderProducts(Context context, String providerName, OnRequest onRequest) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = context.getString(R.string.api_url) + "/providers/" + providerName + "/products"; // xd providerss

        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    Gson gson = new GsonBuilder().create();

                    ArrayList<ProviderProduct> products = gson.fromJson(response, new TypeToken<ArrayList<ProviderProduct>>(){}.getType());
                    onRequest.onSuccess(products);
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
