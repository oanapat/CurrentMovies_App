package com.androiddev.oana.downloadapp;
/*The app will download the top movies from apple.com rss feed and display them on the screen.
* Learned about asynchronous processing and how it works in Android.*/

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
    //store downloadable content
    private String mFileContents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Click the button to extract the fields from the xml file
        btnParse = (Button) findViewById(R.id.btnParse);
        btnParse.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                ParseApplications parseApplications = new ParseApplications(mFileContents);
                //start process
                parseApplications.process();
                ArrayAdapter<Application>arrayAdapter = new ArrayAdapter<Application>(
                        MainActivity.this,R.layout.list_item, parseApplications.getApplications());
                listApps.setAdapter(arrayAdapter);

            }
        });
        listApps = (ListView) findViewById(R.id.xmlListView);
        DownloadData downloadData = new DownloadData();
        downloadData.execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topMovies/xml");
    }

    //implement Async functionality
    //takes 3 parameters:
    //-->String - download the location where the files are stored on the web
    //-->void - used for a progress bar (not going to be implemented in this app that's why it is void)
    //-->String - the actual result
    private class DownloadData extends AsyncTask<String, Void, String> {

        //the ... params specifies that the number of parameters varies
        @Override
        protected String doInBackground(String... params) {
            mFileContents = downloadXMLFile(params[0]);
            if (mFileContents == null){
                //log the output
                Log.d("DownloadData", "Error downloading");
            }
            return mFileContents;
        }

        //the String result is the result that has been received from the on call process
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("DownloadData", "Result was: " + result);
        }

        private String downloadXMLFile(String urlPath){
            //use temporary buffer to store the contents of the xml file 
            StringBuilder tempBuffer = new StringBuilder();
            try{
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

                int charRead;
                char[] inputBuffer = new char[500];
                //create loop to continuosly read from the file(download) and stop the download once everything has been downloaded
                while(true){
                    charRead = isr.read(inputBuffer);
                    if(charRead <=0){
                        break;
                    }
                    tempBuffer.append(String.copyValueOf(inputBuffer, 0, charRead));
                }

                return tempBuffer.toString();


            }catch(IOException e){
                Log.d("DownloadData", "IO Exception reading data: " + e.getMessage());
            }catch(SecurityException e){
                Log.d("DownloadData", "Security exception. Needs permission?" + e.getMessage());
            }
            return null;

        }
    }
}
