package com.example.spotsaver;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import com.example.spotsaver.model.Spot;
import com.example.spotsaver.utils.AppDatabase;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.IconOverlay;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;

public class DetailSpotActivity extends AppCompatActivity {

    int value;
    TextView title;
    TextView desc;
    MapView map = null;
    ImageView back;
    ImageView delete;
    ImageView edit;
    private boolean alertDialogShown = false;
    private Intent nextStartedActivity = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        setContentView(R.layout.spot_details);
        Bundle b = getIntent().getExtras();
        value = -1; // or other values
        if(b != null)
            value = b.getInt("key");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView textView = (TextView)toolbar.findViewById(R.id.tTextview);
        textView.setText("Details");
        delete = toolbar.findViewById(R.id.delete);
        edit = toolbar.findViewById(R.id.edit);
        back = findViewById(R.id.back);

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "item-database").allowMainThreadQueries().fallbackToDestructiveMigration().build();

        title = findViewById(R.id.title);
        desc = findViewById(R.id.description);

        Spot spot = db.spotDao().getById(value);
        title.setText(spot.name);
        desc.setText(spot.description);

        map = findViewById(R.id.mapView);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);

        IMapController mapController = map.getController();
        mapController.setZoom(15.9);
        GeoPoint startPoint = new GeoPoint(spot.latitude, spot.longitude);
        mapController.setCenter(startPoint);

        Marker marker = new Marker(map);
        marker.setTitle(spot.latitude + " - " + spot.longitude);

        //Set Marker Icon
        Drawable d = getDrawable(R.drawable.marker);
        Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
        Drawable dr = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, (int) (10.0f * getResources().getDisplayMetrics().density), (int) (16.0f * getResources().getDisplayMetrics().density), true));
        marker.setIcon(dr);

        marker.setPosition(startPoint);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(marker);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailSpotActivity.this, SpotListActivity.class);
                Bundle b = new Bundle();
                b.putInt("key", spot.lid); //List Id
                intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailSpotActivity.this);
                builder.setTitle("Ort löschen?");
                builder.setMessage("Diese Aktion kann nicht rückgängig gemacht werden");

                builder.setPositiveButton("Delete List", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        db.spotDao().delete(spot.id);
                        Toast.makeText(getApplicationContext(),R.string.toastSpotDelete,Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DetailSpotActivity.this, SpotListActivity.class);
                        Bundle b = new Bundle();
                        b.putInt("key", spot.lid); //List Id
                        intent.putExtras(b); //Put your id to your next Intent
                        startActivity(intent);
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
                Log.d("CreateSpot", "for lid: " + value);
                Intent intent = new Intent(DetailSpotActivity.this, UpdateSpotActivity.class);
                Bundle b = new Bundle();
                b.putInt("key", value); //List Id
                intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);
            }
        });


    }

    public void onResume() {
        super.onResume();
        map.onResume();
    }

    public void onPause() {
        super.onPause();
        map.onPause();
    }
}
