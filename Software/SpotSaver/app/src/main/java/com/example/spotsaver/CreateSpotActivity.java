package com.example.spotsaver;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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

public class CreateSpotActivity extends AppCompatActivity {

    private EditText name;
    private EditText description;
    private TextInputLayout nameLayout;
    private TextInputLayout descLayout;
    private TextView errMap;
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
        errMap = findViewById(R.id.errMap);

        TextView textView = toolbar.findViewById(R.id.tTextview);
        textView.setText(R.string.addSpot);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        name = findViewById(R.id.name);
        description = findViewById(R.id.description);
        Button addSpot = findViewById(R.id.addSpot);

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "item-database").allowMainThreadQueries().fallbackToDestructiveMigration().build();

        map = findViewById(R.id.mapView);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        //Setting for the tile servers to identify the app
        Configuration.getInstance().setUserAgentValue(BuildConfig.BUILD_TYPE);

        IMapController mapController = map.getController();
        mapController.setZoom(15.9);
        GeoPoint startPoint = new GeoPoint(52.526853, 13.558792);
        mapController.setCenter(startPoint);
        mapMarker = new Marker(map);
        mapMarker.setInfoWindow(null);

        //Set Marker Icon
        @SuppressLint("UseCompatLoadingForDrawables") Drawable d = getDrawable(R.drawable.marker);
        Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
        Drawable dr = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, (int) (10.0f * getResources().getDisplayMetrics().density), (int) (16.0f * getResources().getDisplayMetrics().density), true));
        mapMarker.setIcon(dr);

        //Used to place marker on map after click
        final MapEventsReceiver mReceive = new MapEventsReceiver(){
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                map.getOverlays().remove(mapMarker);

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

        addSpot.setOnClickListener(v -> {
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
            //Check if a spot has been chosen
            if(map.getOverlays().size() < 2) {
                Log.d("Err", String.valueOf(map.getOverlays().size()));
                errMap.setText(R.string.errMap);
                return;
            }
            db.spotDao().insertAll(new Spot(name.getText().toString(), description.getText().toString(), geoPoint.getLatitude(), geoPoint.getLongitude(), value));
            Intent intent = new Intent(CreateSpotActivity.this, SpotListActivity.class);
            Bundle bSpotList = new Bundle();
            bSpotList.putInt("key", value); //List Id
            intent.putExtras(bSpotList);
            Toast.makeText(getApplicationContext(),R.string.toastSpot,Toast.LENGTH_SHORT).show();
            startActivity(intent);
        });

        back.setOnClickListener(v -> {
            Intent intent = new Intent(CreateSpotActivity.this, SpotListActivity.class);
            Bundle bBAck = new Bundle();
            bBAck.putInt("key", value); //List Id
            intent.putExtras(bBAck);
            startActivity(intent);
        });
    }
}
