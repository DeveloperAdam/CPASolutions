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
import android.widget.CompoundButton;
import android.widget.Switch;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EmailPrefrenceFragment extends Fragment {

    TextView textView1,textView2,textView3,textView4,textView5,textView6,textView7;
    Typeface typefaceThin,typefaceBold;
    Switch clientSwitch,signatureSwitch,allSwitch;
    String parameter,userid;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    android.support.v7.app.AlertDialog alertDialog;
    String client,all,signature;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_email_prefrence, container, false);

        typefaceBold = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/Poppins-Medium.otf");
        typefaceThin = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/Poppins-Light.otf");
        sharedPreferences = getActivity().getSharedPreferences("abc", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        all=sharedPreferences.getString("all","");
        signature=sharedPreferences.getString("sign","");
        client=sharedPreferences.getString("client","");
        userid=sharedPreferences.getString("userid","");
        textView1=view.findViewById(R.id.tvEmailGroup);
        textView2=view.findViewById(R.id.tvClientPortal);
        textView3=view.findViewById(R.id.tvEsignature);
        textView4=view.findViewById(R.id.tvAllEmails);
        textView5=view.findViewById(R.id.tvText1);
        textView6=view.findViewById(R.id.tvText2);
        textView7=view.findViewById(R.id.tvText3);
        clientSwitch=view.findViewById(R.id.switchClient);
        signatureSwitch=view.findViewById(R.id.switchSignature);
        allSwitch=view.findViewById(R.id.switchAll);

        clientSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    parameter="client";
                    client="true";
                }
                else
                {
                    parameter="";
                    client="false";
                }
            }
        });
        signatureSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    parameter="signature";
                    signature="true";
                }
                else
                {
                    parameter="";
                    signature="false";
                }
            }
        });

        allSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    parameter="all";
                    all="true";
                }
                else
                {
                    parameter="";
                    all="false";
                }
            }
        });


        if (all.equals("true"))
        {
            allSwitch.setChecked(true);
        }
        if (client.equals("true"))
        {
            clientSwitch.setChecked(true);
        }
        if (signature.equals("true"))
        {
            signatureSwitch.setChecked(true);
        }


        textView1.setTypeface(typefaceBold);
        textView2.setTypeface(typefaceBold);
        textView3.setTypeface(typefaceBold);
        textView4.setTypeface(typefaceBold);
        textView5.setTypeface(typefaceThin);
        textView6.setTypeface(typefaceThin);
        textView7.setTypeface(typefaceThin);

        if (alertDialog==null)
        {
            alertDialog= AlertsUtils.createProgressDialog(getActivity());
            alertDialog.show();
        }
        apicall();

        return view;
    }

    private void apicall() {
        if (parameter.equals("client"))
        {
            editor.putString("client","true").commit();
        }
        else
        if (parameter.equals("all"))
        {
            editor.putString("all","true").commit();
        }
        else
        if (parameter.equals("signature"))
        {
            editor.putString("sign","true").commit();
        }
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.emailPrefrence, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("zmaEmailPrefrence",response);
                if (alertDialog!=null)
                    alertDialog.dismiss();
                alertDialog=null;

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONObject object=jsonObject.getJSONObject("users");
                    client=object.getString("client_email");
                    all=object.getString("all_email");
                    signature=object.getString("signature_email");
                    if (client.equals("true"))
                    {
                        clientSwitch.setChecked(true);
                        signatureSwitch.setChecked(false);
                        allSwitch.setChecked(false);
                    }
                    if (all.equals("true"))
                    {
                        allSwitch.setChecked(true);
                        signatureSwitch.setChecked(false);
                        clientSwitch.setChecked(false);
                    }
                    if (signature.equals("true"))
                    {
                        signatureSwitch.setChecked(true);
                        allSwitch.setChecked(false);
                        clientSwitch.setChecked(false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (alertDialog!=null)
                    alertDialog.dismiss();
                alertDialog=null;
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
                params.put("parameter",parameter);
                params.put("userid",userid);
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
