package com.techease.cpasolutions.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
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
import com.techease.cpasolutions.Interfaces.ApiInterface;
import com.techease.cpasolutions.Models.BlogModel;
import com.techease.cpasolutions.R;
import com.techease.cpasolutions.Retro.FileUploadResponseModel;
import com.techease.cpasolutions.Retro.RetrofitClient;
import com.techease.cpasolutions.Retro.UpdateProfileResponseModel;
import com.techease.cpasolutions.Utils.AlertsUtils;
import com.techease.cpasolutions.Utils.Api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;


public class AccountSettingFragment extends Fragment {

    TextView tvFname,tvLname,tvZip,tvPrint,tvAddress,tvMobile,tvEmail;
    EditText etFname,etLname,etZip,etPrint,etAddress,etMobile,etEmail;
    Typeface typefaceThin,typefaceBold;
    Button btnUpdate;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    android.support.v7.app.AlertDialog alertDialog;
    String fname,lname,zip,print,address,mobile,email,userid;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account_setting, container, false);

        getActivity().setTitle("ACCOUNT SETTING");
        typefaceBold = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/Poppins-Medium.otf");
        typefaceThin = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/Poppins-Light.otf");
        sharedPreferences = getActivity().getSharedPreferences("abc", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        userid=sharedPreferences.getString("userid","");
        Toast.makeText(getActivity(), userid, Toast.LENGTH_SHORT).show();
        tvFname=view.findViewById(R.id.tvFnameAS);
        tvLname=view.findViewById(R.id.tvLnameAS);
        tvZip=view.findViewById(R.id.tvZipAS);
        tvPrint=view.findViewById(R.id.tvPrintAS);
        tvAddress=view.findViewById(R.id.tvAddressAS);
        tvMobile=view.findViewById(R.id.tvMNAS);
        tvEmail=view.findViewById(R.id.tvEmailAS);
        etFname=view.findViewById(R.id.etFNameAS);
        etLname=view.findViewById(R.id.etLNameAS);
        etZip=view.findViewById(R.id.etZipCode);
        etPrint=view.findViewById(R.id.etPrintAs);
        etAddress=view.findViewById(R.id.etAddressAS);
        etMobile=view.findViewById(R.id.etMNumAS);
        etEmail=view.findViewById(R.id.etEmailAS);
        btnUpdate=view.findViewById(R.id.btnSave);

        tvFname.setTypeface(typefaceThin);
        tvLname.setTypeface(typefaceThin);
        tvZip.setTypeface(typefaceThin);
        tvPrint.setTypeface(typefaceThin);
        tvAddress.setTypeface(typefaceThin);
        tvEmail.setTypeface(typefaceThin);
        tvMobile.setTypeface(typefaceThin);

        etEmail.setTypeface(typefaceBold);
        etAddress.setTypeface(typefaceBold);
        etMobile.setTypeface(typefaceBold);
        etZip.setTypeface(typefaceBold);
        etLname.setTypeface(typefaceBold);
        etFname.setTypeface(typefaceBold);
        etPrint.setTypeface(typefaceBold);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fname=etFname.getText().toString();
                lname=etLname.getText().toString();
                mobile=etMobile.getText().toString();
                email=etEmail.getText().toString();
                print=etPrint.getText().toString();
                zip=etZip.getText().toString();
                address=etAddress.getText().toString();


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
                    if (mobile.equals(""))
                    {
                        etMobile.setError("Please fill this field");
                    }
                    else
                    if (zip.equals(""))
                    {
                        etZip.setError("Please fill this field");
                    }
                    else
                    if (address.equals(""))
                    {
                        etAddress.setError("Please fill this field");
                    } else
                    if (email.equals("") )
                    {
                        etEmail.setError("Please fill this field");
                        if (!email.contains("@") && !email.contains(".com"))
                        {
                            etEmail.setError("Please enter the correct email");
                        }

                    }
                        else
                        {
                            if (alertDialog==null)
                            {
                                alertDialog= AlertsUtils.createProgressDialog(getActivity());
                                alertDialog.show();
                            }
                           // apicall();
                            uploadFileToServer();
                        }



            }
        });

        return view;
    }

    private void uploadFileToServer()
    {
        ApiInterface apiInterface = RetrofitClient.getClient().create(ApiInterface.class);


        Call<UpdateProfileResponseModel> call = apiInterface.updateProfile(userid,fname,lname,zip,mobile,address,email);

        call.enqueue(new Callback<UpdateProfileResponseModel>() {
            @Override
            public void onResponse(Call<UpdateProfileResponseModel> call, retrofit2.Response<UpdateProfileResponseModel> response) {
                if (alertDialog!=null)
                    alertDialog.dismiss();
                alertDialog=null;

                Log.d("zmaUpdateProfile",response.toString());
                Toast.makeText(getActivity(), String.valueOf("Updated Successfully"), Toast.LENGTH_SHORT).show();
                etAddress.setText("");
                    etEmail.setText("");
                    etFname.setText("");
                    etLname.setText("");
                    etMobile.setText("");
                    etPrint.setText("");
                    etZip.setText("");
            }

            @Override
            public void onFailure(Call<UpdateProfileResponseModel> call, Throwable t) {
                if (alertDialog!=null)
                    alertDialog.dismiss();
                alertDialog=null;
                Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();

            }
        });

    }
}
