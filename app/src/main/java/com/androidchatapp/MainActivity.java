package com.androidchatapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences pref = getSharedPreferences("COOKIE", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        String restoredText = pref.getString("PHONE_NUMBER", "NEW_USER");

        if(restoredText.equals("NEW_USER"))
        {
            startActivity(new Intent(MainActivity.this,Register.class));
        }
        else
        {
            startActivity(new Intent(MainActivity.this,Users.class));
        }
    }
}
