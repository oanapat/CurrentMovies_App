package com.androiddev.oana.downloadapp;

/**
 * Created by Oana on 7/11/2016.
 */
public class Application {

    //information that will be obtained from the xml file
    private String name;
    private String artist;
    private String releaseDate;

    //save a record an grab the content

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public String toString() {
        return "Name: " + getName() +"\n"+
                "Artist: " + getArtist()+"\n"+
                "Release Date: "+getReleaseDate()+"\n";
    }
}

