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


public class AboutFragment extends Fragment {

    TextView tvTitle,tvText;
    Typeface typefaceThin,typefaceBold;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        getActivity().setTitle("ABOUT");
        typefaceBold = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/Poppins-Medium.otf");
        typefaceThin = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/Poppins-Light.otf");
        tvText=view.findViewById(R.id.tvAbout);
        tvTitle=view.findViewById(R.id.tvAPPName);

        tvTitle.setTypeface(typefaceBold);
        tvText.setTypeface(typefaceThin);
        return view;
    }


}
