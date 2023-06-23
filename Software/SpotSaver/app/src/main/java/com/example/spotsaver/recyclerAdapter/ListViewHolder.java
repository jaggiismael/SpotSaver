package com.example.spotsaver.recyclerAdapter;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotsaver.R;

public class ListViewHolder extends RecyclerView.ViewHolder{
    public TextView nameView;
    public RelativeLayout layout;

    public ListViewHolder(@NonNull View itemView) {
        super(itemView);
        nameView = itemView.findViewById(R.id.name);
        layout = itemView.findViewById(R.id.layoutId);
    }
}
