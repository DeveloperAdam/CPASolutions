package com.techease.cpasolutions.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
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
import com.techease.cpasolutions.Adapters.FilesAdapter;
import com.techease.cpasolutions.Adapters.FolderAdapter;
import com.techease.cpasolutions.Models.FilesModel;
import com.techease.cpasolutions.Models.FolderModel;
import com.techease.cpasolutions.R;
import com.techease.cpasolutions.Utils.AlertsUtils;
import com.techease.cpasolutions.Utils.Api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ClientDocumentsFragment extends Fragment {

    GridView gridView;
    String folderId,userid;
    Button btnUpload;
    android.support.v7.app.AlertDialog alertDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ArrayList<FilesModel> filesModelArrayList;
    FilesAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_documents, container, false);

        getActivity().setTitle("FILES");
        sharedPreferences = getActivity().getSharedPreferences("abc", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        folderId=getArguments().getString("folderid");
        userid=sharedPreferences.getString("userid","");
        btnUpload=view.findViewById(R.id.btnUpload);
        gridView=view.findViewById(R.id.gridClientDocuments);
        filesModelArrayList=new ArrayList<>();
        if (alertDialog==null)
        {
            alertDialog= AlertsUtils.createProgressDialog(getActivity());
            alertDialog.show();
        }
        apicall();


        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                     Fragment fragment=new UploadFileFragment();
                     getFragmentManager().beginTransaction().replace(R.id.navContainer,fragment).addToBackStack("abc").commit();
            }
        });
        return view;
    }


    private void apicall() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.filesInFoler, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("zmaAllFolder",response);
                if (alertDialog!=null)
                    alertDialog.dismiss();
                alertDialog=null;

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    for (int i=0; i<jsonArray.length(); i++)
                    {
                        JSONObject object=jsonArray.getJSONObject(i);
                        FilesModel model=new FilesModel();
                        model.setId(object.getString("id"));
                        model.setName(object.getString("filename"));
                        model.setNote(object.getString("note"));
                        model.setSize(object.getString("size"));
                        model.setExt(object.getString("file_ext"));
                        model.setIcon(object.getString("icon"));
                        filesModelArrayList.add(model);
                    }
                    if (getActivity()!=null)
                    {
                        adapter=new FilesAdapter(getActivity(),filesModelArrayList);
                        gridView.setAdapter(adapter);
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
                params.put("userid",userid);
                params.put("folderid",folderId);
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
