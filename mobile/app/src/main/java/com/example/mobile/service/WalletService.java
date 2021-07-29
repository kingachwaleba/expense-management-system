package com.example.mobile.service;

import android.content.Context;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.mobile.config.MySingleton;
import com.example.mobile.model.User;
import com.example.mobile.model.Wallet;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WalletService {
    public static final String BASE_URL = "http://192.168.0.31:8080/";
    Context context;


    public WalletService(Context context) {
        this.context = context;
    }

    public interface VolleyResponseListener{
        void onError(String message);

        void onResponse(List<Wallet> wallets);
    }

    public void getAll(String accesstoken, VolleyResponseListener volleyResponseListener) {
        String url = BASE_URL + "wallets";

        List<Wallet> wallets = new ArrayList<>();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url,null, response -> {
            try {
            for(int i = 0; i < response.length(); i++){

                    String name = "",owner = "",category = "", login = "", status = "";
                    List<User> members = new ArrayList<>();
                    name = response.getJSONObject(i).getString("name");
                    category = response.getJSONObject(i).getJSONObject("walletCategory").getString("name");
                    JSONArray membersJson = response.getJSONObject(i).getJSONArray("walletUserSet");
                    for(int j = 0; j < membersJson.length(); j++){
                        login = membersJson.getJSONObject(j).getJSONObject("user").getString("login");
                        status =  membersJson.getJSONObject(j).getJSONObject("userStatus").getString("name");
                        if(status.equals("właściciel"))
                            owner = login;
                        members.add(new User(login,status));

                    }
                    wallets.add(new Wallet(name,owner,category,members, members.size()));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            volleyResponseListener.onResponse(wallets);
        }, error -> volleyResponseListener.onError("Something went wrong")) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

}
