package com.example.engdesignapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void doSomething(View view) {

        if(view.getId() == R.id.goals_button){
            Intent i2 = new Intent(this, Goals.class);
            startActivity(i2);
        }

        if(view.getId() == R.id.statistics_button){
            Intent i1 = new Intent(this, Statistics.class);
            startActivity(i1);
        }

        if(view.getId() == R.id.login_button){
            Intent i3 = new Intent(this, Login.class);
            startActivity(i3);
        }
    }
}