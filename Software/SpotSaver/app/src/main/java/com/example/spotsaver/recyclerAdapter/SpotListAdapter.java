package com.example.spotsaver.recyclerAdapter;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotsaver.DetailSpotActivity;
import com.example.spotsaver.R;
import com.example.spotsaver.model.Spot;

import java.util.List;

public class SpotListAdapter extends RecyclerView.Adapter<SpotViewHolder> {

    private final Context context;
    private final List<Spot> spots;

    public SpotListAdapter(Context context, List<Spot> spots) {
        this.context = context;
        this.spots = spots;
    }

    @NonNull
    @Override
    public SpotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SpotViewHolder(LayoutInflater.from(context).inflate(R.layout.spot_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SpotViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.nameView.setText(spots.get(position).name.trim());
        holder.descView.setText(spots.get(position).description.trim());

        holder.layout.setOnClickListener(view -> {
            Log.d("Liste", "iid: " + (spots.get(position).id));
            Intent intent = new Intent(context, DetailSpotActivity.class);

            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            Bundle b = new Bundle();
            b.putInt("key", spots.get(position).id); //List Id
            intent.putExtras(b); //Put your id to your next Intent
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return spots.size();
    }
}
