package com.cubic.fundo.travello.json_parser.tour_details;

public class TourDetailsSingle {

    private String content;
    private String id;
    private String title;
    private String[] image;
    private String district;
    private String division;

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    private String rating;
    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getContent ()
    {
        return content;
    }

    public void setContent (String content)
    {
        this.content = content;
    }

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

    public String[] getImage ()
    {
        return image;
    }

    public void setImage (String[] image)
    {
        this.image = image;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [content = "+content+", id = "+id+", title = "+title+", image = "+image+"]";
    }
}
