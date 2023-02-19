package com.example.engdesignapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.LruCache;


import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import org.json.JSONArray;
import org.json.JSONObject;

import android.widget.Button;



public class Statistics extends AppCompatActivity {

    TextView currentGoalTextView;
    TextView waterUsedTextView;

    Button openGraphButton;

    String url = "https://api.thingspeak.com/channels/1893732/feeds.json?results=2";

    JSONObject apiResponseJsonObject;




    private String readFromFile(Context context) {

        String ret = "";

        try {
            AssetManager assetManager = getAssets();
            String[] files = assetManager.list("");
            InputStream inputStream = openFileInput("waterGoal");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append("\n").append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.i("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.i("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        currentGoalTextView = (TextView)findViewById(R.id.currentGoalTextView);
        waterUsedTextView = (TextView)findViewById(R.id.waterUsedTextView);

        currentGoalTextView.setText(readFromFile(getApplicationContext()) + " liters per day");

        openGraphButton = (Button)findViewById(R.id.openGraphButton);
        openGraphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://thingspeak.com/channels/1893732"));
                startActivity(browserIntent);
            }
        });





        RequestQueue requestQueue;

        // Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        requestQueue = new RequestQueue(cache, network);

        // Start the queue
        requestQueue.start();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        apiResponseJsonObject = response;

                        try {
                            JSONArray feedsJsonArray = (JSONArray)response.getJSONArray("feeds");
                            JSONObject feed = feedsJsonArray.getJSONObject(1);
                            String currentWaterUse = feed.getString("field1");
                            double waterUse = Double.parseDouble(currentWaterUse);
                            waterUse = Math.round((waterUse) * 10) / 10.0;
                            currentWaterUse = Double.toString(waterUse);
                            waterUsedTextView.setText(currentWaterUse + " liters today");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //apiTextView.setText("Data Request Fail");
                    }
                });


        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);



    }



}

