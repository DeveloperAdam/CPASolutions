package com.techease.cpasolutions.Adapters;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.techease.cpasolutions.Fragments.ClientDocumentsFragment;
import com.techease.cpasolutions.Fragments.ViewDocumentFragment;
import com.techease.cpasolutions.Models.FilesModel;
import com.techease.cpasolutions.Models.FolderModel;
import com.techease.cpasolutions.R;

import java.util.ArrayList;

/**
 * Created by Adamnoor on 8/1/2018.
 */

public class FilesAdapter extends BaseAdapter{
    Activity activity;
    ArrayList<FilesModel> filesModelArrayList;
    LayoutInflater layoutInflater;
    String ext;
    public FilesAdapter(Activity activity, ArrayList<FilesModel> filesModelArrayList) {
        this.activity=activity;
        this.filesModelArrayList=filesModelArrayList;
        this.layoutInflater=LayoutInflater.from(activity);
    }

    @Override
    public int getCount() {
        if (filesModelArrayList!=null) return filesModelArrayList.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(filesModelArrayList != null && filesModelArrayList.size() > position) return  filesModelArrayList.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        final FilesModel model=filesModelArrayList.get(position);
        if(filesModelArrayList != null && filesModelArrayList.size() > position) return  filesModelArrayList.size();
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final FilesModel model=filesModelArrayList.get(position);
        MyViewHolder viewHolder = null;
        viewHolder=new MyViewHolder() ;
        view=layoutInflater.inflate(R.layout.custom_file,parent,false);

        viewHolder.typefaceBold = Typeface.createFromAsset(activity.getAssets(),  "fonts/Poppins-Medium.otf");
        viewHolder.typefacethin = Typeface.createFromAsset(activity.getAssets(),  "fonts/Poppins-Light.otf");
        viewHolder.tvFileName=view.findViewById(R.id.tvFileName);
        viewHolder.tvFileNote=view.findViewById(R.id.tvFileNote);
        viewHolder.tvFileSize=view.findViewById(R.id.tvFileSize);
        viewHolder.cardView=view.findViewById(R.id.cv);
        viewHolder.ivFile=view.findViewById(R.id.ivFile);


        viewHolder.tvFileNote.setTypeface(viewHolder.typefacethin);
        viewHolder.tvFileName.setTypeface(viewHolder.typefaceBold);
        viewHolder.tvFileSize.setTypeface(viewHolder.typefacethin);

        viewHolder.tvFileName.setText(model.getName());
        viewHolder.tvFileSize.setText(model.getSize());
        viewHolder.tvFileNote.setText(model.getNote());
        ext=model.getExt();

        Glide.with(activity).load(model.getIcon()).into(viewHolder.ivFile);


        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id=model.getId();
                Bundle bundle=new Bundle();
                bundle.putString("fileid",id);
                Fragment fragment=new ViewDocumentFragment();
                fragment.setArguments(bundle);
                ((AppCompatActivity) activity).getFragmentManager().beginTransaction().replace(R.id.navContainer,fragment).addToBackStack("abc").commit();

            }
        });


        view.setTag(viewHolder);
        return view;
    }

    private class MyViewHolder  {

        private TextView tvFileName,tvFileSize,tvFileNote;
        Typeface typefaceBold,typefacethin;
        ImageView ivFile;
        CardView cardView;

    }
}
