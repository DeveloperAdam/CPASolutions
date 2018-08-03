package com.techease.cpasolutions.Fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.techease.cpasolutions.R;


public class LoginOrSignUpFragment extends Fragment {

    TextView textView,textView2;
    Button btnSignUP,btnLogin;
    Typeface typefaceThin,typefaceBold;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login_or_sign_up, container, false);


        typefaceBold = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/Poppins-Medium.otf");
        typefaceThin = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/Poppins-Light.otf");
        textView=view.findViewById(R.id.tvWelcome);
        textView2=view.findViewById(R.id.tvText);
        btnLogin=view.findViewById(R.id.btnLoginChoose);
        btnSignUP=view.findViewById(R.id.btnSignUpChoose);


        textView.setTypeface(typefaceThin);
        textView2.setTypeface(typefaceBold);
        btnSignUP.setTypeface(typefaceBold);
        btnLogin.setTypeface(typefaceBold);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment=new LoginFragment();
                getFragmentManager().beginTransaction().replace(R.id.container,fragment).addToBackStack("abc").commit();
            }
        });

        btnSignUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment=new SignUpFragment();
                getFragmentManager().beginTransaction().replace(R.id.container,fragment).addToBackStack("abc").commit();
            }
        });

        return view;
    }

}
