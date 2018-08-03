package com.techease.cpasolutions.Fragments;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.techease.cpasolutions.R;

import java.util.Locale;


public class AccountSettingFragment extends Fragment {

    TextView tvFname,tvLname,tvZip,tvPrint,tvAddress,tvMobile,tvEmail;
    EditText etFname,etLname,etZip,etPrint,etAddress,etMobile,etEmail;
    Typeface typefaceThin,typefaceBold;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account_setting, container, false);

        typefaceBold = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/Poppins-Medium.otf");
        typefaceThin = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/Poppins-Light.otf");
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
        return view;
    }

}
