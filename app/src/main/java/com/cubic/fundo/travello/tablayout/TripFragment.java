package com.cubic.fundo.travello.tablayout;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.cubic.fundo.travello.R;
import com.cubic.fundo.travello.data_parser.ServerRequestURL;
import com.cubic.fundo.travello.json_parser.tour_details.TourDetails;
import com.cubic.fundo.travello.json_parser.tour_details.TourDetailsData;
import com.cubic.fundo.travello.recycler_view.ContentList;
import com.cubic.fundo.travello.recycler_view.TourAdapter;
import com.cubic.fundo.travello.url_request.FetchHttp;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class TripFragment extends Fragment {
    View mView;
    private ProgressDialog mProgressDialog;
    private RecyclerView mTripRecyclerView;
    TourAdapter adapter;
    List<ContentList> contentLists = null;
    ProgressBar mProgressBarLoader;
    String mNextScrollUrlLink = null;
    //Infinite Scroll
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    LinearLayoutManager manager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
         mView = inflater.inflate(R.layout.trip_fragment,container,false);
         //Type cast
         mTripRecyclerView = mView.findViewById(R.id.trip_recyclerview);
         mProgressBarLoader = mView.findViewById(R.id.mProgressLoader);



         //Adview Banner

        if (isProgressBarAdded()){
            fetchTour();
        }

        return mView;
    }


    private boolean isProgressBarAdded(){
        mProgressDialog = new ProgressDialog(mView.getContext());
        mProgressDialog.setMessage("Please wait loading....");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        return true;
    }



    private void addTrip(String mResponseData){
        Gson gson = new Gson();
        //Multiple data parser gson

        TourDetails details = gson.fromJson(mResponseData,TourDetails.class);
        contentLists = new ArrayList<>();
        mNextScrollUrlLink = details.getNextUrl();

        for (TourDetailsData detail :details.getData())
        {
            contentLists.add(new ContentList(detail.getId(),detail.getTitle(),detail.getContent(), detail.getImage(), "0", "1"));

        }
        //Recycler View Part
         adapter = new TourAdapter(contentLists,getContext());
        manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        //Set Animation
        //mTripRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mTripRecyclerView.setLayoutManager(manager);
        mTripRecyclerView.setAdapter(adapter);
        mTripRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);


            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = mTripRecyclerView.getChildCount();
                totalItemCount = manager.getItemCount();
                firstVisibleItem = manager.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {

                    mProgressBarLoader.setVisibility(View.VISIBLE);
                    fetchTourScroll();

                    loading = true;
                }
            }
        });


    }

    public void fetchTour(){
        FetchHttp fetchHttp = new FetchHttp(mView.getContext());
        fetchHttp.url(ServerRequestURL.getURL("tour_details_short"),FetchHttp.POST);

        fetchHttp.setFetchDataListener(new FetchHttp.DataFetchListener() {
            @Override
            public void dataString(String responseData) {
                mProgressDialog.dismiss();
                 addTrip(responseData);
                }

            @Override
            public void fetchingError(String errorMessage) {
                Toast.makeText(mView.getContext(),"Internet Connection Failed",Toast.LENGTH_LONG).show();
            }
        });
    }
    public void fetchTourScroll(){

        if(mNextScrollUrlLink!=null){

            FetchHttp fetchHttp = new FetchHttp(mView.getContext());
            fetchHttp.url(mNextScrollUrlLink,FetchHttp.POST);

            fetchHttp.setFetchDataListener(new FetchHttp.DataFetchListener() {
                @Override
                public void dataString(String responseData) {
                    fetchNextScroll(responseData);
                   mProgressBarLoader.setVisibility(View.GONE);

                }

                @Override
                public void fetchingError(String errorMessage) {
                    mProgressBarLoader.setVisibility(View.GONE);
                }
            });
        }else{
             mProgressBarLoader.setVisibility(View.GONE);
        }


    }
    public void fetchNextScroll(String responseData){
        Gson gson = new Gson();
        TourDetails tourDetails = gson.fromJson(responseData,TourDetails.class);

        for (TourDetailsData detail :tourDetails.getData())
        {
            contentLists.add(new ContentList(detail.getId(),detail.getTitle(),detail.getContent(), detail.getImage(), "0", "1"));

        }
        mNextScrollUrlLink = tourDetails.getNextUrl();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                adapter.notifyDataSetChanged();

            }
        },1000);

    }


}
