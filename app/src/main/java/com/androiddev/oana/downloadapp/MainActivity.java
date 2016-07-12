package com.androiddev.oana.downloadapp;
/*The app will download data from the internet, parse (manipulate) the data, and show it on the screen.
* Learn about asynchronous processing and how to do it in Android.
* Asynchronous - when your app takes a while to start or download content but that does not stop all the other apps from working
*               - send off request but do not wait on it until it finishes executing or downloading*/

import android.os.AsyncTask;
import android.support.annotation.MainThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private Button btnParse;
    private ListView listApps;
    //store content of download in this variable
    private String mFileContents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //by clicking the button we extract the fields from the xml file
        btnParse = (Button) findViewById(R.id.btnParse);
        btnParse.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
               //TODO: ADD parse activation code
                //kick of processing, when the button is clicked on the screen with this piece of code
                ParseApplications parseApplications = new ParseApplications(mFileContents);
                //start process
                parseApplications.process();
                ArrayAdapter<Application>arrayAdapter = new ArrayAdapter<Application>(
                        MainActivity.this,R.layout.list_item, parseApplications.getApplications());
                listApps.setAdapter(arrayAdapter);

            }
        });
        listApps = (ListView) findViewById(R.id.xmlListView);
        DownloadData downloadData = new DownloadData();//create new instance of the class
        downloadData.execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topMovies/xml");//starts the process
    }

    //implement Async functionality through which we will download an xml file
    //by using the extends keyword we use the build in android studio async class
    //takes 3 parameters that we need to define when we set uo the Async class:
    //-->String - download location where the files are stored on the web
    //-->void - used for a progress bar(not going to be implemented in this app that's why it is void)
    //-->String - the actual result, what actually happened
    private class DownloadData extends AsyncTask<String, Void, String> {

        //the ... params is a variable number of arguments, we are specifyting that the number of parameters varies
        //code that we want to run without holding the app on doing anything else, places code in background automatically by Android
        @Override
        protected String doInBackground(String... params) {
            //passing the first element in the array, the element 0
            mFileContents = downloadXMLFile(params[0]);
            if (mFileContents == null){
                //log the output
                Log.d("DownloadData", "Error downloading");
            }
            return mFileContents;
        }

        //after the above code is executed and completed the studio calls this method
        //the String s is the result that has been received from the on call process
        //update medtods and change layout
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("DownloadData", "Result was: " + result);
        }

        private String downloadXMLFile(String urlPath){
            //use temporary buffer to store the contents of the xml file because when downloading
            //it's going to download different numbers of characters at a time which will be stored in the temp buffer
            StringBuilder tempBuffer = new StringBuilder();
            //use a try catch block to handle any errors that might appear due to internet and prevent your program from catching
            try{
                //GETTING THE CONNECTION READY TO START DOWNLOADING THE DATA
                // TRY OPENING THE path to the HTTP CONNECTION TO SEE IF THE FILE CAN BE OPENED AND THE URL IS VALID
                //1. set the path
                URL url = new URL(urlPath);
                //2. getting the conncection ready to try to open the URL
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int response = connection.getResponseCode();
                //logging the response code
                Log.d("DownloadData", "The response code was" + response);
                //defining the input stream
                InputStream is = connection.getInputStream();
                //using the input stream reader
                InputStreamReader isr = new InputStreamReader(is);

                //START DOWNLOADING DATA
                //1. int reads one character at a time
                int charRead;
                //2. create a buffer, allocating space to read the file 500 bites at a time, around 3 tries at a time to download the data
                char[] inputBuffer = new char[500];
                //create loop to continuosly read from the file(download) and stop the download once everything has been downloaded
                while(true){
                    //read data into input buffer and char read returns the number of characters that will return by this call
                    charRead = isr.read(inputBuffer);
                    if(charRead <=0){
                        break;
                    }
                    //store the characters retrieved by the inputBuffer and append them to what's in the tempBuffer also convert the tempBuffer to String
                    //looks and copies inputBuffer starting at position 0 going up to including the number of characters read
                    tempBuffer.append(String.copyValueOf(inputBuffer, 0, charRead));
                }
               //1. tempBuffer will be saved as a string so the loop goes through the data, save it in the tempBuffer
                //2. convert the buffer to string and return that to the calling process all the way up in the String doInBackground: mFileContents = downloadXMLFILE

                return tempBuffer.toString();//this code happens if there is no error


            }catch(IOException e){
                Log.d("DownloadData", "IO Exception reading data: " + e.getMessage());
            }catch(SecurityException e){
                Log.d("DownloadData", "Security exception. Needs permission?" + e.getMessage());
            }
            return null;

        }
    }
}
