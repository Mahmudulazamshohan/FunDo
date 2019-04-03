package com.cubic.fundo.travello;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.cubic.fundo.travello.comment_list.CommentListItems;
import com.cubic.fundo.travello.data_parser.ServerRequestURL;
import com.cubic.fundo.travello.image_slider.SliderPagerAdapter;
import com.cubic.fundo.travello.json_parser.tour_details.TourDetailsSingle;
import com.cubic.fundo.travello.search_list.SearchItem;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.relex.circleindicator.CircleIndicator;


public class TourDetailActivity extends AppCompatActivity implements View.OnClickListener {
    ViewPager mViewPager;
    private Button mViewCommentBtn;
    private LinearLayout mCommentLayout;
    private String contentId;
    private RequestQueue mRequestQueue;
    private TextView mContent,mPlaceLocation;
    private String mUrl,mRatePlace;
    private FloatingActionButton mRateBtn,mMapLocation;
    private int mRateStartCount = 0;
    private String contentID;
    AdView mDetailsAds,mDetailsAdsBottom;
    private ProgressDialog mProgressDialog;
    CollapsingToolbarLayout mCollapseToolBar;

    //Rating

    String mRatingString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_detail);
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
        //overridePendingTransition(R.anim.activity_open_traslate,R.anim.activity_close_scale);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please wait loading....");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        //Add Navigation View Data
        navigationPlus();
        mViewPager = findViewById(R.id.imageSliderViewPager);
        //mViewCommentBtn =  findViewById(R.id.viewComments);
        //mCommentLayout = findViewById(R.id.commentLinearLayout);
        mContent = findViewById(R.id.content);
        mRequestQueue =   Volley.newRequestQueue(getApplicationContext());
        mRateBtn = findViewById(R.id.rateBtn);
        mMapLocation  = findViewById(R.id.map_location);
        mPlaceLocation = findViewById(R.id.place_location);
        mCollapseToolBar = findViewById(R.id.collapse_toolbarlayout);
        contentID = getIntent().getStringExtra("content_id");
        //Ads Admobs
        mDetailsAds = findViewById(R.id.detailsAds);
        mDetailsAdsBottom = findViewById(R.id.detailsAdsBottom);

        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mDetailsAds.loadAd(adRequest);

        AdRequest adRequest2 = new AdRequest.Builder()
                .build();
        mDetailsAdsBottom.loadAd(adRequest);

        //Http Request Url
        mUrl = ServerRequestURL.getURL("tour_details/"+contentID);

        //Fetching tour data
        fetchTour();
        mContent.offsetTopAndBottom(1);
        //Dismiss Progress Dialog
        mRateBtn.setOnClickListener(this);
        mMapLocation.setOnClickListener(this);









    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    Toolbar toolbar;
    private void navigationPlus(){
        //Toolbar
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_24dp);

    }
    private void responseInfo(String mResponseData){
        Gson gson = new Gson();
        TourDetailsSingle mTourDetails = gson.fromJson(mResponseData, TourDetailsSingle.class);

        toolbar.setTitle(mTourDetails.getTitle());
        mCollapseToolBar.setTitle(mTourDetails.getTitle());
        mContent.setText(mTourDetails.getContent());
        mPlaceLocation.setText(mTourDetails.getDistrict()+","+mTourDetails.getDivision());
        mRatingString = mTourDetails.getRating();

        SliderPagerAdapter sliderPagerAdapter = new SliderPagerAdapter(getApplicationContext(),mTourDetails.getImage());
        mViewPager.setAdapter(sliderPagerAdapter);

        //Indicator Code
        CircleIndicator circleIndicator =  findViewById(R.id.indicator);
        circleIndicator.setViewPager(mViewPager);
    }
    public void fetchTour(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, mUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                      responseInfo(response);
                        mProgressDialog.dismiss();
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            //Access Token as Header
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
               Map<String, String> headerMap = new HashMap<String, String>();;
                return headerMap;
            }
            //Access Token as Header
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                 Map<String, String> paramMap = new HashMap<String, String>();;
                return paramMap;
            }


        };
        this.mRequestQueue.add(stringRequest);
    }
    //Navigitaion Back Button Press
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        onBackPressed();
        return true;
    }


    //Onclick Event
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rateBtn:
                alertDialogBuilder();
                break;
            case R.id.map_location:
                Intent mIntent = new Intent(this,SingleMapsActivity.class);
                mIntent.putExtra("title","abc");
                startActivity(mIntent);
                break;
        }
    }


    //Alert Dialog Rating
    private void alertDialogBuilder(){
        View view = LayoutInflater.from(getApplicationContext())
                                  .inflate(R.layout.rate_place,null);
        //Star Image
        final ImageView mStarImage1 = view.findViewById(R.id.img1);
        final ImageView mStarImage2 = view.findViewById(R.id.img2);
        final ImageView mStarImage3 = view.findViewById(R.id.img3);
        final ImageView mStarImage4 = view.findViewById(R.id.img4);
        final ImageView mStarImage5 = view.findViewById(R.id.img5);
        TextView mRating = view.findViewById(R.id.rating);
        mRating.setText(mRatingString);


        mStarImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setBackgroundColor(Color.GREEN);
                mRateStartCount = 1;

            }
        });
        mStarImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStarImage1.setBackgroundColor(Color.GREEN);
                view.setBackgroundColor(Color.GREEN);
                mRateStartCount = 2;
            }
        });
        mStarImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStarImage1.setBackgroundColor(Color.GREEN);
                mStarImage2.setBackgroundColor(Color.GREEN);
                view.setBackgroundColor(Color.GREEN);
                mRateStartCount = 3;
            }
        });
        mStarImage4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStarImage1.setBackgroundColor(Color.GREEN);
                mStarImage2.setBackgroundColor(Color.GREEN);
                mStarImage3.setBackgroundColor(Color.GREEN);
                view.setBackgroundColor(Color.GREEN);
                mRateStartCount = 4;
            }
        });
        mStarImage5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStarImage1.setBackgroundColor(Color.GREEN);
                mStarImage2.setBackgroundColor(Color.GREEN);
                mStarImage3.setBackgroundColor(Color.GREEN);
                mStarImage4.setBackgroundColor(Color.GREEN);
                view.setBackgroundColor(Color.GREEN);
                mRateStartCount = 5;
            }
        });

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        mBuilder.setView(view)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                                Settings.Secure.ANDROID_ID);
                        mRatePlace = ServerRequestURL.getURL(android_id+"/"+contentID+"/"+mRateStartCount);
                        Toast.makeText(getApplicationContext(),"Thanks for rate place"+mRatePlace,Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Cancel",null)
                .setCancelable(false);
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();

    }

    @Override
    protected void onStop() {
        super.onStop();
        Glide.get(getApplicationContext()).clearMemory();
    }

    class CommentListAdapter extends BaseAdapter {
        List<CommentListItems> mCommentListItems;
        public CommentListAdapter(List<CommentListItems> mCommentListItems){
            this.mCommentListItems = mCommentListItems;
        }

        @Override
        public int getCount() {
            return mCommentListItems.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.comment_list_layout,null);


            return view;
        }



    }


}
