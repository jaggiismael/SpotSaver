package com.example.spotsaver;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private SpotListDao spotListDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView textView = toolbar.findViewById(R.id.tTextview);
        textView.setText(R.string.listTitle);
        //Hide the unnecessary images
        toolbar.findViewById(R.id.back).setVisibility(View.GONE);
        toolbar.findViewById(R.id.edit).setVisibility(View.GONE);
        toolbar.findViewById(R.id.delete).setVisibility(View.GONE);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);


        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "item-database").allowMainThreadQueries().fallbackToDestructiveMigration().build();

        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        spotListDao = db.spotListDao();

        List<SpotList> lists = spotListDao.getAll();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ListAdapter listAdapter = new ListAdapter(this.getApplicationContext(), lists);
        recyclerView.setAdapter(listAdapter);


        FloatingActionButton addList = findViewById(R.id.addList);
        addList.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(R.string.newList);

            View layout = getLayoutInflater().inflate(R.layout.alert_dialog, null);
            builder.setView(layout);

            //Add EditText to AlertDialog to add List name
            EditText listName = layout.findViewById(R.id.listName);
            listName.setInputType(InputType.TYPE_CLASS_TEXT);
            listName.setHint(R.string.newListHint);

            builder.setPositiveButton(R.string.add, (dialog, id) -> {
                //Check if name is still empty
                if(TextUtils.isEmpty(listName.getText().toString())) {
                    Toast.makeText(getApplicationContext(),R.string.toastSpotListFail,Toast.LENGTH_SHORT).show();
                    return;
                }
                db.spotListDao().insertAll(new SpotList(listName.getText().toString()));
                List<SpotList> lists1 = spotListDao.getAll();
                listAdapter.updateList(lists1);
                Toast.makeText(getApplicationContext(),R.string.toastSpotList,Toast.LENGTH_SHORT).show();
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
        startActivity(new Intent(MainActivity.this, MainActivity.class));
    }
}