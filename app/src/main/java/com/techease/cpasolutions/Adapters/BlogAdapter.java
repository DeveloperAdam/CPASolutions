package com.techease.cpasolutions.Adapters;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.techease.cpasolutions.Fragments.BlogDetailFragment;
import com.techease.cpasolutions.Fragments.ClientDocumentsFragment;
import com.techease.cpasolutions.Models.BlogModel;
import com.techease.cpasolutions.R;

import java.util.List;

/**
 * Created by Adamnoor on 8/2/2018.
 */

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.ViewHolder> {
    Activity activity;
    List<BlogModel> blogModelList;

    public BlogAdapter(Activity activity, List<BlogModel> blogModelList) {
        this.activity=activity;
        this.blogModelList=blogModelList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_blogs, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final  BlogModel model=blogModelList.get(position);

        holder.tvTitle.setText(model.getTitle());
        holder.tvDate.setText(model.getDate());
        holder.tvDown.setText(model.getTitle());
        Glide.with(activity).load(model.getImage()).into(holder.iv);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id=model.getId();
                String title=model.getTitle();
                String date=model.getDate();
                String image=model.getImage();
                Bundle bundle=new Bundle();
                bundle.putString("blogid",id);
                bundle.putString("title",title);
                bundle.putString("date",date);
                bundle.putString("image",image);
                Fragment fragment=new BlogDetailFragment();
                fragment.setArguments(bundle);
                ((AppCompatActivity) activity).getFragmentManager().beginTransaction().replace(R.id.navContainer,fragment).addToBackStack("abc").commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return blogModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle,tvDown,tvDate;
        ImageView iv;
        Typeface typefaceThin,typefaceBold;
        CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);

            typefaceBold = Typeface.createFromAsset(activity.getAssets(),  "fonts/Poppins-Medium.otf");
            typefaceThin = Typeface.createFromAsset(activity.getAssets(),  "fonts/Poppins-Light.otf");
            tvTitle=itemView.findViewById(R.id.tvBlogTitle);
            tvDate=itemView.findViewById(R.id.tvBlogDate);
            tvDown=itemView.findViewById(R.id.tv);
            iv=itemView.findViewById(R.id.ivBlog);
            cardView=itemView.findViewById(R.id.cvBlogs);

            tvTitle.setTypeface(typefaceBold);
            tvDate.setTypeface(typefaceThin);
            tvDate.setTypeface(typefaceThin);
        }
    }
}
