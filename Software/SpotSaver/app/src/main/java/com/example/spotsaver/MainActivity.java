package com.example.spotsaver;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.spotsaver.model.SpotList;
import com.example.spotsaver.model.SpotListDao;
import com.example.spotsaver.recyclerAdapter.ListAdapter;
import com.example.spotsaver.utils.AppDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;


import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton addList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView textView = (TextView)toolbar.findViewById(R.id.tTextview);
        textView.setText(R.string.listTitle);
        toolbar.findViewById(R.id.back).setVisibility(View.GONE);
        toolbar.findViewById(R.id.edit).setVisibility(View.GONE);
        toolbar.findViewById(R.id.delete).setVisibility(View.GONE);

        getSupportActionBar().setDisplayShowTitleEnabled(false);


        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "item-database").allowMainThreadQueries().fallbackToDestructiveMigration().build();

        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        SpotListDao spotListDao = db.spotListDao();

        List<SpotList> lists = spotListDao.getAll();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ListAdapter listAdapter = new ListAdapter(this.getApplicationContext(), lists);
        recyclerView.setAdapter(listAdapter);


        addList = findViewById(R.id.addList);
        addList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.newList);

                final EditText listName = new EditText(MainActivity.this);
                listName.setInputType(InputType.TYPE_CLASS_TEXT);
                listName.setHint(R.string.newListHint);


                builder.setView(listName);
                builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        db.spotListDao().insertAll(new SpotList(listName.getText().toString()));
                        List<SpotList> lists = spotListDao.getAll();
                        listAdapter.updateList(lists);
                    }
                });
                // Create the AlertDialog
                builder.create();
                // Show the Dialog
                builder.show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(MainActivity.this, MainActivity.class));
    }
}