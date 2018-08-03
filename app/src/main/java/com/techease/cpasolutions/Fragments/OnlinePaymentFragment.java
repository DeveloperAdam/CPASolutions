package com.techease.cpasolutions.Fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.techease.cpasolutions.R;

public class OnlinePaymentFragment extends Fragment {

    TextView tvSendPayment,tvExampleEmail,tvPhone,tvLoc,tvAmount,tvNote;
    EditText etNote,etAmount;
    Button btnSend;
    Typeface typefaceThin,typefaceBold;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_online_payment, container, false);

        typefaceBold = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/Poppins-Medium.otf");
        typefaceThin = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/Poppins-Light.otf");
        tvSendPayment=view.findViewById(R.id.tvSendPaymentTo);
        tvExampleEmail=view.findViewById(R.id.tvexampleEmail);
        tvPhone=view.findViewById(R.id.tvPhone);
        tvLoc=view.findViewById(R.id.tvLocation);
        tvAmount=view.findViewById(R.id.tvAmount);
        tvNote=view.findViewById(R.id.tvNoteToMerchant);
        etNote=view.findViewById(R.id.etNote);
        etAmount=view.findViewById(R.id.etAmount);
        btnSend=view.findViewById(R.id.btnSendPayment);


        tvSendPayment.setTypeface(typefaceThin);
        tvExampleEmail.setTypeface(typefaceThin);
        tvPhone.setTypeface(typefaceThin);
        tvLoc.setTypeface(typefaceThin);
        tvAmount.setTypeface(typefaceBold);
        tvNote.setTypeface(typefaceBold);
        etNote.setTypeface(typefaceThin);
        etAmount.setTypeface(typefaceThin);
        btnSend.setTypeface(typefaceBold);


        return view;
    }

}
