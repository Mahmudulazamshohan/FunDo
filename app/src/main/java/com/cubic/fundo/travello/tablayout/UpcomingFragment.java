package com.cubic.fundo.travello.tablayout;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cubic.fundo.travello.MapsActivity;
import com.cubic.fundo.travello.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class UpcomingFragment extends Fragment implements View.OnClickListener {

    View mView;
    LinearLayout mLinearLayout;
    CardView mDhaka,mChittagong,mKhulna,mBarisal,mMymensingh,mSylhet,mRajshahi,mRangpur;
    AdView mAdViewTop,mAdViewBottom;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //Set View
        mView = inflater.inflate(R.layout.upcoming_fragment,container,false);
        //Type cast
        mLinearLayout = mView.findViewById(R.id.linear);
        mDhaka = mView.findViewById(R.id.dhaka_card);
        mChittagong = mView.findViewById(R.id.chittagong_card);
        mKhulna = mView.findViewById(R.id.khulna_card);
        mBarisal = mView.findViewById(R.id.barisal_card);
        mMymensingh = mView.findViewById(R.id.mymensingh_card);
        mSylhet = mView.findViewById(R.id.sylhet_card);
        mRajshahi = mView.findViewById(R.id.rajshahi_card);
        mRangpur = mView.findViewById(R.id.rangpur_card);
        //Ads View
        mAdViewTop = mView.findViewById(R.id.adViewTop);
        mAdViewBottom = mView.findViewById(R.id.adViewBottom);
        //Event
        mDhaka.setOnClickListener(this);
        mChittagong.setOnClickListener(this);
        mKhulna.setOnClickListener(this);
        mBarisal.setOnClickListener(this);
        mMymensingh.setOnClickListener(this);
        mSylhet.setOnClickListener(this);
        mRajshahi.setOnClickListener(this);
        mRangpur.setOnClickListener(this);
        viewAdsNow();

        return mView;
    }

    private void viewAdsNow(){
        //Admob section
       // MobileAds.initialize(mView.getContext(), "ca-app-pub-3940256099942544~3347511713");
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdViewTop.loadAd(adRequest);
        AdRequest adRequest1 = new AdRequest.Builder()
                .build();
        mAdViewBottom.loadAd(adRequest1);
    }

    @Override
    public void onClick(View view) {
      switch (view.getId()){
          case R.id.dhaka_card:
             startNewActivity("dhaka");
              break;
          case R.id.chittagong_card:
              startNewActivity("chittagong");
              break;
          case R.id.khulna_card:
              startNewActivity("khulna");
              break;
          case R.id.sylhet_card:
              startNewActivity("sylhet");
              break;
          case R.id.rajshahi_card:
              startNewActivity("rajshahi");
              break;
          case R.id.mymensingh_card:
              startNewActivity("mymensingh");;
              break;
          case R.id.barisal_card:
              startNewActivity("barisal");
              break;
      }
    }
    public void startNewActivity(String division){
        Intent mapIntent = new Intent(mView.getContext(), MapsActivity.class);
        mapIntent.putExtra("division",division);
        mView.getContext().startActivity(mapIntent);
    }


}
