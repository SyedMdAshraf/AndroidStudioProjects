package com.example.whatstheweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView weatherTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText=findViewById(R.id.cityEditText);
        weatherTextView=findViewById(R.id.weatherTextView);


    }
    public void getWeather(View view){

        try {
            DownloadTask task=new DownloadTask();
            task.execute("https://openweathermap.org/data/2.5/weather?q="+editText.getText().toString()+"&appid=b6907d289e10d714a6e88b30761fae22");

            InputMethodManager mgr=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(editText.getWindowToken(),0);

        }catch (Exception ex){
            ex.printStackTrace();
            Toast.makeText(getApplicationContext(), "Could not find Weather :(", Toast.LENGTH_LONG).show();

        }

    }


    public class DownloadTask extends AsyncTask <String,Void,String> {

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

            }catch (Exception ex) {
                ex.printStackTrace();
                runOnUiThread(
                        new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Could not find Weather :(", Toast.LENGTH_LONG).show();

                            }
                        }
                );
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

                String message="";
                for (int i=0;i<arr.length();i++){
                    JSONObject json=arr.getJSONObject(i);

                    String main=json.getString("main");
                    String description=json.getString("description");

                    if(!main.equals("")&&!description.equals("")){
                        message+=main+": "+description+"\r\n";
                    }

                }
                if(!message.equals("")){
                    weatherTextView.setText(message);
                }else{
                    Toast.makeText(getApplicationContext(), "Could not find Weather :(", Toast.LENGTH_LONG).show();
                }


            }catch (Exception ex){
                Toast.makeText(getApplicationContext(), "Could not find Weather :(", Toast.LENGTH_LONG).show();
                ex.printStackTrace();
            }

        }
    }
}
