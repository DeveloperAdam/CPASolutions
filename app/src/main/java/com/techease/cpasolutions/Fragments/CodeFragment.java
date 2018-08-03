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
import android.widget.EditText;
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
import com.techease.cpasolutions.R;
import com.techease.cpasolutions.Utils.AlertsUtils;
import com.techease.cpasolutions.Utils.Api;

import java.util.HashMap;
import java.util.Map;


public class CodeFragment extends Fragment {

    EditText etCOde;
    Button btnSend;
    TextView tvTitle;
    android.support.v7.app.AlertDialog alertDialog;
    Typeface typefaceThin,typefaceBold;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String code,email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_code, container, false);

        sharedPreferences = getActivity().getSharedPreferences("abc", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        email=getArguments().getString("verifyemail");
        typefaceBold = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/Poppins-Medium.otf");
        typefaceThin = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/Poppins-Light.otf");
        etCOde=view.findViewById(R.id.etCode);
        tvTitle=view.findViewById(R.id.tvTitleCode);
        btnSend=view.findViewById(R.id.btnSend);


        etCOde.setTypeface(typefaceThin);
        tvTitle.setTypeface(typefaceBold);
        btnSend.setTypeface(typefaceBold);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                code=etCOde.getText().toString();

                if (code.equals(""))
                {
                    etCOde.setError("Please enter the valid code");
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
        });

        return view;
    }
    private void apicall() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.verifyEmail, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("zmaVerify",response);
                if (alertDialog!=null)
                    alertDialog.dismiss();
                alertDialog=null;
                if (response.contains("true"))
                {
                    Toast.makeText(getActivity(), "Email verified", Toast.LENGTH_SHORT).show();
                    Bundle bundle=new Bundle();
                    bundle.putString("code",code);
                    Fragment fragment=new NewPasswordFragment();
                    fragment.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("zma error", String.valueOf(error.getCause()));
                alertDialog=null;
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
                params.put("email",email);
                params.put("code",code);
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
