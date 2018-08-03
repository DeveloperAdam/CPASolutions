package com.techease.cpasolutions.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.obsez.android.lib.filechooser.ChooserDialog;
import com.techease.cpasolutions.Interfaces.ApiInterface;
import com.techease.cpasolutions.R;
import com.techease.cpasolutions.Retro.FileUploadResponseModel;
import com.techease.cpasolutions.Retro.RetrofitClient;
import com.techease.cpasolutions.Utils.AlertsUtils;
import com.techease.cpasolutions.Utils.Api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

import static android.app.Activity.RESULT_OK;


public class UploadFileFragment extends Fragment {

    TextView tvChooseDoc,tvChooseFolder,tvNote,tvFolder,tvSentTo,tvSelect;
    EditText etComment;
    LinearLayout linearLayout;
    Button btnUploadFile;
    ImageView iv;
    String imagepath;
    Spinner chooseFolderSpinner,sentToSpinner;
    int pos;
    File  file;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String folderName,folderId,userid,sentToName,sentToId,comment;
    String[] names;
    String[] ids;
    String[] sentToIds;
    String[] sentToNames;
    public static final int REQUEST_CODE=6384;
    Typeface typefaceThin,typefaceBold;
    android.support.v7.app.AlertDialog alertDialog;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upload_file, container, false);


        sharedPreferences = getActivity().getSharedPreferences("abc", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        userid=sharedPreferences.getString("userid","");
        typefaceBold = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/Poppins-Medium.otf");
        typefaceThin = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/Poppins-Light.otf");
        tvChooseDoc=view.findViewById(R.id.tvChooseDoc);
        chooseFolderSpinner=view.findViewById(R.id.spinnerCustom);
        sentToSpinner=view.findViewById(R.id.spinnerSentTo);
        tvSelect=view.findViewById(R.id.tvSelect);
        tvChooseFolder=view.findViewById(R.id.tvChooseFolder);
        iv=view.findViewById(R.id.ivFileView);
        tvNote=view.findViewById(R.id.tvNote);
        tvFolder=view.findViewById(R.id.tvFolder);
        tvSentTo=view.findViewById(R.id.tvSentTo);
        etComment=view.findViewById(R.id.etComment);
        btnUploadFile=view.findViewById(R.id.btnUploadFile);
        linearLayout=view.findViewById(R.id.llChooseDocument);

        Dexter.withActivity(getActivity())
                .withPermissions(
                        Manifest.permission.INTERNET,
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

            }

        }).check();


        apicall();
        getAllUser();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);

            }

        }

        enableButton();


        chooseFolderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                pos=position;
                folderName=names[pos];
                folderId=ids[pos];

                tvChooseFolder.setVisibility(View.GONE);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                folderId=ids[1];
                folderName=names[1];
            }
        });



        sentToSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pos=position;

                sentToName=sentToNames[pos];
                sentToId=sentToIds[pos];
                tvSelect.setVisibility(View.GONE);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                sentToName=sentToNames[1];
                sentToId=sentToIds[1];

            }
        });

        tvChooseDoc.setTypeface(typefaceThin);
        etComment.setTypeface(typefaceThin);
        tvSentTo.setTypeface(typefaceBold);
        tvFolder.setTypeface(typefaceBold);
        tvNote.setTypeface(typefaceBold);
        tvChooseFolder.setTypeface(typefaceThin);
        btnUploadFile.setTypeface(typefaceBold);



        btnUploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment=etComment.getText().toString();
                Toast.makeText(getActivity(), file.toString(), Toast.LENGTH_SHORT).show();
                if (alertDialog==null)
                {
                    alertDialog= AlertsUtils.createProgressDialog(getActivity());
                    alertDialog.show();
                }
                uploadFileToServer();
            }
        });

        return view;
    }
    public void enableButton()
    {
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new ChooserDialog().with(getActivity())
                        .withStartFile("*")
                        .withChosenListener(new ChooserDialog.Result() {
                            @Override
                            public void onChoosePath(String path, File pathFile) {
                                Toast.makeText(getActivity(), "FILE: " + path, Toast.LENGTH_SHORT).show();

                                file=new File(path);
                            }
                        })
                        .build()
                        .show();

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode==100 && (grantResults[0] == PackageManager.PERMISSION_GRANTED))
        {
            enableButton();
        }
        else
        {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);

            }
        }

    }



    private void uploadFileToServer()
    {
        ApiInterface apiInterface = RetrofitClient.getClient().create(ApiInterface.class);

        RequestBody requestFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        final MultipartBody.Part fileBody1 = MultipartBody.Part.createFormData("file", file.getName(), requestFile1);
        RequestBody fileName1 = RequestBody.create(MediaType.parse("text/plain"), "upload_test");

        RequestBody userIdBody = RequestBody.create(MediaType.parse("multipart/form-data"), userid);
        RequestBody folderIdBody = RequestBody.create(MediaType.parse("multipart/form-data"), folderId);
        RequestBody sendToIdBody = RequestBody.create(MediaType.parse("multipart/form-data"), sentToId);
        RequestBody noteBody = RequestBody.create(MediaType.parse("multipart/form-data"), comment);

        Call<FileUploadResponseModel> call = apiInterface.uploadFile(userIdBody,folderIdBody,sendToIdBody,fileBody1,fileName1,noteBody);

        call.enqueue(new Callback<FileUploadResponseModel>() {
            @Override
            public void onResponse(Call<FileUploadResponseModel> call, retrofit2.Response<FileUploadResponseModel> response) {
                if (alertDialog!=null)
                    alertDialog.dismiss();
                alertDialog=null;

                    Log.d("zmaRetroResponse",response.toString());
                    Toast.makeText(getActivity(), String.valueOf("Uploaded Successfully"), Toast.LENGTH_SHORT).show();
                    Fragment fragment=new ClientFoldersFragment();
                    getFragmentManager().beginTransaction().replace(R.id.navContainer,fragment).commit();
            }

            @Override
            public void onFailure(Call<FileUploadResponseModel> call, Throwable t) {
                if (alertDialog!=null)
                    alertDialog.dismiss();
                alertDialog=null;
                Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();

            }
        });

    }












    private void apicall() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.folderList, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    names=new String[jsonArray.length()];
                    ids=new String[jsonArray.length()];
                    for (int i=0; i<jsonArray.length(); i++)
                    {
                        JSONObject object=jsonArray.getJSONObject(i);

                        ids[i]=(object.getString("id"));
                        names[i]=object.getString("name");
                    }


                    ArrayAdapter<String> adp1 = new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_list_item_1, names);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    chooseFolderSpinner.setAdapter(adp1);


                } catch (JSONException e) {
                    e.printStackTrace();
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










    private void getAllUser() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Api.getAllUser, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    sentToIds=new String[jsonArray.length()];
                    sentToNames=new String[jsonArray.length()];
                    for (int i=0; i<jsonArray.length(); i++)
                    {
                        JSONObject object=jsonArray.getJSONObject(i);

                        sentToNames[i]=(object.getString("name"));
                        sentToIds[i]=(object.getString("id"));

                    }


                    ArrayAdapter<String> adp1 = new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_list_item_1, sentToNames);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sentToSpinner.setAdapter(adp1);


                } catch (JSONException e) {
                    e.printStackTrace();
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
