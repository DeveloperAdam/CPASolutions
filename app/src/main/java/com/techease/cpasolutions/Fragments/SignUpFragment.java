package com.techease.cpasolutions.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.techease.cpasolutions.Activities.NavigationActivity;
import com.techease.cpasolutions.R;
import com.techease.cpasolutions.Utils.AlertsUtils;
import com.techease.cpasolutions.Utils.Api;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class SignUpFragment extends Fragment {

    TextView tvFname,tvLname,tvPass,tvMob,tvEmail;
    EditText etFname,etLname,etMob,etEmail,etPass;
    Button btnSignUp;
    String fname,lname,pass,mob,email,all,sign,client;
    Typeface typefaceThin,typefaceBold;
    android.support.v7.app.AlertDialog alertDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        sharedPreferences = getActivity().getSharedPreferences("abc", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        typefaceBold = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/Poppins-Medium.otf");
        typefaceThin = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/Poppins-Light.otf");
        tvFname=view.findViewById(R.id.tvFName);
        tvLname=view.findViewById(R.id.tvFName);
        tvPass=view.findViewById(R.id.tvFName);
        tvMob=view.findViewById(R.id.tvFName);
        tvEmail=view.findViewById(R.id.tvFName);
        etFname=view.findViewById(R.id.etFName);
        etLname=view.findViewById(R.id.etLName);
        etPass=view.findViewById(R.id.etPassSignUp);
        etMob=view.findViewById(R.id.etMNum);
        etEmail=view.findViewById(R.id.etEmailSignUp);
        btnSignUp=view.findViewById(R.id.btnSignUp);

        tvFname.setTypeface(typefaceBold);
        tvLname.setTypeface(typefaceBold);
        tvMob.setTypeface(typefaceBold);
        tvEmail.setTypeface(typefaceBold);
        tvPass.setTypeface(typefaceBold);
        etFname.setTypeface(typefaceThin);
        etLname.setTypeface(typefaceThin);
        etPass.setTypeface(typefaceThin);
        etEmail.setTypeface(typefaceThin);
        etMob.setTypeface(typefaceThin);
        btnSignUp.setTypeface(typefaceBold);


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });

        return view;
    }

    private void check() {
        fname=etFname.getText().toString();
        lname=etLname.getText().toString();
        mob=etMob.getText().toString();
        pass=etPass.getText().toString();
        email=etEmail.getText().toString();

        if (fname.equals(""))
        {
            etFname.setError("Please fill this field");
        }
        else
        if (lname.equals(""))
        {
            etLname.setError("Please fill this field");
        }
        else
        if (mob.equals(""))
        {
            etMob.setError("Please fill this field");
        }
        else
        if (pass.equals(""))
        {
            etPass.setError("Please fill this field");
        }
        else
        if (etEmail.equals("") )
        {
            etEmail.setError("Please fill this field");
        }
        else
        {
            if (alertDialog==null)
            {
                alertDialog= AlertsUtils.createProgressDialog(getActivity());
                alertDialog.show();
            }
            apicall();
        }
    }
    private void apicall() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.signUp, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("true"))
                {
                    if (alertDialog!=null)
                        alertDialog.dismiss();
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        JSONObject object=jsonObject.getJSONObject("users");
                        editor.putString("email",object.getString("email")).commit();
                        fname=object.getString("firstname");
                        lname=object.getString("lastname");
                        all=object.getString("all_email");
                        sign=object.getString("signature_email");
                        client=object.getString("client_email");
                        editor.putString("currentpass",pass).commit();
                        editor.putString("userid",object.getString("id")).commit();
                        editor.putString("name",fname+" "+lname).commit();
                        editor.putString("token","token").commit();
                        editor.putString("all",all).commit();
                        editor.putString("sign",sign).commit();
                        editor.putString("client",client).commit();

                        startActivity(new Intent(getActivity(), NavigationActivity.class));
                        getActivity().finish();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
                else
                {

                    if (alertDialog!=null)
                        alertDialog.dismiss();
                    try {
                        JSONObject object=new JSONObject(response);
                        String message=object.getString("message");
                        AlertsUtils.showErrorDialog(getActivity(),message);
                        etEmail.setText("");
                        etPass.setText("");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (alertDialog!=null)
                    alertDialog.dismiss();
                AlertsUtils.showErrorDialog(getActivity(),error.getMessage().toString());
                Log.d("zma error", String.valueOf(error.getCause()));
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded;charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email",email);
                params.put("password",pass);
                params.put("firstname",fname);
                params.put("lastname",lname);
                params.put("mobile",mob);
                return params;
            }
        };

        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setRetryPolicy(new
                DefaultRetryPolicy(200000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);
    }


}
