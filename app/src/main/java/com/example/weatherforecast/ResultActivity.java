package com.example.weatherforecast;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ResultActivity extends Activity {

    TextView temperature;
    TextView tempLowHigh;
    TextView summary;
    TextView precipVal;
    TextView rainVal;
    TextView windVal;
    TextView dewVal;
    TextView humidVal;
    TextView visVal;
    TextView sunriseVal;
    TextView sunsetVal;
    HttpClient client;
    JSONObject json;
    JSONObject jsonParam;
    final String degSym = "\u00b0";
    String main_summ = "";
    String desc_summ = "";
    String main_icon = "";
    String degreeParam;
    String lat="";
    String lon = "";


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_activity);

        temperature = (TextView) findViewById(R.id.temperature);
        summary = (TextView) findViewById(R.id.summ_text);
        tempLowHigh = (TextView) findViewById(R.id.temp_low_high);
        precipVal = (TextView) findViewById(R.id.precip_val);
        rainVal = (TextView) findViewById(R.id.rain_val);
        windVal = (TextView) findViewById(R.id.wind_val);
        dewVal = (TextView) findViewById(R.id.dew_val);
        humidVal = (TextView) findViewById(R.id.humid_val);
        visVal = (TextView) findViewById(R.id.vis_val);
        sunriseVal = (TextView) findViewById(R.id.sunrise_val);
        sunsetVal = (TextView) findViewById(R.id.sunset_val);
        client = new DefaultHttpClient();
        new Read().execute();


        //Details Button

        Button detailsBtn = (Button) findViewById(R.id.details_button);
        detailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), DetailsActivity.class);
                Bundle b = new Bundle();
                b.putString("jsondata", jsonParam.toString());
                intent.putExtras(b);
                intent.putExtra("DEGREE", degreeParam);
                Intent cityIntent = getIntent();
                String cityParam = cityIntent.getExtras().getString("CITY");
                intent.putExtra("CITY", cityParam);
                Intent stateIntent = getIntent();
                String stateParam = stateIntent.getExtras().getString("STATE");
                intent.putExtra("STATE", stateParam);
                startActivity(intent);

            }
        });

        //Map Button

        Button mapBtn = (Button) findViewById(R.id.map_button);
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ResultActivity.this, MapActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("lat", lat);
                bundle.putString("lon", lon);
                i.putExtras(bundle);
                startActivity(i);
            }
        });


        //FB Button

        ImageButton share = (ImageButton) findViewById(R.id.fbBtn);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), com.example.weatherforecast.FacebookActivity.class);
                intent.putExtra("SUMMARY",main_summ);
                intent.putExtra("DESC",desc_summ);
                intent.putExtra("URL","http://forecast.io/");
                intent.putExtra("ICON",main_icon);
                startActivity(intent);
            }
        });

    }


    public JSONObject Weather() throws ClientProtocolException,IOException,JSONException{
        Intent intent=getIntent();
        String url = intent.getExtras().getString("URL");
        System.out.println(url);
        HttpGet get = new HttpGet(url);
        HttpResponse r = client.execute(get);
        int status = r.getStatusLine().getStatusCode();
        if(status==200){
            HttpEntity e= r.getEntity();
            String data = EntityUtils.toString(e);
            JSONObject jsonData = new JSONObject(data);
            jsonParam = jsonData;
            return jsonData;
        }else
        {
            Toast.makeText(ResultActivity.this, "Oops! There was a Problem", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public class Read extends AsyncTask<String, String, JSONObject>{

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                json = Weather();
                return json;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonData) {
            try {

                //Get Form params from Main Activity

                Intent degreeIntent=getIntent();
                String degree = degreeIntent.getExtras().getString("DEGREE");
                Intent cityIntent=getIntent();
                String city = cityIntent.getExtras().getString("CITY");
                Intent stateIntent=getIntent();
                String state = stateIntent.getExtras().getString("STATE");


                //get JSON data

                JSONObject currently = jsonData.getJSONObject("currently");
                JSONObject daily = jsonData.getJSONObject("daily");
                JSONArray dailyDataArr = daily.getJSONArray("data");
                JSONObject dailyData = dailyDataArr.getJSONObject(0);

                //Extract values from JSON

                String tz = jsonData.getString("timezone"); //get timezone

                int srTime = Integer.parseInt(dailyData.getString("sunriseTime"));
                int ssTime = Integer.parseInt(dailyData.getString("sunsetTime"));

                Date dateSR = new Date(srTime * 1000L);
                Date dateSS = new Date(ssTime * 1000L);

                DateFormat format = new SimpleDateFormat("h:mm a");
                format.setTimeZone(TimeZone.getTimeZone(tz));

                String sunrise = format.format(dateSR);
                String sunset = format.format(dateSS);

                lat = jsonData.getString("latitude");
                lon = jsonData.getString("longitude");

                float tempFloat = Float.parseFloat(currently.getString("temperature"));
                tempFloat=Math.round(tempFloat);
                String temp = (int)tempFloat+degSym;
                String summ = currently.getString("summary");
                desc_summ = summ + ", " + temp;
                summ = summ + " in " + city + ", " + state;
                main_summ = summ;
                float precip = Float.parseFloat(currently.getString("precipIntensity"));
                String precipitation = "";
                float rainFloat = Float.parseFloat(currently.getString("precipProbability"));
                rainFloat=Math.round(rainFloat*100);
                String rain = (int)rainFloat + " %";
                float windFloat = Float.parseFloat(currently.getString("windSpeed"));
                String wind="";
                float dewFloat = Float.parseFloat(currently.getString("dewPoint"));
                String dew = String.format("%.2f",dewFloat) + degSym;
                float humidFloat = Float.parseFloat(currently.getString("humidity"));
                humidFloat=Math.round(humidFloat*100);
                String humid = (int)humidFloat + " %";
                float tempMinFloat = Float.parseFloat(dailyData.getString("temperatureMin"));
                float tempMaxFloat = Float.parseFloat(dailyData.getString("temperatureMax"));
                tempMaxFloat = Math.round(tempMaxFloat);
                tempMinFloat = Math.round(tempMinFloat);
                String tempMax = "H:"+(int)tempMaxFloat+degSym;
                String tempMin = "L:"+(int)tempMinFloat+degSym;
                String tempMinMax = tempMin + " | " + tempMax;
                Float visFloat = Float.parseFloat(currently.getString("visibility"));
                String vis = "";
                String icon = currently.getString("icon");
                ImageView imageView = (ImageView) findViewById(R.id.summ_image);
                TextView sup = (TextView) findViewById(R.id.sup);

                //Check for Celsius or Fahrenheit

                if(degree.equals("Fahrenheit")){
                    wind = String.format("%.2f",windFloat) + " mph";
                    vis = String.format("%.2f",visFloat) + " mi";
                    sup.setText("F");
                    dew = dew+"F";
                    desc_summ = desc_summ + "F";
                    degreeParam = "F";

                }
                else{
                    precip/=25.4;
                    wind = String.format("%.2f",windFloat) + " m/s";
                    vis = String.format("%.2f",visFloat) + " km";
                    sup.setText("C");
                    dew = dew+"C";
                    desc_summ = desc_summ + "C";
                    degreeParam = "C";
                }

                //Summary icon

                if(icon.equals("clear-day")){
                    imageView.setImageResource(R.drawable.clear);
                    main_icon="http://cs-server.usc.edu:45678/hw/hw8/images/clear.png";
                }
                else if(icon.equals("clear-night")){
                    imageView.setImageResource(R.drawable.clear_night);
                    main_icon="http://cs-server.usc.edu:45678/hw/hw8/images/clear_night.png";
                }
                else if(icon.equals("rain")){
                    imageView.setImageResource(R.drawable.rain);
                    main_icon="http://cs-server.usc.edu:45678/hw/hw8/images/clear.png";
                }
                else if(icon.equals("snow")){
                    imageView.setImageResource(R.drawable.snow);
                    main_icon="http://cs-server.usc.edu:45678/hw/hw8/images/rain.png";
                }
                else if(icon.equals("sleet")){
                    imageView.setImageResource(R.drawable.sleet);
                    main_icon="http://cs-server.usc.edu:45678/hw/hw8/images/sleet.png";
                }
                else if(icon.equals("wind")){
                    imageView.setImageResource(R.drawable.wind);
                    main_icon="http://cs-server.usc.edu:45678/hw/hw8/images/wind.png";
                }
                else if(icon.equals("fog")){
                    imageView.setImageResource(R.drawable.fog);
                    main_icon="http://cs-server.usc.edu:45678/hw/hw8/images/fog.png";

                }
                else if(icon.equals("partly-cloudy-day")){
                    imageView.setImageResource(R.drawable.cloud_day);
                    main_icon="http://cs-server.usc.edu:45678/hw/hw8/images/cloud_day.png";
                }
                else if(icon.equals("partly-cloudy-night")){
                    imageView.setImageResource(R.drawable.cloud_night);
                    main_icon="http://cs-server.usc.edu:45678/hw/hw8/images/cloud_night.png";
                }
                else if(icon.equals("cloudy")){
                    imageView.setImageResource(R.drawable.cloudy);
                    main_icon="http://cs-server.usc.edu:45678/hw/hw8/images/cloudy.png";
                }


                //precipitation

                if(precip>=0 && precip<0.002) {
                    precipitation = "None";
                }
                else if(precip>=0.002 && precip<0.017)
                {
                    precipitation = "Very Light";
                }
                else if(precip>=0.017 && precip<0.1)
                {
                    precipitation = "Light";
                }
                else if(precip>=0.1 && precip<0.4){
                    precipitation = "Moderate";
                }
                else if(precip>=0.4){
                    precipitation = "Heavy";
                }
                else{
                    precipitation = "NA";
                }

                //Set text for display

                temperature.setText(temp);
                summary.setText(summ);
                tempLowHigh.setText(tempMinMax);
                precipVal.setText(precipitation);
                rainVal.setText(rain);
                windVal.setText(wind);
                dewVal.setText(dew);
                humidVal.setText(humid);
                visVal.setText(vis);
                sunriseVal.setText(sunrise);
                sunsetVal.setText(sunset);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
