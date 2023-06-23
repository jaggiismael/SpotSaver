package com.example.spotsaver;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import com.google.android.material.textfield.TextInputLayout;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.library.BuildConfig;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;

import java.util.Objects;

public class UpdateSpotActivity extends AppCompatActivity {

    private EditText name;
    private EditText description;
    private TextInputLayout nameLayout;
    private TextInputLayout descLayout;
    private int value;
    private MapView map = null;
    private Marker mapMarker;
    private GeoPoint geoPoint;


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
        ImageView delete = toolbar.findViewById(R.id.delete);
        ImageView edit = toolbar.findViewById(R.id.edit);
        ImageView back = findViewById(R.id.back);
        delete.setVisibility(View.GONE);
        edit.setVisibility(View.GONE);
        nameLayout = findViewById(R.id.nameLayout);
        descLayout = findViewById(R.id.descLayout);

        TextView textView = (TextView)toolbar.findViewById(R.id.tTextview);
        textView.setText(R.string.updateSpot);


        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        name = findViewById(R.id.name);
        description = findViewById(R.id.description);
        Button updateSpot = findViewById(R.id.addSpot);

        updateSpot.setText(R.string.save);

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "item-database").allowMainThreadQueries().fallbackToDestructiveMigration().build();

        Spot spot = db.spotDao().getById(value);
        name.setText(spot.name);
        description.setText(spot.description);


        map = findViewById(R.id.mapView);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        //Setting for the tile servers to identify the app
        Configuration.getInstance().setUserAgentValue(BuildConfig.BUILD_TYPE);

        IMapController mapController = map.getController();
        mapController.setZoom(15.9);
        GeoPoint startPoint = new GeoPoint(spot.latitude, spot.longitude);
        mapController.setCenter(startPoint);
        mapMarker = new Marker(map);
        mapMarker.setInfoWindow(null);

        //Set Marker Icon
        @SuppressLint("UseCompatLoadingForDrawables") Drawable d = getDrawable(R.drawable.marker);
        Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
        Drawable dr = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, (int) (10.0f * getResources().getDisplayMetrics().density), (int) (16.0f * getResources().getDisplayMetrics().density), true));
        mapMarker.setIcon(dr);

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
                map.postInvalidate();
                return false;
            }
            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        };
        map.getOverlays().add(new MapEventsOverlay(mReceive));

        //Update Spot and go back to detail page
        updateSpot.setOnClickListener(v -> {
            //Check if name is set
            if(name.getText().toString().equals("")) {
                nameLayout.setError("Bitte geben Sie einen Namen ein!");
                return;
            }
            //Check if description is set
            if(description.getText().toString().equals("")) {
                descLayout.setError("Bitte geben Sie eine Beschreibung ein!");
                return;
            }
            spot.name = name.getText().toString();
            spot.description = description.getText().toString();
            spot.latitude = geoPoint.getLatitude();
            spot.longitude = geoPoint.getLongitude();
            db.spotDao().update(spot);
            Intent intent = new Intent(UpdateSpotActivity.this, DetailSpotActivity.class);
            Bundle bUpdate = new Bundle();
            bUpdate.putInt("key", value); //List Id
            intent.putExtras(bUpdate);
            Toast.makeText(getApplicationContext(),R.string.savedChanges,Toast.LENGTH_SHORT).show();
            startActivity(intent);
        });

        //Go back to DetailPage
        back.setOnClickListener(v -> {
            Intent intent = new Intent(UpdateSpotActivity.this, DetailSpotActivity.class);
            Bundle bBack = new Bundle();
            bBack.putInt("key", value); //List Id
            intent.putExtras(bBack);
            startActivity(intent);
        });
    }
}
