package com.techease.cpasolutions.Adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.techease.cpasolutions.Models.MessagesModel;
import com.techease.cpasolutions.R;

import java.util.List;

/**
 * Created by Adamnoor on 8/3/2018.
 */

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {
    Activity activity;
    List<MessagesModel> messagesModelList;

    public MessagesAdapter(Activity activity, List<MessagesModel> messagesModelList) {
        this.activity=activity;
        this.messagesModelList=messagesModelList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_message, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final MessagesModel model=messagesModelList.get(position);

        holder.tvMsg.setText(model.getMessage());
        holder.tvDate.setText(model.getDate());

    }

    @Override
    public int getItemCount() {
        return messagesModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMsg,tvDate;
        public ViewHolder(View itemView) {
            super(itemView);

            tvDate=itemView.findViewById(R.id.tvDateMsg);
            tvMsg=itemView.findViewById(R.id.tvMsg);
        }
    }
}
