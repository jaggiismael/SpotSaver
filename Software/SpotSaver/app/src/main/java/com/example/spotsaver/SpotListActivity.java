package com.example.spotsaver;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.spotsaver.model.Spot;
import com.example.spotsaver.model.SpotList;
import com.example.spotsaver.recyclerAdapter.SpotListAdapter;
import com.example.spotsaver.utils.AppDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

public class SpotListActivity extends AppCompatActivity {

    FloatingActionButton addSpot;
    int value;
    ImageView back;
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
        Log.d("SpotListe", "Key ist: " + value);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        delete = toolbar.findViewById(R.id.delete);
        edit = toolbar.findViewById(R.id.edit);
        back = findViewById(R.id.back);

        TextView textView = (TextView)toolbar.findViewById(R.id.tTextview);


        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "item-database").allowMainThreadQueries().fallbackToDestructiveMigration().build();

        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        List<Spot> spots = db.spotDao().getSpotsByListId(value);

        SpotList list = db.spotListDao().getListById(value);
        Log.d("SpotListe", "Key ist: " + list);
        textView.setText(list.name);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new SpotListAdapter(getApplicationContext(), spots));

        addSpot = findViewById(R.id.addSpot);
        addSpot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CreateSpot", "for lid: " + value);
                Intent intent = new Intent(SpotListActivity.this, CreateSpotActivity.class);
                Bundle b = new Bundle();
                b.putInt("key", value); //List Id
                intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SpotListActivity.this, MainActivity.class));
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SpotListActivity.this);
                builder.setTitle(R.string.deleteList);
                builder.setMessage(R.string.cantUndone);

                builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        db.spotListDao().deleteListById(value);
                        Toast.makeText(getApplicationContext(),R.string.toastSpotListDelete,Toast.LENGTH_SHORT).show();
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
                builder.setTitle(R.string.renameList);

                View layout = getLayoutInflater().inflate(R.layout.alert_dialog, null);
                builder.setView(layout);

                EditText listName = layout.findViewById(R.id.listName);
                listName.setInputType(InputType.TYPE_CLASS_TEXT);
                listName.setHint(R.string.newListHint);

                builder.setPositiveButton(R.string.rename, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(TextUtils.isEmpty(listName.getText().toString())) {
                            Toast.makeText(getApplicationContext(),R.string.toastSpotListFail,Toast.LENGTH_SHORT).show();
                            return;
                        }
                        list.name = listName.getText().toString();
                        db.spotListDao().update(list);
                        textView.setText(list.name);
                        Toast.makeText(getApplicationContext(),R.string.toastSpotListEdit,Toast.LENGTH_SHORT).show();
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
        startActivity(new Intent(SpotListActivity.this, MainActivity.class));
    }
}
