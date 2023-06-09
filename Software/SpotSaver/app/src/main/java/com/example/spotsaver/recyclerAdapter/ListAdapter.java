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
import androidx.room.Room;

import com.example.spotsaver.R;
import com.example.spotsaver.SpotListActivity;
import com.example.spotsaver.model.SpotList;
import com.example.spotsaver.utils.AppDatabase;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListViewHolder> {

    private final Context context;
    private List<SpotList> lists;

    public ListAdapter(Context context, List<SpotList> lists) {
        this.context = context;
        this.lists = lists;
        AppDatabase db = Room.databaseBuilder(context,
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

        //After click on one List open SpotListActivity
        holder.layout.setOnClickListener(view -> {
            Log.d("Liste", "iid: " + (lists.get(position).lid));
            Intent intent = new Intent(context, SpotListActivity.class);

            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            Bundle b = new Bundle();
            b.putInt("key", lists.get(position).lid); //List Id
            intent.putExtras(b);
            context.startActivity(intent);
        });
    }

    //Change view when new List is created
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
