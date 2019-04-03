package com.cubic.fundo.travello.recycler_view;

public class ContentList {

   String headingText,imageUrl,likeHit,hearthit,contentText;
   int id;

    public ContentList(Integer id , String headingText, String contentText, String imageUrl, String likeHit, String hearthit) {
        this.headingText = headingText;
        this.imageUrl = imageUrl;
        this.likeHit =likeHit;
        this.hearthit = hearthit;
        this.contentText = contentText;
        this.id = id;
    }

    public String getHeadingText() {
        return headingText;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getLikeHit() {
        return likeHit;
    }

    public String getHearthit() {
        return hearthit;
    }

    public String getContentText() {
        return contentText;
    }

    public int getId() {
        return id;
    }
}

