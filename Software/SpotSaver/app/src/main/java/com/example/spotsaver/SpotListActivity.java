package com.example.spotsaver;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.spotsaver.model.Spot;
import com.example.spotsaver.model.SpotDao;
import com.example.spotsaver.recyclerAdapter.SpotListAdapter;
import com.example.spotsaver.utils.AppDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class SpotListActivity extends AppCompatActivity {

    FloatingActionButton addSpot;
    int value;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spots_list);
        Bundle b = getIntent().getExtras();
        value = -1; // or other values
        if(b != null)
            value = b.getInt("key");

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "item-database").allowMainThreadQueries().fallbackToDestructiveMigration().build();

        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        SpotDao spotDao = db.spotDao();

        List<Spot> spots = spotDao.getSpotsByListId(value);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new SpotListAdapter(getApplicationContext(), spots));

        addSpot = findViewById(R.id.addSpot);
        addSpot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CreateSpot", "for lid: " + value);
                Intent intent = new Intent(SpotListActivity.this, CreateSpot.class);
                Bundle b = new Bundle();
                b.putInt("key", value); //List Id
                intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);
            }
        });
    }
}
