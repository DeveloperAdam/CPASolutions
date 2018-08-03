package com.techease.cpasolutions.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.renderscript.ScriptGroup;
import android.text.InputType;
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


public class LoginFragment extends Fragment {

    TextView tvEmail,tvPass,tvShow,tvForgot;
    EditText etEmail,etPass;
    Button btnLogin;
    String email,pass,fname,lname,all,sign,client;
    android.support.v7.app.AlertDialog alertDialog;
    Typeface typefaceThin,typefaceBold;
    boolean check=false;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_login, container, false);

        sharedPreferences = getActivity().getSharedPreferences("abc", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        typefaceBold = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/Poppins-Medium.otf");
        typefaceThin = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/Poppins-Light.otf");
        tvEmail=view.findViewById(R.id.tvEmailAddress);
        tvPass=view.findViewById(R.id.tvPassword);
        tvShow=view.findViewById(R.id.tvShow);
        tvForgot=view.findViewById(R.id.tvForgotPass);
        etEmail=view.findViewById(R.id.etEmailLogin);
        etPass=view.findViewById(R.id.etPassLogin);
        btnLogin=view.findViewById(R.id.btnLogin);


        tvEmail.setTypeface(typefaceBold);
        tvForgot.setTypeface(typefaceBold);
        tvShow.setTypeface(typefaceBold);
        tvPass.setTypeface(typefaceBold);
        btnLogin.setTypeface(typefaceBold);
        etEmail.setTypeface(typefaceThin);
        etPass.setTypeface(typefaceThin);

        tvForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment=new ForgotPasswordFragment();
                getFragmentManager().beginTransaction().replace(R.id.container,fragment).addToBackStack("abc").commit();
                
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });

        tvShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (check==false)
                {
                    etPass.setInputType(InputType.TYPE_CLASS_TEXT );
                    check=true;
                }
                else
                {
                    etPass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    check=false;
                }


            }
        });



        return view;
    }

    private void check() {
        email=etEmail.getText().toString();
        pass=etPass.getText().toString();

        if (email.equals(""))
        {
            etEmail.setError("Please fill this field");

        }
        else
            if (pass.equals(""))
            {
                etPass.setError("Please fill this field");
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.login, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("zmaLogin","");
                if (response.contains("true"))
                {
                    if (alertDialog!=null)
                        alertDialog.dismiss();
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        JSONObject object=jsonObject.getJSONObject("users");
                        editor.putString("token","token").commit();
                        fname=object.getString("firstname");
                        lname=object.getString("lastname");
                        all=object.getString("all_email");
                        sign=object.getString("signature_email");
                        client=object.getString("client_email");
                        editor.putString("currentpass",pass).commit();
                        editor.putString("all",all).commit();
                        editor.putString("sign",sign).commit();
                        editor.putString("client",client).commit();
                        editor.putString("userid",object.getString("id")).commit();
                        editor.putString("name",fname+" "+lname).commit();
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
               // AlertsUtils.showErrorDialog(getActivity(),error.getMessage().toString());
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
