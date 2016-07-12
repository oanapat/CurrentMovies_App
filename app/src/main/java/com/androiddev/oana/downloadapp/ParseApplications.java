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
    //store the data, the xml that we will process and pass data to this class, ParseApplication
    private String xmlData;
    //use the array list to store multiple elements and all the entry stored in the array will be of type application
    private ArrayList<Application> applications;

    //generate a constructor and add getters for the application
    public ParseApplications(String xmlData) {
        this.xmlData = xmlData;
        //initialize it
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

        //use build in code to extract data from xml files
        try{
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();//create new instance of this class so we can access the new features
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(this.xmlData));//parsing the code that is been send to us and that we are going to work on. Needs code of a specific form which is achieved by using the StringReader to parse
            //capture the event type. each time we get to a particular part of the xml file the event type will change.
            //every time we get to an entry we need to initialize the application object
            int eventType = xpp.getEventType();
            //continue looping through the xml file until we get to the end of the file
            while(eventType != XmlPullParser.END_DOCUMENT){
                //get the name of the tag, start with the START_TAG
                String tagName = xpp.getName();
                //depending on the tag name we will start to do some processing
                switch(eventType){
                    case XmlPullParser.START_TAG:
                        //display this message
                        Log.d("ParseApplication", "Starting tag for "+ tagName);
                        //checking if we are at the start and if yes we will start to process an application
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
                        //only if we are actually withing the entry component of the xml file
                        if(inEntry){
                            //if you reached the end of an entry we will save the record and set inEntry to false so that we can process another record
                            //these are test cases that keep the program going
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
                        //Nothing else to do.
                }
                eventType = xpp.next();
            }
        }
        catch(Exception e){
            status = false;
            e.printStackTrace();

        }
        //summary of what is being processed
        //go throigh each elem that is in the arraylist and for each element create an object called app
        for(Application app : applications){
            Log.d("ParseApplications", "*****************");
            Log.d("ParseApplications", "Name: " + app.getName());
            Log.d("ParseApplications", "Artist: " + app.getArtist());
            Log.d("ParseApplications", "Release Date: "+ app.getReleaseDate());
        }
        return true;
    }


}
