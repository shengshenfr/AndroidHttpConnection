package com.example.android.internetrequest;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    //create the button and textview for the UI
    private Button mButton;
    private TextView mResult;

    //create url
    private  String request ="http://api.openweathermap.org/data/2.5/forecast/daily?q=NOM_VILLE&cnt=14&mode=json&units=metric&lang=fr";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //put button amd result in the UI
        mButton = (Button) findViewById(R.id.bt_search);
        mResult = (TextView) findViewById(R.id.tv_result);
        //realize the listener for button
        mButton.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //execute the URL
                new SearchTask().execute(request);

            }
        });
    }


    //create the async task
    private class SearchTask extends AsyncTask<String, Void, String>{
        private String requestURL;
        @Override
        //do some markup before the background task
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        //When the background operation ends, the result is displayed directly on the UI component.
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            //create the object json
            Gson gson = new Gson();
            Log.w("AAAA",response);
            //change json data to another type of data
            Jsondataformat result = gson.fromJson(response,Jsondataformat.class);
            Log.w("AAAA","~~~~~~~~~~~~~~~~~~```");
            response = "cod: "+ String.valueOf(result.cod) + "\nmessage: "+result.message;

            Log.w("AAAA","~~~~~~~~~~~~~~~~~~```");
            mResult.setText(response);//put it in the view
        }

        @Override
        //create the httpConnection
        protected String doInBackground(String... params) {
            InputStream in = null;

            requestURL = params[0];
            URL urlRequest = null;
            try {
                urlRequest = new URL(requestURL);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection urlConnection = null;
            try {
                urlConnection = (HttpURLConnection) urlRequest.openConnection();
                //Log.w("AAAA","~~~~~~~~~~~~~~~~~~```");
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                // if response code is 200, use get input stream
                in = urlConnection.getInputStream();

            } catch (IOException e) {
                //if response code is 401 unauthorized, catch the exception
                in = urlConnection.getErrorStream();

            } finally {
                urlConnection.disconnect();

            }
            //read the data
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");//use A as a separator
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        }
    }
    private class Jsondataformat {
        long cod;
        String message;
    }

}
