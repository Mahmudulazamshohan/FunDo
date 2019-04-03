package com.cubic.fundo.travello;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
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
import com.cubic.fundo.travello.data_parser.ServerRequestURL;
import com.cubic.fundo.travello.json_parser.tour_details.MapInfo;
import com.cubic.fundo.travello.json_parser.tour_details.SearchListInfo;
import com.cubic.fundo.travello.search_list.SearchItem;
import com.cubic.fundo.travello.url_request.FetchHttp;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchViewActivity extends AppCompatActivity {

    SearchView mSearchView;
    ListView mSearchList;

    ArrayAdapter<String> mArrayAdapter;
    List<SearchItem> mList =new ArrayList<>();
    ListSearchAdapter mListAdapter;
    private RequestQueue mRequestQueue;
    private String mUrl = ServerRequestURL.getURL("search_queue");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view);
        overridePendingTransition(R.anim.slide_in,R.anim.slide_up);
        //Cast
        mSearchView = findViewById(R.id.item_search_view);
        mSearchList = findViewById(R.id.search_list);
        mRequestQueue =   Volley.newRequestQueue(getApplicationContext());


        mListAdapter = new ListSearchAdapter(mList);

        //Event Listener
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                requestForSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        mSearchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Toast.makeText(getApplicationContext(),i+"",Toast.LENGTH_LONG).show();
            }
        });
        mSearchList.setAdapter(mListAdapter);

    }


    private void requestForSearch(final String query){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, mUrl+"?title="+query,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<ArrayList<SearchListInfo>>(){}.getType();
                        final List<SearchListInfo> mSearchInfoList = gson.fromJson(response,type);
                         mList.clear();
                         for (int i =0;i<mSearchInfoList.size();i++){
                            mList.add(new SearchItem(mSearchInfoList.get(i).getId(),
                                                         mSearchInfoList.get(i).getTitle()));
                         }
                         mListAdapter.notifyDataSetChanged();

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
    class ListSearchAdapter extends BaseAdapter{
        List<SearchItem> mSearchItems;
        public ListSearchAdapter(List<SearchItem> mSearchItems){
            this.mSearchItems = mSearchItems;
        }

        @Override
        public int getCount() {
            return mSearchItems.size();
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
            view = getLayoutInflater().inflate(R.layout.search_list_layout,null);
            TextView title = view.findViewById(R.id.title);
            //Button actionBtn = view.findViewById(R.id.action_btn);
            ImageButton actionBtn = view.findViewById(R.id.actionBtn);
            actionBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View views) {
                    //Intent To TourActivity
                    Intent tourIntent = new Intent(views.getContext(), TourDetailActivity.class);
                    tourIntent.putExtra("content_id",String.valueOf(mSearchItems.get(i).getId()));
                    views.getContext().startActivity(tourIntent);
                }
            });

            title.setText(mSearchItems.get(i).getTitle());

            return view;
        }



    }

}
