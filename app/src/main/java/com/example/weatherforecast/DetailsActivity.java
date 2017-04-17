package com.example.weatherforecast;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class DetailsActivity extends Activity{

    String temp;
    JSONArray hourlyDataArr;
    JSONObject hourlyData;
    JSONArray dailyDataArr;
    JSONObject dailyData;
    final String degSym = "\u00b0";
    String tz;
    int time;
    int dailydate;
    Date date;
    Date date2;
    DateFormat format;
    String displayTime;
    String icon;
    String icon2;
    TextView moreDetailsText;
    ViewSwitcher viewSwitcher;
    TableLayout next24Table;
    TableLayout next7Table;
    String tempMinMax;
    String dateDispaly = "";
    final List<String> weekDay = Arrays.asList("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");
    final List<String> monthName = Arrays.asList("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);


        //Get Json from result activity

        Bundle b = getIntent().getExtras();
        try {
            JSONObject json = new JSONObject(b.getString("jsondata"));
            JSONObject daily = json.getJSONObject("daily");
            dailyDataArr = daily.getJSONArray("data");
            JSONObject hourly = json.getJSONObject("hourly");
            hourlyDataArr= hourly.getJSONArray("data");


            tz = json.getString("timezone"); //get timezone
            format = new SimpleDateFormat("hh:mm a");
            format.setTimeZone(TimeZone.getTimeZone(tz));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        next24Table = (TableLayout) findViewById(R.id.maintable);
        next7Table = (TableLayout) findViewById(R.id.maintable2);
        next24Table.setVisibility(View.VISIBLE);
        next7Table.setVisibility(View.GONE);
        TextView tv1;
        TextView tv2;
        TextView tv3;
        TextView tv4;
        TextView tvblank;
        ImageView iv;
        ImageView iv2;
        final TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT,TableRow.LayoutParams.FILL_PARENT);
        final TableRow.LayoutParams trlp = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.33f);


        //Get Params from Result Activity

        Intent degreeIntent=getIntent();
        String degreeParam = degreeIntent.getExtras().getString("DEGREE");
        degreeParam = degSym + degreeParam ;
        Intent cityIntent=getIntent();
        String cityParam = cityIntent.getExtras().getString("CITY");
        Intent stateIntent=getIntent();
        String stateParam = stateIntent.getExtras().getString("STATE");
        String moreDetails = "More Details for " + cityParam + ", " + stateParam;
        moreDetailsText = (TextView) findViewById(R.id.moreDetails);
        moreDetailsText.setText(moreDetails);
        TableRow rowhead = new TableRow(this);
        rowhead.setLayoutParams(lp);
        rowhead.setMinimumHeight(100);
        rowhead.setBackgroundColor(Color.CYAN);
        TextView th1 = new TextView(this);
        TextView th2 = new TextView(this);
        TextView th3 = new TextView(this);
        String tmp = "Temp(" + degreeParam + ")";
        th1.setText("Time");
        th2.setText("Summary");
        th3.setText(tmp);
        th3.setGravity(Gravity.RIGHT);
        th1.setTypeface(Typeface.DEFAULT_BOLD);
        th2.setTypeface(Typeface.DEFAULT_BOLD);
        th3.setTypeface(Typeface.DEFAULT_BOLD);
        th1.setTextSize(18);
        th2.setTextSize(18);
        th3.setTextSize(18);
        th1.setPadding(10, 0, 0, 0);
        th3.setPadding(0,0,15,0);
        th1.setLayoutParams(trlp);
        th3.setLayoutParams(trlp);
        rowhead.addView(th1);
        rowhead.addView(th2);
        rowhead.addView(th3);
        next24Table.addView(rowhead);


        //Populate Next 24 Table

        for (int i = 0; i <24; i++) {

            try {
                hourlyData = hourlyDataArr.getJSONObject(i);
                icon = hourlyData.getString("icon");
                time = Integer.parseInt(hourlyData.getString("time"));
                date = new Date(time * 1000L);
                displayTime = format.format(date);
                float tempFloat = Float.parseFloat(hourlyData.getString("temperature"));
                tempFloat=Math.round(tempFloat);
                temp = (int)tempFloat+"";

            } catch (JSONException e) {
                e.printStackTrace();
            }
            TableRow row= new TableRow(this);
            row.setLayoutParams(lp);
            row.setMinimumHeight(100);

            if(i%2==0) {
                row.setBackgroundColor(Color.LTGRAY);
            }
            else
            {
                row.setBackgroundColor(Color.WHITE);
            }
            iv= new ImageView(this);
            if(icon.equals("clear-day")){
                iv.setImageResource(R.drawable.clear);
            }
            else if(icon.equals("clear-night")){
                iv.setImageResource(R.drawable.clear_night);
            }
            else if(icon.equals("rain")){
                iv.setImageResource(R.drawable.rain);
            }
            else if(icon.equals("snow")){
                iv.setImageResource(R.drawable.snow);
            }
            else if(icon.equals("sleet")){
                iv.setImageResource(R.drawable.sleet);
            }
            else if(icon.equals("wind")){
                iv.setImageResource(R.drawable.wind);

            }
            else if(icon.equals("fog")){
                iv.setImageResource(R.drawable.fog);
            }
            else if(icon.equals("partly-cloudy-day")){
                iv.setImageResource(R.drawable.cloud_day);

            }
            else if(icon.equals("partly-cloudy-night")){
                iv.setImageResource(R.drawable.cloud_night);
            }
            else if(icon.equals("cloudy")){
                iv.setImageResource(R.drawable.cloudy);
            }

            iv.setLayoutParams(new TableRow.LayoutParams(80, 80));
            tv1 = new TextView(this);
            tv2 = new TextView(this);
            tv2.setGravity(Gravity.RIGHT);
            tv1.setText(displayTime);
            tv2.setText(temp);
            tv1.setTextSize(18);
            tv2.setTextSize(18);
            tv1.setTypeface(Typeface.DEFAULT_BOLD);
            tv1.setPadding(10,0,0,0);
            tv2.setPadding(0,0,15,0);
            tv1.setLayoutParams(trlp);
            tv2.setLayoutParams(trlp);
            row.addView(tv1);
            row.addView(iv);
            row.addView(tv2);
            next24Table.addView(row);
        }

        TableRow blankRow = new TableRow(this);
        blankRow.setLayoutParams(lp);
        TextView blk1 = new TextView(this);
        TextView blk2 = new TextView(this);
        blk1.setLayoutParams(trlp);
        blk2.setLayoutParams(trlp);
        blk1.setText("       ");
        blk2.setText("       ");
        ImageView plus_btn=new ImageView(this);
        plus_btn.setLayoutParams(new TableRow.LayoutParams(80, 80));
        plus_btn.setImageResource(R.drawable.plus);
        blankRow.addView(blk1);
        blankRow.addView(plus_btn);
        blankRow.addView(blk2);
        blankRow.setBackgroundColor(Color.LTGRAY);
        next24Table.addView(blankRow);

        plus_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                next24Table.removeViewAt(25);

                TextView tv1;
                TextView tv2;
                ImageView iv;
                for (int k = 24; k <48; k++) {

                    try {
                        hourlyData = hourlyDataArr.getJSONObject(k);
                        icon = hourlyData.getString("icon");
                        time = Integer.parseInt(hourlyData.getString("time"));
                        date = new Date(time * 1000L);
                        displayTime = format.format(date);
                        float tempFloat = Float.parseFloat(hourlyData.getString("temperature"));
                        tempFloat = Math.round(tempFloat);
                        temp = (int) tempFloat + "";

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    TableRow row = new TableRow(next24Table.getContext());
                    row.setLayoutParams(lp);
                    row.setMinimumHeight(100);

                    if (k % 2 == 0) {
                        row.setBackgroundColor(Color.LTGRAY);
                    } else {
                        row.setBackgroundColor(Color.WHITE);
                    }
                    iv = new ImageView(next24Table.getContext());
                    if (icon.equals("clear-day")) {
                        iv.setImageResource(R.drawable.clear);
                    } else if (icon.equals("clear-night")) {
                        iv.setImageResource(R.drawable.clear_night);
                    } else if (icon.equals("rain")) {
                        iv.setImageResource(R.drawable.rain);
                    } else if (icon.equals("snow")) {
                        iv.setImageResource(R.drawable.snow);
                    } else if (icon.equals("sleet")) {
                        iv.setImageResource(R.drawable.sleet);
                    } else if (icon.equals("wind")) {
                        iv.setImageResource(R.drawable.wind);

                    } else if (icon.equals("fog")) {
                        iv.setImageResource(R.drawable.fog);
                    } else if (icon.equals("partly-cloudy-day")) {
                        iv.setImageResource(R.drawable.cloud_day);

                    } else if (icon.equals("partly-cloudy-night")) {
                        iv.setImageResource(R.drawable.cloud_night);
                    } else if (icon.equals("cloudy")) {
                        iv.setImageResource(R.drawable.cloudy);
                    }

                    iv.setLayoutParams(new TableRow.LayoutParams(80, 80));
                    tv1 = new TextView(next24Table.getContext());
                    tv2 = new TextView(next24Table.getContext());
                    tv2.setGravity(Gravity.RIGHT);
                    tv1.setText(displayTime);
                    tv2.setText(temp);
                    tv1.setTextSize(18);
                    tv2.setTextSize(18);
                    tv1.setTypeface(Typeface.DEFAULT_BOLD);
                    tv1.setPadding(10, 0, 0, 0);
                    tv2.setPadding(0, 0, 15, 0);
                    tv1.setLayoutParams(trlp);
                    tv2.setLayoutParams(trlp);
                    row.addView(tv1);
                    row.addView(iv);
                    row.addView(tv2);
                    next24Table.addView(row);
                }

            }
        });

        // Populate next 7 Table


        for(int j=1;j<dailyDataArr.length();j++) {
            try {
                dailyData = dailyDataArr.getJSONObject(j);
                icon2 = dailyData.getString("icon");
                float tempMinFloat = Float.parseFloat(dailyData.getString("temperatureMin"));
                float tempMaxFloat = Float.parseFloat(dailyData.getString("temperatureMax"));
                tempMaxFloat = Math.round(tempMaxFloat);
                tempMinFloat = Math.round(tempMinFloat);
                String tempMax = "Max: " + (int) tempMaxFloat + degreeParam;
                String tempMin = "Min: " + (int) tempMinFloat + degreeParam;
                tempMinMax = tempMin + " | " + tempMax;
                dailydate = Integer.parseInt(dailyData.getString("time"));
                date2 = new Date(dailydate * 1000L);
                Calendar cal = Calendar.getInstance();
                cal.setTime(date2);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int day_of_week = cal.get(Calendar.DAY_OF_WEEK);
                day_of_week-=1;
                dateDispaly = weekDay.get(day_of_week) + ", " + monthName.get(month) + " " + day;

            } catch (JSONException e) {
                e.printStackTrace();
            }


            TableRow row2 = new TableRow(this);
            TableRow row= new TableRow(this);
            TableRow blank = new TableRow(this);
            blank.setLayoutParams(lp);
            row.setLayoutParams(lp);
            row2.setLayoutParams(lp);
            row2.setMinimumHeight(100);
            switch(j) {
                case 1:
                    row.setBackgroundColor(Color.parseColor("#FDD835"));
                    row2.setBackgroundColor(Color.parseColor("#FDD835"));
                    break;
                case 2:
                    row.setBackgroundColor(Color.parseColor("#80DEEA"));
                    row2.setBackgroundColor(Color.parseColor("#80DEEA"));
                    break;
                case 3:
                    row.setBackgroundColor(Color.parseColor("#F8BBD0"));
                    row2.setBackgroundColor(Color.parseColor("#F8BBD0"));
                    break;
                case 4:
                    row.setBackgroundColor(Color.parseColor("#CCFF90"));
                    row2.setBackgroundColor(Color.parseColor("#CCFF90"));
                    break;
                case 5:
                    row.setBackgroundColor(Color.parseColor("#EF9A9A"));
                    row2.setBackgroundColor(Color.parseColor("#EF9A9A"));
                    break;
                case 6:
                    row.setBackgroundColor(Color.parseColor("#E6EE9C"));
                    row2.setBackgroundColor(Color.parseColor("#E6EE9C"));
                    break;
                case 7:
                    row.setBackgroundColor(Color.parseColor("#B39DDB"));
                    row2.setBackgroundColor(Color.parseColor("#B39DDB"));
                    break;
            }
            iv2= new ImageView(this);
            if(icon2.equals("clear-day")){
                iv2.setImageResource(R.drawable.clear);
            }
            else if(icon2.equals("clear-night")){
                iv2.setImageResource(R.drawable.clear_night);
            }
            else if(icon2.equals("rain")){
                iv2.setImageResource(R.drawable.rain);
            }
            else if(icon2.equals("snow")){
                iv2.setImageResource(R.drawable.snow);
            }
            else if(icon2.equals("sleet")){
                iv2.setImageResource(R.drawable.sleet);
            }
            else if(icon2.equals("wind")){
                iv2.setImageResource(R.drawable.wind);

            }
            else if(icon2.equals("fog")){
                iv2.setImageResource(R.drawable.fog);

            }
            else if(icon2.equals("partly-cloudy-day")){
                iv2.setImageResource(R.drawable.cloud_day);

            }
            else if(icon2.equals("partly-cloudy-night")){
                iv2.setImageResource(R.drawable.cloud_night);
            }
            else if(icon2.equals("cloudy")){
                iv2.setImageResource(R.drawable.cloudy);
            }

            iv2.setLayoutParams(new TableRow.LayoutParams(80, 80,2));
            tvblank = new TextView(this);
            tvblank.setText(" ");
            tvblank.setTextSize(5);
            blank.addView(tvblank);
            tv3 = new TextView(this);
            tv3.setText(dateDispaly);
            tv3.setTypeface(Typeface.DEFAULT_BOLD);
            tv3.setTextSize(18);
            tv4 = new TextView(this);
            tv4.setText(tempMinMax);
            tv4.setTextSize(18);
            tv3.setPadding(10, 10, 0, 0);
            iv2.setPadding(0,15,0,0);
            tv4.setPadding(10, 0, 0, 0);
            row2.addView(tv4);
            row.addView(tv3);
            row.addView(iv2);
            next7Table.addView(row);
            next7Table.addView(row2);
            next7Table.addView(blank);
        }


        final Button button24 = (Button) findViewById(R.id.next24_button);
        button24.setBackgroundColor(Color.parseColor("#039BE5"));
        final Button button7 = (Button) findViewById(R.id.next7_button);


        button24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                next24Table.setVisibility(View.VISIBLE);
                next7Table.setVisibility(View.GONE);
                button24.setBackgroundColor(Color.parseColor("#039BE5"));
                button7.setBackgroundColor(Color.LTGRAY);
            }
        });


        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                next24Table.setVisibility(View.GONE);
                next7Table.setVisibility(View.VISIBLE);
                button7.setBackgroundColor(Color.parseColor("#039BE5"));
                button24.setBackgroundColor(Color.LTGRAY);
            }
        });


    }

}
