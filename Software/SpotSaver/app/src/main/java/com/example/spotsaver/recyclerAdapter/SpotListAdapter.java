package com.example.spotsaver.recyclerAdapter;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.spotsaver.R;
import com.example.spotsaver.model.SpotList;
import com.example.spotsaver.utils.AppDatabase;

import java.util.List;

public class SpotListAdapter extends RecyclerView.Adapter<SpotListViewHolder> {

    Context context;
    List<SpotList> lists;
    AppDatabase db;

    public SpotListAdapter(Context context, List<SpotList> lists) {
        this.context = context;
        this.lists = lists;
        db = Room.databaseBuilder(context,
                AppDatabase.class, "item-database").allowMainThreadQueries().fallbackToDestructiveMigration().build();
    }

    @NonNull
    @Override
    public SpotListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SpotListViewHolder(LayoutInflater.from(context).inflate(R.layout.spotlist_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SpotListViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.nameView.setText(lists.get(position).name.trim());
        holder.imageView.setImageResource(R.drawable.list);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Log.d("Liste", "iid: " + (lists.get(position).iid));
                Intent intent = new Intent(context, SpotActivity.class);
                //Sollte noch geändert werden
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                Bundle b = new Bundle();
                b.putInt("key", items.get(position).iid); //List Id
                intent.putExtras(b); //Put your id to your next Intent
                context.startActivity(intent);*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }
}
