package com.example.spotsaver.recyclerAdapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotsaver.R;

public class SpotListViewHolder extends RecyclerView.ViewHolder{

    ImageView imageView;
    TextView nameView;
    RelativeLayout layout;


    public SpotListViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageview);
        nameView = itemView.findViewById(R.id.name);
        layout = itemView.findViewById(R.id.layoutId);
    }
}
