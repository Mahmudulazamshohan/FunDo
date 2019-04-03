package com.cubic.fundo.travello;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.cubic.fundo.travello.data_parser.ServerRequestURL;
import com.cubic.fundo.travello.json_parser.tour_details.MapInfo;
import com.cubic.fundo.travello.url_request.FetchHttp;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        navigationPlus();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    private void navigationPlus(){
        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        String division = getIntent().getStringExtra("division");
        String a = division.substring(0,1).toUpperCase() + division.substring(1).toLowerCase();

        toolbar.setTitle(a);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_24dp);

    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        onBackPressed();
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        try {
            boolean success = mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getApplicationContext(),R.raw.map_style_retro));
            if(success){
                Log.d("Map","Map Style Successfully");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        fetchMap(mMap);
    }
    public void fetchMap(final GoogleMap map){
        FetchHttp fetchHttp = new FetchHttp(getApplicationContext());
        fetchHttp.url(ServerRequestURL.getURL("map_latlang_division/"+getIntent().getStringExtra("division")),FetchHttp.POST);

        fetchHttp.setFetchDataListener(new FetchHttp.DataFetchListener() {
            @Override
            public void dataString(String responseData) {
                //Gson Parser
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<MapInfo>>(){}.getType();
                final List<MapInfo> mMapInfo = gson.fromJson(responseData,type);
                //LatLng List Array
                List<LatLng> mLatLng =  new ArrayList<>();
                for (int i =0;i<mMapInfo.size();i++){
                    mLatLng.add(new LatLng(Double.parseDouble(mMapInfo.get(i).getLat()),
                                           Double.parseDouble(mMapInfo.get(i).getLng())));
                }

                int height = 64;
                int width = 64;

                BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.map_placeholder);
                Bitmap b = bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

                for(int i =0;i<mMapInfo.size();i++){
                    //Marker From internet
                    map.addMarker(new MarkerOptions().position(mLatLng.get(i)).title(mMapInfo.get(i).getTitle()).icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));

                }

                //Marker click event
                map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        //maarker id
                        StringBuilder stringBuilder = new StringBuilder(marker.getId());
                        //Delete m from m0
                        stringBuilder.deleteCharAt(0);

                        //Intent Data Process
                        int contentId = Integer.parseInt(mMapInfo.get(Integer.parseInt(stringBuilder.toString())).getId());
                        Intent tourIntent = new Intent(getApplicationContext(), TourDetailActivity.class);
                        tourIntent.putExtra("content_id",String.valueOf(contentId));
                         startActivity(tourIntent);
                    }
                });

                int zoomToLocation = (int) Math.floor(Math.random() * mMapInfo.size());
                map.moveCamera(CameraUpdateFactory.newLatLng(mLatLng.get(zoomToLocation)));
                // map.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatLng.get(0),14));
                map.animateCamera(CameraUpdateFactory.zoomTo(5), 1000, null);

            }

            @Override
            public void fetchingError(String errorMessage) {
                Toast.makeText(getApplicationContext(),"Internet Connection Failed",Toast.LENGTH_LONG).show();
            }
        });
    }
}
