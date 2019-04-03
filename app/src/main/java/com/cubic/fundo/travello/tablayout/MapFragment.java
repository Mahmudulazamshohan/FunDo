package com.cubic.fundo.travello.tablayout;



import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.cubic.fundo.travello.R;
import com.cubic.fundo.travello.TourDetailActivity;
import com.cubic.fundo.travello.data_parser.ServerRequestURL;
import com.cubic.fundo.travello.json_parser.tour_details.MapInfo;
import com.cubic.fundo.travello.url_request.FetchHttp;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
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

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    View mView;
    AdView adViewTop;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.map_fragment,container,false);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
        adViewTop = mView.findViewById(R.id.adViewTop);
        return mView;
    }
    private void viewAdsNow(){
        //Admob section
         MobileAds.initialize(mView.getContext(), "ca-app-pub-3940256099942544~3347511713");

        AdRequest adRequest1 = new AdRequest.Builder()
                                            .build();
        adViewTop.loadAd(adRequest1);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        try {
            boolean success = mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(mView.getContext(),R.raw.map_style_retro));
            if(success){
                Log.d("Map","Map Style Successfully");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        fetchMap(mMap);
        //List Lat lang

    }
    public void fetchMap(final GoogleMap map){
        FetchHttp fetchHttp = new FetchHttp(mView.getContext());
        fetchHttp.url(ServerRequestURL.getURL("map_latlang"),FetchHttp.POST);

        fetchHttp.setFetchDataListener(new FetchHttp.DataFetchListener() {
            @Override
            public void dataString(String responseData) {
                //Gson Parser
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<MapInfo>>(){}.getType();
                final List<MapInfo> mMapInfos = gson.fromJson(responseData,type);
                //LatLng List Array
                List<LatLng> mLatLng =  new ArrayList<>();
                for (MapInfo mMapInfo : mMapInfos){
                    mLatLng.add(new LatLng(Double.parseDouble(mMapInfo.getLat()),
                                            Double.parseDouble(mMapInfo.getLng())));
                }
                //Test

                //EndTest
                int height = 64;
                int width = 64;
                BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.map_placeholder);
                Bitmap b=bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

                for(int i =0;i<mMapInfos.size();i++){
                    //Marker From internet
                    map.addMarker(new MarkerOptions().position(mLatLng.get(i)).title(mMapInfos.get(i).getTitle()).icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
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
                        int contentId = Integer.parseInt(mMapInfos.get(Integer.parseInt(stringBuilder.toString())).getId());
                        Intent tourIntent = new Intent(mView.getContext(), TourDetailActivity.class);
                        tourIntent.putExtra("content_id",String.valueOf(contentId));
                        mView.getContext().startActivity(tourIntent);
                    }
                });
                map.moveCamera(CameraUpdateFactory.newLatLng(mLatLng.get(0)));
               // map.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatLng.get(0),14));
                map.animateCamera(CameraUpdateFactory.zoomTo(7), 2000, null);

            }

            @Override
            public void fetchingError(String errorMessage) {
                Toast.makeText(mView.getContext(),errorMessage,Toast.LENGTH_LONG).show();
            }
        });
    }

}

