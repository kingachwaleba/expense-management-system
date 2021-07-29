package com.example.mobile.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.mobile.LoginActivity;
import com.example.mobile.MainActivity;
import com.example.mobile.RegistrationActivity;
import com.example.mobile.config.MySingleton;
import com.example.mobile.config.SessionManager;
import com.example.mobile.model.LoginForm;
import com.example.mobile.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class UserService {
    public static final String BASE_URL = "http://192.168.0.31:8080/";
    Context context;

    // Session Manager Class
    SessionManager session;


    public UserService(Context context) {
        this.context = context;
        this.session = new SessionManager(context);
    }

    public void register(User user) {
        String url = BASE_URL + "register";

        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("login", user.getLogin());
            jsonBody.put("email", user.getEmail());
            jsonBody.put("password", user.getPassword());
            final String mRequestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Intent i = new Intent(context, MainActivity.class);
                    context.startActivity(i);
                    ((RegistrationActivity)context).finish();
                }
            }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "Rejestracja się nie powiodła", Toast.LENGTH_LONG).show();
                }})
                {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() {
                    return mRequestBody.getBytes(StandardCharsets.UTF_8);
                }

              /*  @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }*/
            };

            MySingleton.getInstance(context).addToRequestQueue(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void login(LoginForm loginForm) {
        String url = BASE_URL + "login";


        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", loginForm.getEmail());
            jsonBody.put("password", loginForm.getPassword());
            final String mRequestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject logindata = new JSONObject(response);
                        Log.d("TAG", response);
                        session.createLoginSession(logindata.getString("login"));
                        Intent i = new Intent(context, MainActivity.class);
                        context.startActivity(i);
                        ((LoginActivity)context).finish();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "Niepoprawny email lub hasło", Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() {
                    return mRequestBody.getBytes(StandardCharsets.UTF_8);
                }

              /*  @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }*/
            };

            MySingleton.getInstance(context).addToRequestQueue(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
