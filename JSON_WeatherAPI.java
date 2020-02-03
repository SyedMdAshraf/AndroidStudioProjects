package com.example.jsondemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

public class DownloadTask extends AsyncTask<String,Void,String>{

    @Override
    protected String doInBackground(String... urls) {
        String result="";
        URL url;
        HttpURLConnection urlConnection=null;

        try{
           url=new URL(urls[0]);
           urlConnection=(HttpURLConnection) url.openConnection();
           InputStream in=urlConnection.getInputStream();
           InputStreamReader reader=new InputStreamReader(in);
           int data=reader.read();

           while(data!=-1){
               char current=(char)data;
               result+=current;
               data=reader.read();
           }
            return result;

        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        try {
            JSONObject jsonObject=new JSONObject(s);
            String weatherInfo=jsonObject.getString("weather");
            Log.i("Weather Content",weatherInfo);

            JSONArray arr=new JSONArray(weatherInfo);

            for (int i=0;i<arr.length();i++){
                JSONObject json=arr.getJSONObject(i);

                Log.i("Id",json.getString("id"));
                Log.i("Main",json.getString("main"));
                Log.i("Description", json.getString("description"));
                Log.i("Icon",json.getString("icon"));
            }

          
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }
}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DownloadTask task=new DownloadTask();
        task.execute("https://samples.openweathermap.org/data/2.5/weather?q=London,uk&appid=b6907d289e10d714a6e88b30761fae22");


    }
}
