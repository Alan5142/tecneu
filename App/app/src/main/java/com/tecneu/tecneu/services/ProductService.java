package com.tecneu.tecneu.services;

import android.content.Context;

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

public class ProductService {
    public static void changeStock(Context context, String meliId, int newStock, OnRequest onRequest) throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = context.getString(R.string.api_url) + "/products";

        JSONObject request = new JSONObject();
        request.put("id", meliId);
        request.put("stock", newStock);

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
}
