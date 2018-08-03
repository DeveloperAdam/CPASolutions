package com.techease.cpasolutions.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
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
import com.techease.cpasolutions.Fragments.ClientDocumentsFragment;
import com.techease.cpasolutions.Fragments.ClientFoldersFragment;
import com.techease.cpasolutions.Models.FolderModel;
import com.techease.cpasolutions.R;
import com.techease.cpasolutions.Utils.AlertsUtils;
import com.techease.cpasolutions.Utils.Api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Adamnoor on 8/1/2018.
 */

public class FolderAdapter extends BaseAdapter {

    Activity activity;
    ArrayList<FolderModel> folderModelList;
    android.support.v7.app.AlertDialog alertDialog;
    String id;
    private LayoutInflater layoutInflater;
    public FolderAdapter(Activity activity, ArrayList<FolderModel> folderModelList) {
        this.activity=activity;
        this.folderModelList=folderModelList;
        this.layoutInflater=LayoutInflater.from(activity);
    }

    @Override
    public int getCount() {
        if (folderModelList!=null) return folderModelList.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(folderModelList != null && folderModelList.size() > position) return  folderModelList.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        final FolderModel model=folderModelList.get(position);
        if(folderModelList != null && folderModelList.size() > position) return  folderModelList.size();
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        final FolderModel model=folderModelList.get(position);
        MyViewHolder viewHolder = null;
        viewHolder=new MyViewHolder() ;
        view=layoutInflater.inflate(R.layout.custom_folder,parent,false);
        viewHolder.typefaceBold = Typeface.createFromAsset(activity.getAssets(),  "fonts/Poppins-Medium.otf");
        viewHolder.typefacethin = Typeface.createFromAsset(activity.getAssets(),  "fonts/Poppins-Light.otf");
        viewHolder.tvName=view.findViewById(R.id.tvFolderName);
        viewHolder.cardView=view.findViewById(R.id.card_view_outer);
        viewHolder.tvTotalFiles=view.findViewById(R.id.tvtotalFiles);

        viewHolder.tvName.setTypeface(viewHolder.typefaceBold);
        viewHolder.tvTotalFiles.setTypeface(viewHolder.typefacethin);
        viewHolder.tvName.setText(model.getName());
        viewHolder.tvTotalFiles.setText(model.getTotalFiles()+" files");


        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id=model.getId();
                Bundle bundle=new Bundle();
                bundle.putString("folderid",id);
                Fragment fragment=new ClientDocumentsFragment();
                fragment.setArguments(bundle);
                ((AppCompatActivity) activity).getFragmentManager().beginTransaction().replace(R.id.navContainer,fragment).addToBackStack("abc").commit();
            }
        });

        viewHolder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder pictureDialog = new AlertDialog.Builder(activity);
                pictureDialog.setTitle("Do you want to Delete it?");
                String[] pictureDialogItems = {
                        "\tYes"};
                pictureDialog.setItems(pictureDialogItems,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        id=model.getId();
                                        if (alertDialog==null)
                                        {
                                            alertDialog= AlertsUtils.createProgressDialog(activity);
                                            alertDialog.show();
                                        }
                                        deleteApicall();
                                        break;
                                }
                            }
                        });
                pictureDialog.show();
                return false;
            }
        });

        view.setTag(viewHolder);
        return view;
    }

    private void deleteApicall() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.deleteFolder, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("zmaBlogs",response);
                if (alertDialog!=null)
                    alertDialog.dismiss();
                if (response.contains("true"))
                {
                    Toast.makeText(activity, "Folder Deleted Successfully", Toast.LENGTH_SHORT).show();
                    Fragment fragment=new ClientFoldersFragment();
                    ((AppCompatActivity) activity).getFragmentManager().beginTransaction().replace(R.id.navContainer,fragment).addToBackStack("abc").commit();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("zma error", String.valueOf(error.getCause()));

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    AlertsUtils.showErrorDialog(activity, "Network Error");
                } else if (error instanceof AuthFailureError) {
                    AlertsUtils.showErrorDialog(activity, "Email or Password Error");
                } else if (error instanceof ServerError) {
                    AlertsUtils.showErrorDialog(activity, "Server Error");
                } else if (error instanceof NetworkError) {
                    AlertsUtils.showErrorDialog(activity, "Network Error");
                } else if (error instanceof ParseError) {
                    AlertsUtils.showErrorDialog(activity, "Parsing Error");
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
                params.put("folderid",id);

                return params;
            }
        };

        RequestQueue mRequestQueue = Volley.newRequestQueue(activity);
        stringRequest.setRetryPolicy(new
                DefaultRetryPolicy(200000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);
    }

    private class MyViewHolder  {

        private TextView tvTotalFiles,tvName;
        Typeface typefaceBold,typefacethin;
        CardView cardView;

    }

}
