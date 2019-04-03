package com.cubic.fundo.travello;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.cubic.fundo.travello.tablayout.MapFragment;
import com.cubic.fundo.travello.tablayout.TripFragment;
import com.cubic.fundo.travello.tablayout.UpcomingFragment;
import com.cubic.fundo.travello.tablayout.ViewPagerAdapter;
import com.cubic.fundo.travello.url_request.FetchHttp;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {
      private TabLayout mTabLayout;
      private AppBarLayout mAppBarLayout;
      private ViewPager mTabLayoutViewPager;
      private FloatingActionButton mFloatButton;
      private RecyclerView mTripRecyclerView;
      //Admob Ads Native 320x50 Ads views
      NativeExpressAdView mNativeExpressAdView;
      VideoController videoController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        overridePendingTransition(R.anim.activity_open_traslate,R.anim.activity_close_scale);
        //Add Navigation Drawer Layout
        navigationPlus();
        //Initialize Admob ads native 320x50 size Add
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-3940256099942544~3347511713");
        //Only For TypeCast Data
        mTabLayout =findViewById(R.id.tablayout_id);
        mAppBarLayout = findViewById(R.id.appbar_id);
        mTabLayoutViewPager = findViewById(R.id.tablayout_viewpager);
        mFloatButton  =findViewById(R.id.floatPlus);
        mTripRecyclerView = findViewById(R.id.trip_recyclerview);
        //Setup Adapter
        ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPagerAdapter.addFragment(new TripFragment(),"Tour");
        mViewPagerAdapter.addFragment(new MapFragment(),"Map");
        mViewPagerAdapter.addFragment(new UpcomingFragment(),"Division");
        //set Limit Adapter for refresh the fragment

        mTabLayoutViewPager.setAdapter(mViewPagerAdapter);
        //This is limit
        int limit = (mViewPagerAdapter.getCount() > 1 ? mViewPagerAdapter.getCount() - 1 : 1);
        mTabLayoutViewPager.setOffscreenPageLimit(limit);
        //Tab Icon
        mTabLayout.setupWithViewPager(mTabLayoutViewPager);
        //Set Tablayout icon per index hard coded

        mTabLayout.getTabAt(0).setIcon(R.drawable.ic_explore_black_24dp);
        mTabLayout.getTabAt(1).setIcon(R.drawable.ic_place_black_24dp);
        mTabLayout.getTabAt(2).setIcon(R.drawable.ic_view_module_black_24dp);
        //Tab Seletcted Listener to track each tab fragment
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 1){
                    mFloatButton.setVisibility(FloatingActionButton.GONE);
                }else{
                    mFloatButton.setVisibility(FloatingActionButton.VISIBLE);
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        //Event Listener Part
        mFloatButton.setOnClickListener(this);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Back key press Event
        if(keyCode == KeyEvent.KEYCODE_BACK){
            alertDialogBuilder();
        }
        return super.onKeyDown(keyCode, event);

    }

    //Navigation Plus method simplify code
    public void navigationPlus(){
        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Drawer
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //Set Indicator false for new one
        toggle.setDrawerIndicatorEnabled(false);
        //Use Custom by removing old default
        toggle.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        //Add Event
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    //Ada Menubar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()){
            case R.id.nav_setting:
                Intent settingIntent = new Intent(this,SettingActivity.class);
                startActivity(settingIntent);
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
       switch (view.getId()){
           case R.id.floatPlus:
               Intent searchViewIntent = new Intent(this,SearchViewActivity.class);
               startActivity(searchViewIntent);
               break;

       }
    }

    private void alertDialogBuilder(){
        View view = LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.exit_native_ads_view,null);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

        //ads
         mNativeExpressAdView = view.findViewById(R.id.nativeAdview);
         mNativeExpressAdView.setVideoOptions(new VideoOptions.Builder()
                                                              .setStartMuted(true)
                                                              .build());


         mNativeExpressAdView.loadAd(new AdRequest.Builder()
                 .build());
         videoController = mNativeExpressAdView.getVideoController();
         videoController.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
             @Override
             public void onVideoEnd() {
                 super.onVideoEnd();
             }
         });
         mNativeExpressAdView.setAdListener(new AdListener());


         //End Ads
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        mBuilder.setView(view)
                .setCancelable(false);

        final AlertDialog mDialog = mBuilder.create();
        //Set AlertDialog Background Trasparent
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //Rescale The Background of Alert Dialog
        lp.copyFrom(mDialog.getWindow().getAttributes());
        lp.width = (int)(getResources().getDisplayMetrics().widthPixels * 0.90);
        lp.height = (int) (getResources().getDisplayMetrics().heightPixels * 0.65);
        mDialog.getWindow().setAttributes(lp);
        mDialog.show();
        //Exit Button
        Button exitBtn = view.findViewById(R.id.exit_btn);
        Button cancelBtn = view.findViewById(R.id.cancel_btn);
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });


    }

}
