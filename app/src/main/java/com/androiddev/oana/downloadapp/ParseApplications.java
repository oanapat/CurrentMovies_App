package com.androiddev.oana.downloadapp;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by Oana on 7/11/2016.
 */
public class ParseApplications {
    private String xmlData;
    //use the array list to store multiple elements
    private ArrayList<Application> applications;

    public ParseApplications(String xmlData) {
        this.xmlData = xmlData;
        applications = new ArrayList<Application>();
    }
    //return the data when finished
    public ArrayList<Application> getApplications() {
        return applications;
    }


    public boolean process(){
        boolean status = true;
        Application currentRecord = null;
        boolean inEntry = false;
        String textValue = "";

        try{
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(this.xmlData));
            //capture the event type. each time we get to a particular part of the xml file the event type will change.
            int eventType = xpp.getEventType();
            //continue looping through the xml file until we get to the end of the file
            while(eventType != XmlPullParser.END_DOCUMENT){
                //get the name of the tag, start with the START_TAG
                String tagName = xpp.getName();
                switch(eventType){
                    case XmlPullParser.START_TAG:
                        //display this message
                        Log.d("ParseApplication", "Starting tag for "+ tagName);
                        if(tagName.equalsIgnoreCase("entry")){
                            inEntry = true;
                            currentRecord = new Application();

                        }
                        break;

                    //actual text to be extracted from the field
                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        Log.d("ParseApplication", "Ending tag for "+ tagName);
                        if(inEntry){
                            if(tagName.equalsIgnoreCase("entry")){
                                applications.add(currentRecord);
                                inEntry = false;
                            }
                            else if(tagName.equalsIgnoreCase("name")){
                                currentRecord.setName(textValue);
                            }
                            else if(tagName.equalsIgnoreCase("artist")){
                                currentRecord.setArtist(textValue);
                            }
                            else if(tagName.equalsIgnoreCase("releaseDate")){
                                currentRecord.setReleaseDate(textValue);
                            }
                        }
                        break;
                    default:
                }
                eventType = xpp.next();
            }
        }
        catch(Exception e){
            status = false;
            e.printStackTrace();

        }
        //summary of what is being processed
        for(Application app : applications){
            Log.d("ParseApplications", "*****************");
            Log.d("ParseApplications", "Name: " + app.getName());
            Log.d("ParseApplications", "Artist: " + app.getArtist());
            Log.d("ParseApplications", "Release Date: "+ app.getReleaseDate());
        }
        return true;
    }


}
