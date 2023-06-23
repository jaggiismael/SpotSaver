package com.example.spotsaver;

import android.app.AlertDialog;
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

    public int value;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spots_list);
        Bundle b = getIntent().getExtras();
        value = -1;
        if(b != null)
            value = b.getInt("key");
        Log.d("SpotListe", "Key ist: " + value);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView delete = toolbar.findViewById(R.id.delete);
        ImageView edit = toolbar.findViewById(R.id.edit);
        ImageView back = findViewById(R.id.back);

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

        FloatingActionButton addSpot = findViewById(R.id.addSpot);
        addSpot.setOnClickListener(v -> {
            Log.d("CreateSpot", "for lid: " + value);
            Intent intent = new Intent(SpotListActivity.this, CreateSpotActivity.class);
            Bundle bAdd = new Bundle();
            bAdd.putInt("key", value); //List Id
            intent.putExtras(bAdd);
            startActivity(intent);
        });

        back.setOnClickListener(v -> startActivity(new Intent(SpotListActivity.this, MainActivity.class)));

        delete.setOnClickListener(v -> {
            //Show AlertDialog to make sure user wants to delete list
            AlertDialog.Builder builder = new AlertDialog.Builder(SpotListActivity.this);
            builder.setTitle(R.string.deleteList);
            builder.setMessage(R.string.cantUndone);

            builder.setPositiveButton(R.string.delete, (dialog, id) -> {
                db.spotListDao().deleteListById(value);
                Toast.makeText(getApplicationContext(),R.string.toastSpotListDelete,Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SpotListActivity.this, MainActivity.class));
            });

            builder.create();
            builder.show();
        });

        edit.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(SpotListActivity.this);
            builder.setTitle(R.string.renameList);

            View layout = getLayoutInflater().inflate(R.layout.alert_dialog, null);
            builder.setView(layout);

            //Create Dialog with EditText to enter new name
            EditText listName = layout.findViewById(R.id.listName);
            listName.setInputType(InputType.TYPE_CLASS_TEXT);
            listName.setHint(R.string.newListHint);

            builder.setPositiveButton(R.string.rename, (dialog, id) -> {
                if(TextUtils.isEmpty(listName.getText().toString())) {
                    Toast.makeText(getApplicationContext(),R.string.toastSpotListFail,Toast.LENGTH_SHORT).show();
                    return;
                }
                list.name = listName.getText().toString();
                db.spotListDao().update(list);
                textView.setText(list.name);
                Toast.makeText(getApplicationContext(),R.string.toastSpotListEdit,Toast.LENGTH_SHORT).show();
            });
            // Create the AlertDialog
            builder.create();
            // Show the Dialog
            builder.show();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SpotListActivity.this, MainActivity.class));
    }
}
