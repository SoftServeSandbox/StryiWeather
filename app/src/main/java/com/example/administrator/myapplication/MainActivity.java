package com.example.administrator.myapplication;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import av.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView mDescriptionTextView;
    private TextView mTemperatureTextView;
    private TextView mPressureTextView;
    private TextView mHumidityTextView;
    private TextView mWindSpeedTextView;
    private TextView mCloudsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDescriptionTextView = (TextView) findViewById(R.id.description);
        mTemperatureTextView = (TextView) findViewById(R.id.temperature);
        mPressureTextView = (TextView) findViewById(R.id.pressure);
        mHumidityTextView = (TextView) findViewById(R.id.humidity);
        mWindSpeedTextView = (TextView) findViewById(R.id.wind_speed);
        mCloudsTextView = (TextView) findViewById(R.id.clouds);
        new MyAsyncTask().execute("http://api.openweathermap.org/data/2.5/weather?q=Stryi&units=metric");
    }

    public class MyAsyncTask extends AsyncTask<String, Void,JSONObject>{

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                InputStream stream = new URL(params[0]).openStream();
                String strStream = convertStreamToString(stream);
                Log.d("qwerty", strStream);
                return new JSONObject(strStream);
            } catch (IOException | JSONException error) {
                error.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONObject response) {
            if (response == null) return;
            try {
                mDescriptionTextView.setText(response.getJSONArray("weather").getJSONObject(0).getString("main") +
                        " (" + response.getJSONArray("weather").getJSONObject(0).getString("description") + ")");
                mTemperatureTextView.setText("Temperature: " +
                        String.valueOf(response.getJSONObject("main").getInt("temp")) + " \u2103");
                mPressureTextView.setText("Pressure: " +
                        String.valueOf(response.getJSONObject("main").getInt("pressure")) + " hPa");
                mHumidityTextView.setText("Humidity: " +
                        String.valueOf(response.getJSONObject("main").getInt("humidity")) + " %");
                mWindSpeedTextView.setText("Wind: " +
                        String.valueOf(response.getJSONObject("wind").getInt("speed")) + " meter/sec");
                mCloudsTextView.setText("Clouds: " +
                        String.valueOf(response.getJSONObject("clouds").getInt("all")) + " %");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        private String convertStreamToString(InputStream is) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return sb.toString();
        }
    }

}
