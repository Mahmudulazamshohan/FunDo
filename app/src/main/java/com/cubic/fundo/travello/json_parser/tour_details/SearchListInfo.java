package com.cubic.fundo.travello.json_parser.tour_details;

public class SearchListInfo {
    private String id;

    private String title;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", title = "+title+"]";
    }
}
