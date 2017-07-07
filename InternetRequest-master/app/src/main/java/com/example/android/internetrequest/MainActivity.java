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
    private Button mButton;
    private TextView mResult;
    private  String request ="http://api.openweathermap.org/data/2.5/forecast/daily?q=NOM_VILLE&cnt=14&mode=json&units=metric&lang=fr";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton = (Button) findViewById(R.id.bt_search);
        mResult = (TextView) findViewById(R.id.tv_result);
        mButton.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                new SearchTask().execute(request);

            }
        });
    }
    private class SearchTask extends AsyncTask<String, Void, String>{
        private String requestURL;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            Gson gson = new Gson();
            Log.w("AAAA",response);
            Jsondataformat result = gson.fromJson(response,Jsondataformat.class);
            Log.w("AAAA","~~~~~~~~~~~~~~~~~~```");
            response = "cod: "+ String.valueOf(result.cod) + "\nmessage: "+result.message;

            Log.w("AAAA","~~~~~~~~~~~~~~~~~~```");
            mResult.setText(response);
        }

        @Override
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
                in = urlConnection.getInputStream();

            } catch (IOException e) {
                in = urlConnection.getErrorStream();

            } finally {
                urlConnection.disconnect();

            }
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
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
