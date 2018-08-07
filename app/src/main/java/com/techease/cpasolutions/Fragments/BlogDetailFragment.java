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
import android.widget.ImageView;
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
import com.bumptech.glide.Glide;
import com.techease.cpasolutions.R;
import com.techease.cpasolutions.Utils.AlertsUtils;
import com.techease.cpasolutions.Utils.Api;

import java.util.HashMap;
import java.util.Map;


public class BlogDetailFragment extends Fragment {

    ImageView imageView;
    EditText etComment;
    Button btnSend;
    TextView tvtitle,tvdate;
    String image,title,date,userid,blogid,message;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    android.support.v7.app.AlertDialog alertDialog;
    Typeface typefaceThin,typefaceBold;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_blog_detail, container, false);

        getActivity().setTitle("BLOG");
        typefaceBold = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/Poppins-Medium.otf");
        typefaceThin = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/Poppins-Light.otf");
        sharedPreferences = getActivity().getSharedPreferences("abc", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        userid=sharedPreferences.getString("userid","");
        image=getArguments().getString("image");
        title=getArguments().getString("title");
        date=getArguments().getString("date");
        blogid=getArguments().getString("blogid");

        etComment=view.findViewById(R.id.etBlogDetailComment);
        btnSend=view.findViewById(R.id.btnComment);
        tvtitle=view.findViewById(R.id.tvBlogDetailTitle);
        tvdate=view.findViewById(R.id.tvBlogDetailDate);


        Glide.with(getActivity()).load(image).into(imageView);
        tvtitle.setText(title);
        tvdate.setText(date);


        etComment.setTypeface(typefaceThin);
        btnSend.setTypeface(typefaceBold);
        tvtitle.setTypeface(typefaceBold);
        tvdate.setTypeface(typefaceThin);


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message=etComment.getText().toString();
                if (alertDialog==null)
                {
                    alertDialog= AlertsUtils.createProgressDialog(getActivity());
                    alertDialog.show();
                }
                apicall();
            }
        });
        return  view;
    }

    private void apicall() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.comment, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("zmaFileDetail",response);
                if (alertDialog!=null)
                    alertDialog.dismiss();
                alertDialog=null;

                if (response.contains("true"))
                {
                    Toast.makeText(getActivity(), "Commented successfully", Toast.LENGTH_SHORT).show();
                    etComment.setText("");
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
                params.put("message",message);
                params.put("userid",userid);
                params.put("blogid",blogid);
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
