package com.example.spotsaver;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import org.osmdroid.library.BuildConfig;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.Objects;

public class DetailSpotActivity extends AppCompatActivity {

    private int value;
    private MapView map = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spot_details);
        Bundle b = getIntent().getExtras();
        value = -1;
        if(b != null)
            value = b.getInt("key");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        TextView textView = toolbar.findViewById(R.id.tTextview);
        textView.setText(R.string.details);
        ImageView delete = toolbar.findViewById(R.id.delete);
        ImageView edit = toolbar.findViewById(R.id.edit);
        ImageView back = findViewById(R.id.back);

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "item-database").allowMainThreadQueries().fallbackToDestructiveMigration().build();

        TextView title = findViewById(R.id.title);
        TextView desc = findViewById(R.id.description);

        Spot spot = db.spotDao().getById(value);
        title.setText(spot.name);
        desc.setText(spot.description);

        map = findViewById(R.id.mapView);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        //Setting for the tile servers to identify the app
        Configuration.getInstance().setUserAgentValue(BuildConfig.BUILD_TYPE);

        IMapController mapController = map.getController();
        mapController.setZoom(15.9);
        GeoPoint startPoint = new GeoPoint(spot.latitude, spot.longitude);
        mapController.setCenter(startPoint);

        Marker marker = new Marker(map);
        marker.setTitle(spot.latitude + " - " + spot.longitude);

        //Set Marker Icon
        @SuppressLint("UseCompatLoadingForDrawables") Drawable d = getDrawable(R.drawable.marker);
        Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
        Drawable dr = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, (int) (10.0f * getResources().getDisplayMetrics().density), (int) (16.0f * getResources().getDisplayMetrics().density), true));
        marker.setIcon(dr);

        marker.setPosition(startPoint);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(marker);

        back.setOnClickListener(v -> {
            Intent intent = new Intent(DetailSpotActivity.this, SpotListActivity.class);
            Bundle bBack = new Bundle();
            bBack.putInt("key", spot.lid); //List Id
            intent.putExtras(bBack);
            startActivity(intent);
        });

        delete.setOnClickListener(v -> {
            //Show AlertDialog to make sure user wants to delete spot
            AlertDialog.Builder builder = new AlertDialog.Builder(DetailSpotActivity.this);
            builder.setTitle(R.string.deleteSpot);
            builder.setMessage(R.string.cantUndone);

            builder.setPositiveButton(R.string.delete, (dialog, id) -> {
                db.spotDao().delete(spot.id);
                Toast.makeText(getApplicationContext(),R.string.toastSpotDelete,Toast.LENGTH_SHORT).show();
                //Go back to SpotListActivity after deletings
                Intent intent = new Intent(DetailSpotActivity.this, SpotListActivity.class);
                Bundle bDelete = new Bundle();
                bDelete.putInt("key", spot.lid); //List Id
                intent.putExtras(bDelete);
                startActivity(intent);
            });
            builder.create();
            builder.show();
        });

        //Switch to UpdateSpotActivity after clicking on edit button
        edit.setOnClickListener(v -> {
            Intent intent = new Intent(DetailSpotActivity.this, UpdateSpotActivity.class);
            Bundle bEdit = new Bundle();
            bEdit.putInt("key", value); //Spot Id
            intent.putExtras(bEdit);
            startActivity(intent);
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
