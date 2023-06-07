package com.example.spotsaver;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.spotsaver.model.Spot;
import com.example.spotsaver.utils.AppDatabase;

public class CreateSpot extends AppCompatActivity {

    EditText name;
    EditText email;
    Button addSpot;
    int value;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_spot);
        Bundle b = getIntent().getExtras();
        value = -1; // or other values
        if(b != null)
            value = b.getInt("key");

        name = findViewById(R.id.name);
        email = findViewById(R.id.description);
        addSpot = findViewById(R.id.addSpot);

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "item-database").allowMainThreadQueries().fallbackToDestructiveMigration().build();

        addSpot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CreateSpot", "Name: " + name.getText().toString());
                db.spotDao().insertAll(new Spot(name.getText().toString(), email.getText().toString(), value));
                Intent intent = new Intent(CreateSpot.this, SpotListActivity.class);
                Bundle b = new Bundle();
                b.putInt("key", value); //List Id
                intent.putExtras(b); //Put your id to your next Intent
                Toast.makeText(getApplicationContext(),"Spot created",Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
    }
}
