package com.tecneu.tecneu.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Pair;

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
import com.tecneu.tecneu.models.DatabaseProduct;
import com.tecneu.tecneu.models.Order;
import com.tecneu.tecneu.models.OrderInfo;
import com.tecneu.tecneu.models.Provider;
import com.tecneu.tecneu.models.ProviderProduct;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderService {
    public static void createOrder(Context context, ArrayList<Pair<Provider, ProviderProduct>> products, OnRequest onRequest) throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = context.getString(R.string.api_url) + "/orders";

        JSONObject request = new JSONObject();
        JSONArray productsArray = new JSONArray();
        for (Pair<Provider, ProviderProduct> product : products) {
            JSONObject productJson = new JSONObject();
            Provider provider = product.first;
            ProviderProduct providerProduct = product.second;
            productJson.put("providerId", provider.id);
            productJson.put("companyName", provider.companyName);
            productJson.put("idProduct", providerProduct.idProduct);
            productJson.put("price", providerProduct.price);
            productJson.put("name", providerProduct.name);
            productJson.put("quantity", providerProduct.stock);
            productsArray.put(productJson);
        }
        request.put("products", productsArray);


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

    public static void getAllOrders(Context context, OnRequest onRequest) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = context.getString(R.string.api_url) + "/orders";

        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy HH:mm:ss").create();

                    ArrayList<Order> users = gson.fromJson(response, new TypeToken<ArrayList<Order>>() {
                    }.getType());
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

    public static void modifyProvider(Context context, Provider provider, List<DatabaseProduct> productList, OnRequest onRequest) throws JSONException {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = context.getString(R.string.api_url) + "/providers/" + provider.id;
        Gson gson = new Gson();
        JSONObject object = new JSONObject(gson.toJson(provider));

        JSONArray array = new JSONArray();
        for (DatabaseProduct p: productList) {
            JSONObject o = new JSONObject(gson.toJson(p));
            array.put(o);
        }
        object.put("products", array);

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

    public static void deleteOrder(Context context, int orderId, OnRequest onRequest) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = context.getString(R.string.api_url) + "/orders/" + orderId; // xd providerss

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

    public static void getOrderProducts(Context context, int orderId, OnRequest onRequest) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = context.getString(R.string.api_url) + "/orders/" + orderId + "/products";

        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy HH:mm:ss").create();

                    ArrayList<OrderInfo> products = gson.fromJson(response, new TypeToken<ArrayList<OrderInfo>>() {
                    }.getType());
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

    public static void deleteOrderProduct(Context context, int productId, OnRequest onRequest) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = context.getString(R.string.api_url) + "/orders/products/" + productId;

        StringRequest jsObjRequest = new StringRequest(Request.Method.DELETE, url,
                response -> onRequest.onSuccess(null),
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

    public static void modifyOrderStatus(Context context, int orderId, OnRequest onRequest) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = context.getString(R.string.api_url) + "/orders/" + orderId;

        StringRequest jsObjRequest = new StringRequest(Request.Method.PUT, url,
                response -> onRequest.onSuccess(null),
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
}
