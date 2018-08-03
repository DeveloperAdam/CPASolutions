package com.techease.cpasolutions.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.techease.cpasolutions.Adapters.FilesAdapter;
import com.techease.cpasolutions.Adapters.MessagesAdapter;
import com.techease.cpasolutions.Models.FilesModel;
import com.techease.cpasolutions.Models.MessagesModel;
import com.techease.cpasolutions.R;
import com.techease.cpasolutions.Utils.AlertsUtils;
import com.techease.cpasolutions.Utils.Api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ViewDocumentFragment extends Fragment {

    TextView tvNoMessage,tvDoc,tvFname,tvFnameShow,tvSentTo,tvSentToShow,tvUploadBy,tvUploadByShow,tvFileSize,tvMessage,tvCreated,tvCreatedShow;
    EditText etMessage;
    Button btnSend;
    String fileId,userid,strMessage;
    ImageView iv;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    RecyclerView recyclerView;
    List<MessagesModel> messagesModelList;
    MessagesAdapter adapter;
    Typeface typefaceThin,typefaceBold;
    android.support.v7.app.AlertDialog alertDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_document, container, false);

        typefaceBold = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/Poppins-Medium.otf");
        typefaceThin = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/Poppins-Light.otf");
        sharedPreferences = getActivity().getSharedPreferences("abc", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        userid=sharedPreferences.getString("userid","");
        fileId=getArguments().getString("fileid");
        tvDoc=view.findViewById(R.id.tvDocuments);
        tvFname=view.findViewById(R.id.tvFileTitle);
        tvFnameShow=view.findViewById(R.id.tvFileNameSHow);
        tvUploadBy=view.findViewById(R.id.tvUploadBy);
        tvUploadByShow=view.findViewById(R.id.tvUploadedByShow);
        tvSentTo=view.findViewById(R.id.tvSentToViewDoc);
        tvSentToShow=view.findViewById(R.id.tvSentToSHow);
        tvFileSize=view.findViewById(R.id.tvFileSizeView);
        tvMessage=view.findViewById(R.id.tvMessage);
        tvCreated=view.findViewById(R.id.tvCreationDate);
        tvCreatedShow=view.findViewById(R.id.tvCreationDateShow);
        etMessage=view.findViewById(R.id.etMessage);
        btnSend=view.findViewById(R.id.btnSendMessage);
        iv=view.findViewById(R.id.ivFileDetail);
        tvNoMessage=view.findViewById(R.id.tvNoMessage);
        recyclerView=view.findViewById(R.id.rvMessage);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        messagesModelList = new ArrayList<>();

        btnSend.setTypeface(typefaceBold);
        etMessage.setTypeface(typefaceThin);
        tvCreatedShow.setTypeface(typefaceThin);
        tvCreated.setTypeface(typefaceThin);
        tvMessage.setTypeface(typefaceThin);
        tvFileSize.setTypeface(typefaceThin);
        tvSentToShow.setTypeface(typefaceThin);
        tvSentTo.setTypeface(typefaceThin);
        tvUploadByShow.setTypeface(typefaceThin);
        tvUploadBy.setTypeface(typefaceThin);
        tvFnameShow.setTypeface(typefaceThin);
        tvFname.setTypeface(typefaceThin);
        tvDoc.setTypeface(typefaceBold);
        tvNoMessage.setTypeface(typefaceBold);


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strMessage=etMessage.getText().toString();
                if (alertDialog==null)
                {
                    alertDialog= AlertsUtils.createProgressDialog(getActivity());
                    alertDialog.show();
                }
                apicallForSendingMessage();

            }
        });

        if (alertDialog==null)
        {
            alertDialog= AlertsUtils.createProgressDialog(getActivity());
            alertDialog.show();
        }
        apicall();
        adapter = new MessagesAdapter(getActivity(),messagesModelList);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void apicallForSendingMessage() {

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.sendMessage, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("zmaFileDetail",response);
                if (alertDialog!=null)
                    alertDialog.dismiss();
                alertDialog=null;

               if (response.contains("true"))
               {
                   Toast.makeText(getActivity(), "Message has been sent", Toast.LENGTH_SHORT).show();
                   if (alertDialog==null)
                   {
                       alertDialog= AlertsUtils.createProgressDialog(getActivity());
                       alertDialog.show();
                   }
                   apicall();

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
                params.put("fileid",fileId);
                params.put("userid",userid);
                params.put("message",strMessage);
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

    private void apicall() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.fileDetail, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("zmaFileDetail",response);
                if (alertDialog!=null)
                    alertDialog.dismiss();
                alertDialog=null;

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    for (int i=0; i<jsonArray.length(); i++)
                    {
                        JSONObject object=jsonArray.getJSONObject(i);
                        tvFnameShow.setText(object.getString("filename"));
                        tvFileSize.setText(object.getString("size"));
                        tvUploadByShow.setText(object.getString("uploadedby"));
                        tvSentToShow.setText(object.getString("sendto"));
                        tvCreatedShow.setText(object.getString("date"));
                        Glide.with(getActivity()).load(object.getString("icon")).into(iv);
                        JSONArray array=object.getJSONArray("messages");
                        if (array.length()<1)
                        {
                         recyclerView.setVisibility(View.GONE);
                         tvNoMessage.setVisibility(View.VISIBLE);
                        }
                        for (int z=0; z<array.length(); z++)
                        {
                            JSONObject messageObject=array.getJSONObject(z);
                            MessagesModel model=new MessagesModel();
                            model.setId(messageObject.getString("id"));
                            model.setMessage(messageObject.getString("message"));
                            model.setSendBy(messageObject.getString("messageby"));
                            model.setDate(messageObject.getString("date"));
                            messagesModelList.add(model);
                        }
                        adapter.notifyDataSetChanged();
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
                params.put("id",fileId);
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
