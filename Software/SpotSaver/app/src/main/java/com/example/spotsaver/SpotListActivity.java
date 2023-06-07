package com.example.spotsaver;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.spotsaver.model.Spot;
import com.example.spotsaver.model.SpotDao;
import com.example.spotsaver.model.SpotList;
import com.example.spotsaver.model.SpotListDao;
import com.example.spotsaver.recyclerAdapter.SpotListAdapter;
import com.example.spotsaver.utils.AppDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class SpotListActivity extends AppCompatActivity {

    FloatingActionButton addSpot;
    int value;
    ImageView delete;
    ImageView edit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spots_list);
        Bundle b = getIntent().getExtras();
        value = -1; // or other values
        if(b != null)
            value = b.getInt("key");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        delete = toolbar.findViewById(R.id.delete);
        edit = toolbar.findViewById(R.id.edit);

        TextView textView = (TextView)toolbar.findViewById(R.id.tTextview);


        getSupportActionBar().setDisplayShowTitleEnabled(false);

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "item-database").allowMainThreadQueries().fallbackToDestructiveMigration().build();

        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        List<Spot> spots = db.spotDao().getSpotsByListId(value);

        SpotList list = db.spotListDao().getListById(value);
        textView.setText(list.name);

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

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SpotListActivity.this);
                builder.setTitle("Liste löschen?");
                builder.setMessage("Diese Aktion kann nicht rückgängig gemacht werden");

                builder.setPositiveButton("Delete List", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        db.spotListDao().deleteListById(value);
                        startActivity(new Intent(SpotListActivity.this, MainActivity.class));
                    }
                });
                // Create the AlertDialog
                builder.create();
                // Show the Dialog
                builder.show();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SpotListActivity.this);
                builder.setTitle("Rename the List");

                final EditText listName = new EditText(SpotListActivity.this);
                listName.setInputType(InputType.TYPE_CLASS_TEXT);
                listName.setHint("Enter new Name of List");

                builder.setView(listName);

                builder.setPositiveButton("Rename List", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        list.name = listName.getText().toString();
                        db.spotListDao().update(list);
                        textView.setText(list.name);
                    }
                });
                // Create the AlertDialog
                builder.create();
                // Show the Dialog
                builder.show();
            }
        });
    }
}
