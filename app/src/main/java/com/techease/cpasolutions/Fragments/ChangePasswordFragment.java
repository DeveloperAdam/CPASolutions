package com.techease.cpasolutions.Fragments;

import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.techease.cpasolutions.Models.BlogModel;
import com.techease.cpasolutions.R;
import com.techease.cpasolutions.Utils.AlertsUtils;
import com.techease.cpasolutions.Utils.Api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ChangePasswordFragment extends Fragment {

    TextView tvConfirmPass,tvNewPass,tvCurrentPass;
    TextView etConfirmPass,etNewPass,etCurrentPass;
    Typeface typefaceThin,typefaceBold;
    Button btnChange;
    String newPass,currentPass,userid,cPass,checkCurrentPass;
    android.support.v7.app.AlertDialog alertDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        sharedPreferences = getActivity().getSharedPreferences("abc", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        checkCurrentPass=sharedPreferences.getString("currentpass","");
        userid=sharedPreferences.getString("userid","");
        typefaceBold = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/Poppins-Medium.otf");
        typefaceThin = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/Poppins-Light.otf");
        tvConfirmPass=view.findViewById(R.id.tvConfirmPass);
        tvCurrentPass=view.findViewById(R.id.tvCurrentPass);
        tvNewPass=view.findViewById(R.id.tvNewPass);
        etCurrentPass=view.findViewById(R.id.etCurrentPass);
        etNewPass=view.findViewById(R.id.etNewPass);
        etConfirmPass=view.findViewById(R.id.etConfirmPass);
        btnChange=view.findViewById(R.id.btnChange);

        tvCurrentPass.setTypeface(typefaceBold);
        tvNewPass.setTypeface(typefaceBold);
        tvConfirmPass.setTypeface(typefaceBold);
        etConfirmPass.setTypeface(typefaceThin);
        etNewPass.setTypeface(typefaceThin);
        etCurrentPass.setTypeface(typefaceThin);



        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPass=etCurrentPass.getText().toString();
                newPass=etNewPass.getText().toString();
                cPass=etConfirmPass.getText().toString();
                if (!currentPass.equals(checkCurrentPass))
                {
                    etCurrentPass.setError("Current password is incorrect");
                }
                else
                if (currentPass.equals(""))
                {
                    etCurrentPass.setError("Enter current password");
                }
                else
                if (newPass.equals(""))
                {
                    etNewPass.setError("Enter New password");
                }
                else
                if (cPass.equals(""))
                {
                    etConfirmPass.setError("Please confirm password");
                }
                else
                {
                    if (newPass.equals(cPass))
                    {
                        if (alertDialog==null)
                        {
                            alertDialog= AlertsUtils.createProgressDialog(getActivity());
                            alertDialog.show();
                        }
                        apicall();
                    }
                    else
                    {
                        etConfirmPass.setError("Password does not match");
                    }


                }
            }
        });

        return view;
    }
    private void apicall() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.updatePass, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("zmaBlogs",response);
                if (alertDialog!=null)
                    alertDialog.dismiss();
                if (response.contains("true"))
                {
                    editor.putString("currentpass",currentPass).commit();
                    Toast.makeText(getActivity(), "Password Updated Successfully", Toast.LENGTH_SHORT).show();
                    etConfirmPass.setText("");
                    etCurrentPass.setText("");
                    etNewPass.setText("");

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("zma error", String.valueOf(error.getCause()));

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    AlertsUtils.showErrorDialog(getActivity(), "Network Error");
                } else if (error instanceof AuthFailureError) {
                    AlertsUtils.showErrorDialog(getActivity(), "Email or Password Error");
                } else if (error instanceof ServerError) {
                    AlertsUtils.showErrorDialog(getActivity(), "Server Error");
                } else if (error instanceof NetworkError) {
                    AlertsUtils.showErrorDialog(getActivity(), "Network Error");
                } else if (error instanceof ParseError) {
                    AlertsUtils.showErrorDialog(getActivity(), "Parsing Error");
                }
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded;charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userid",userid);
                params.put("current",currentPass);
                params.put("new",newPass);
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
