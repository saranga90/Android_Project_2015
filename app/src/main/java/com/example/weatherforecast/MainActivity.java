package com.example.weatherforecast;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MainActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //SEARCH Button

        Button searchBtn = (Button) findViewById(R.id.s_button);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //url for php

                String url = "http://cs-server.usc.edu:26071/geo.php";

                EditText streetField = (EditText) findViewById(R.id.streetText);
                EditText cityField = (EditText) findViewById(R.id.cityText);
                Spinner stateSpinner = (Spinner) findViewById(R.id.state_spinner);
                RadioGroup degreeRadio = (RadioGroup) findViewById(R.id.radioGroup);


                //Alert Messages for Validation

                String msg1 = "Please enter a Street";
                String msg2 = "Please enter a City";
                String msg3 = "Please enter a State";
                TextView streetAlert = (TextView) findViewById(R.id.street_alert_msg);
                TextView cityAlert = (TextView) findViewById(R.id.city_alert_msg);
                TextView stateAlert = (TextView) findViewById(R.id.state_alert_msg);
                streetAlert.setText(msg1);
                cityAlert.setText(msg2);
                stateAlert.setText(msg3);
                streetAlert.setVisibility(View.INVISIBLE);
                cityAlert.setVisibility(View.INVISIBLE);
                stateAlert.setVisibility(View.INVISIBLE);

                String street = "";
                String city = "";
                String state = "";
                String degree = "";
                String cityParam = cityField.getText().toString().trim();


                //Get values from Form

                try {
                    street = URLEncoder.encode(streetField.getText().toString().trim(), "UTF-8");
                    city = URLEncoder.encode(cityField.getText().toString().trim(), "UTF-8");
                    state = URLEncoder.encode(stateSpinner.getSelectedItem().toString().trim(), "UTF-8");
                    degree = URLEncoder.encode(((RadioButton) findViewById(degreeRadio.getCheckedRadioButtonId())).getText().toString(), "UTF-8");

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


                //Form Validation

                boolean flag = true;

                if (street.equals("")) {
                    streetAlert.setVisibility(View.VISIBLE);
                    flag = false;
                }
                if (city.equals("")) {
                    cityAlert.setVisibility(View.VISIBLE);
                    flag = false;
                }

                if (state.equals("Select")) {
                    stateAlert.setVisibility(View.VISIBLE);
                    flag = false;
                }


                // If Valid, Call Result Activity

                if (flag) {

                    String urlString = url + "?street=" + street + "&city=" + city + "&state=" + state + "&degree=" + degree;
                    Intent intent = new Intent(v.getContext(), ResultActivity.class);
                    intent.putExtra("URL", urlString);
                    intent.putExtra("CITY", cityParam);
                    intent.putExtra("STATE", state);
                    intent.putExtra("DEGREE", degree);
                    startActivity(intent);
                }
            }

        });


        //CLEAR Button

        Button clearBtn = (Button) findViewById(R.id.c_button);
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView streetAlert = (TextView) findViewById(R.id.street_alert_msg);
                TextView cityAlert = (TextView) findViewById(R.id.city_alert_msg);
                TextView stateAlert = (TextView) findViewById(R.id.state_alert_msg);
                streetAlert.setVisibility(View.INVISIBLE);
                cityAlert.setVisibility(View.INVISIBLE);
                stateAlert.setVisibility(View.INVISIBLE);
                EditText streetField = (EditText) findViewById(R.id.streetText);
                EditText cityField = (EditText) findViewById(R.id.cityText);
                Spinner stateSpinner = (Spinner) findViewById(R.id.state_spinner);
                RadioGroup degreeRadio = (RadioGroup) findViewById(R.id.radioGroup);
                streetField.setText("");
                cityField.setText("");
                stateSpinner.setSelection(0);
                degreeRadio.check(R.id.f_radio);
            }
        });


        //About Button

        Button aboutBtn = (Button) findViewById(R.id.about_button);
        aboutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), AboutActivity.class);
                startActivity(intent);
            }
        });


        //Disclaimer Link

        ImageButton forecastButton = (ImageButton)findViewById(R.id.forecastButton);
        forecastButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://forecast.io/"));
                startActivity(browserIntent);
            }

        });
    }
}
