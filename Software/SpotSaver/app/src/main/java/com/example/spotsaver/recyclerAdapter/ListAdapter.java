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
import com.example.spotsaver.SpotListActivity;
import com.example.spotsaver.model.SpotList;
import com.example.spotsaver.utils.AppDatabase;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListViewHolder> {

    Context context;
    List<SpotList> lists;
    AppDatabase db;

    public ListAdapter(Context context, List<SpotList> lists) {
        this.context = context;
        this.lists = lists;
        db = Room.databaseBuilder(context,
                AppDatabase.class, "item-database").allowMainThreadQueries().fallbackToDestructiveMigration().build();
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.nameView.setText(lists.get(position).name.trim());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Liste", "iid: " + (lists.get(position).lid));
                Intent intent = new Intent(context, SpotListActivity.class);

                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                Bundle b = new Bundle();
                b.putInt("key", lists.get(position).lid); //List Id
                intent.putExtras(b); //Put your id to your next Intent
                context.startActivity(intent);
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(List<SpotList> lists) {
        this.lists = lists;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }
}
