package com.teamturing.dreamteam.MainFragments.Adapters;

public class HomeEvent {
    private String title;
    private String description;
    private String startDate;
    private String lastDate;
    private int image;
    private String  id;
    private  String link;
    private String key;

    public HomeEvent(String  id , String title, String description,
                     String startDate, String lastDate, int image,String link,String key) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.lastDate = lastDate;
        this.image = image;
        this.link = link;
        this.key = key;
    }

    public String getKey(){
        return key;
    }
    public void setKey(String key){
        this.key =key;
    }
    public String getLink() {return link;}
    public void setLink(String link){
        this.link = link;
    }
    public String getId() {
        return id;
    }

    public void setId(String  id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}