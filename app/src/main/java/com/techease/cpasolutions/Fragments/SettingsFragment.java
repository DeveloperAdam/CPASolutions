package com.techease.cpasolutions.Fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.techease.cpasolutions.R;

public class SettingsFragment extends Fragment {

    TextView tvAccount,tvEmailPrefrence,tvPayment,tvContactUs,tvLogout,tvChangePass;
    Typeface typefaceThin,typefaceBold;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        getActivity().setTitle("SETTING");
        typefaceBold = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/Poppins-Medium.otf");
        typefaceThin = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/Poppins-Light.otf");
        tvAccount=view.findViewById(R.id.tvAccountSetting);
        tvEmailPrefrence=view.findViewById(R.id.tvEmailPrefrences);
        tvPayment=view.findViewById(R.id.tvPaymentMethod);
        tvContactUs=view.findViewById(R.id.tvContactUs);
        tvLogout=view.findViewById(R.id.tvLogout);
        tvChangePass=view.findViewById(R.id.tvChangePassword);


        tvAccount.setTypeface(typefaceThin);
        tvChangePass.setTypeface(typefaceThin);
        tvLogout.setTypeface(typefaceThin);
        tvContactUs.setTypeface(typefaceThin);
        tvEmailPrefrence.setTypeface(typefaceThin);
        tvChangePass.setTypeface(typefaceThin);
        tvPayment.setTypeface(typefaceThin);


        tvChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment=new ChangePasswordFragment();
                getFragmentManager().beginTransaction().replace(R.id.navContainer,fragment).addToBackStack("abc").commit();
            }
        });

        tvEmailPrefrence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment=new EmailPrefrenceFragment();
                getFragmentManager().beginTransaction().replace(R.id.navContainer,fragment).addToBackStack("abc").commit();
            }
        });

        tvAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AccountSettingFragment();
                getFragmentManager().beginTransaction().replace(R.id.navContainer,fragment).addToBackStack("abc").commit();
            }
        });

        return view;
    }

}
