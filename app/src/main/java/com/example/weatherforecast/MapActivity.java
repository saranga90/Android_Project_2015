package com.example.weatherforecast;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import android.widget.Toast;


import com.hamweather.aeris.communication.AerisEngine;

public class MapActivity extends ActionBarActivity {
    private  String lat;
    private  String lon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);




        AerisEngine.initWithKeys("aqsxdwwJfl9bQW4gooIDd", "rzvTtlUJY5ltem8fqbdfKoSXr0fbyUbTu60AbDgr", this);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        MapFragment myFragment = new MapFragment();
        // FragmentMap myFragment = new FragmentMap();
        Bundle bundle = getIntent().getExtras();
        myFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.MapFrame, myFragment);
        fragmentTransaction.commit();
    }

}

