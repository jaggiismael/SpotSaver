package com.example.spotsaver;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;

import java.util.Objects;

public class UpdateSpotActivity extends AppCompatActivity {

    public EditText name;
    public EditText description;
    public Button updateSpot;
    int value;
    ImageView back;
    ImageView delete;
    ImageView edit;
    MapView map = null;
    Marker mapMarker;
    GeoPoint geoPoint;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_spot);
        Bundle b = getIntent().getExtras();
        value = -1; // or other values
        if(b != null) {
            value = b.getInt("key");
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        delete = toolbar.findViewById(R.id.delete);
        edit = toolbar.findViewById(R.id.edit);
        back = findViewById(R.id.back);
        delete.setVisibility(View.GONE);
        edit.setVisibility(View.GONE);

        TextView textView = (TextView)toolbar.findViewById(R.id.tTextview);
        textView.setText(R.string.updateSpot);


        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        name = findViewById(R.id.name);
        description = findViewById(R.id.description);
        updateSpot = findViewById(R.id.addSpot);

        updateSpot.setText(R.string.save);

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "item-database").allowMainThreadQueries().fallbackToDestructiveMigration().build();

        Spot spot = db.spotDao().getById(value);
        name.setText(spot.name);
        description.setText(spot.description);


        map = findViewById(R.id.mapView);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);

        IMapController mapController = map.getController();
        mapController.setZoom(15.9);
        GeoPoint startPoint = new GeoPoint(52.526853, 13.558792);
        mapController.setCenter(startPoint);
        mapMarker = new Marker(map);
        geoPoint = new GeoPoint(spot.latitude, spot.longitude);
        mapMarker.setPosition(geoPoint);
        mapMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(mapMarker);

        final MapEventsReceiver mReceive = new MapEventsReceiver(){
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                if(map.getOverlays().size() < 1) {
                    map.getOverlays().remove(mapMarker);
                }
                geoPoint = p;
                mapMarker.setPosition(p);
                mapMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                map.getOverlays().add(mapMarker);
                Log.d("Lat Long", p.getLatitude() + " - "+p.getLongitude());
                return false;
            }
            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        };
        map.getOverlays().add(new MapEventsOverlay(mReceive));

        updateSpot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("UpdateSpot", "Name: " + name.getText().toString());
                spot.name = name.getText().toString();
                spot.description = description.getText().toString();
                spot.latitude = geoPoint.getLatitude();
                spot.longitude = geoPoint.getLongitude();
                db.spotDao().update(spot);
                Intent intent = new Intent(UpdateSpotActivity.this, DetailSpotActivity.class);
                Bundle b = new Bundle();
                b.putInt("key", value); //List Id
                intent.putExtras(b); //Put your id to your next Intent
                Toast.makeText(getApplicationContext(),R.string.savedChanges,Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateSpotActivity.this, DetailSpotActivity.class);
                Bundle b = new Bundle();
                b.putInt("key", value); //List Id
                intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);
            }
        });
    }
}
