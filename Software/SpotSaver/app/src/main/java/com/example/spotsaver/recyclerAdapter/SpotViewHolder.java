package com.example.spotsaver.recyclerAdapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotsaver.R;

public class SpotViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView nameView, descView;
        RelativeLayout layout;


    public SpotViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageview);
            nameView = itemView.findViewById(R.id.name);
            descView = itemView.findViewById(R.id.description);
            layout = itemView.findViewById(R.id.layoutId);
    }
}