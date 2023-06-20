package com.example.spotsaver;

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

import org.osmdroid.api.IMapController;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;

import java.util.Objects;

public class CreateSpotActivity extends AppCompatActivity {

    public EditText name;
    public EditText description;
    public Button addSpot;
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
        textView.setText(R.string.addSpot);


        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        name = findViewById(R.id.name);
        description = findViewById(R.id.description);
        addSpot = findViewById(R.id.addSpot);

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "item-database").allowMainThreadQueries().fallbackToDestructiveMigration().build();

        map = findViewById(R.id.mapView);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);

        IMapController mapController = map.getController();
        mapController.setZoom(15.9);
        GeoPoint startPoint = new GeoPoint(52.526853, 13.558792);
        mapController.setCenter(startPoint);
        mapMarker = new Marker(map);
        mapMarker.setInfoWindow(null);

        //Set Marker Icon
        Drawable d = getDrawable(R.drawable.marker);
        Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
        Drawable dr = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, (int) (10.0f * getResources().getDisplayMetrics().density), (int) (16.0f * getResources().getDisplayMetrics().density), true));
        mapMarker.setIcon(dr);

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
                map.postInvalidate();
                return false;
            }
            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }


        };
        map.getOverlays().add(new MapEventsOverlay(mReceive));

        addSpot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CreateSpot", "Name: " + name.getText().toString());
                db.spotDao().insertAll(new Spot(name.getText().toString(), description.getText().toString(), geoPoint.getLatitude(), geoPoint.getLongitude(), value));
                Intent intent = new Intent(CreateSpotActivity.this, SpotListActivity.class);
                Bundle b = new Bundle();
                b.putInt("key", value); //List Id
                intent.putExtras(b); //Put your id to your next Intent
                Toast.makeText(getApplicationContext(),R.string.toastSpot,Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateSpotActivity.this, SpotListActivity.class);
                Bundle b = new Bundle();
                b.putInt("key", value); //List Id
                intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);
            }
        });
    }
}
