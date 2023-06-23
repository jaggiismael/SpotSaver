package com.example.spotsaver.recyclerAdapter;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotsaver.R;

public class SpotViewHolder extends RecyclerView.ViewHolder {
        public TextView nameView, descView;
        public RelativeLayout layout;

    public SpotViewHolder(@NonNull View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.name);
            descView = itemView.findViewById(R.id.description);
            layout = itemView.findViewById(R.id.layoutId);
    }
}