package com.cubic.fundo.travello.recycler_view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cubic.fundo.travello.R;
import com.cubic.fundo.travello.TourDetailActivity;

import java.util.List;

/**
 * Created by Sho on 6/30/2018.
 */

public class TourAdapter extends RecyclerView.Adapter<TourAdapter.ContentViewHolder> {

    List<ContentList> contentLists;
    Context  applicationContext;
    int positionIndex;
    public TourAdapter(List<ContentList> contentLists, Context context){
        this.contentLists = contentLists;
        this.applicationContext  = context;
    }

    @Override
    public ContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_content,parent,false);
        return new ContentViewHolder(view);
    }
    int previousPositon = 0;


    @SuppressLint("LongLogTag")
    @Override
    public void onBindViewHolder(ContentViewHolder holder, int position) {

        ContentList contentList = contentLists.get(position);

        Glide.with(applicationContext)
                .load(contentList.getImageUrl())
                .apply(new RequestOptions().placeholder(R.drawable.blank_image)
                        .error(R.drawable.blank_image))
                .into(holder.mainImage);


        holder.headingText.setText(contentList.getHeadingText());
        holder.contentText.setText(contentList.getContentText());
        holder.setContentID(contentList.getId());


        previousPositon = position;

    }
    public void addItem(int position,ContentList contentList){
        contentLists.add(position,contentList);
        notifyItemInserted(position);
        notifyItemRangeChanged(position,contentLists.size());
    }
    public void removeItem(int positionIndex){
        contentLists.remove(positionIndex);
        notifyItemRemoved(positionIndex);

    }
    @Override
    public int getItemCount() {
        return contentLists.size();
    }
    public class ContentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mainImage,likeBtn,heartBtn,popupBtn;
        private TextView headingText,contentText;
        private boolean isLoved = false;
        private CardView cardView;
        private int contentID;

        public ContentViewHolder(View itemView) {
            super(itemView);
            //Type Cast
            mainImage = itemView.findViewById(R.id.main_image);
            likeBtn = itemView.findViewById(R.id.like_btn);
            heartBtn = itemView.findViewById(R.id.heart_btn);
            headingText = itemView.findViewById(R.id.heading_text);
            contentText = itemView.findViewById(R.id.contentText);
            popupBtn = itemView.findViewById(R.id.popup_menu_btn);
            cardView = itemView.findViewById(R.id.cardAction);
            //Set Event Listener
            popupBtn.setOnClickListener(this);
            heartBtn.setOnClickListener(this);
            cardView.setOnClickListener(this);
            mainImage.setOnClickListener(this);





        }

        public void setContentID(int contentID) {
            this.contentID = contentID;
        }

        public int getContentID() {
            return contentID;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.popup_menu_btn:
                    //Popup Menu Setup
                    PopupMenu popupMenu = new PopupMenu(applicationContext,popupBtn,Gravity.START);
                    popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()){
                                case R.id.a1:
                                    Toast.makeText(applicationContext,"Hello",Toast.LENGTH_LONG).show();
                                    break;
                            }
                            return false;
                        }
                    });

                    popupMenu.show();

                    break;

                case R.id.main_image:
                    Intent tourIntent = new Intent(applicationContext, TourDetailActivity.class);
                    tourIntent.putExtra("content_id",String.valueOf(getContentID()));
                    applicationContext.startActivity(tourIntent);
                    break;
            }
        }

    }

}
