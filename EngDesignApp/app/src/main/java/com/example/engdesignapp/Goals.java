package com.example.engdesignapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Goals extends AppCompatActivity {

    Button changeGoalButton;

    TextView goalValueTextView;

    EditText editGoalTextInput;


    private void writeToFile(String data,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("waterGoal", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.i("login activity", "File write failed: " + e.toString());
        }
    }

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
        setContentView(R.layout.activity_goals);

        goalValueTextView = (TextView)findViewById(R.id.goalValueTextView);
        changeGoalButton = (Button)findViewById(R.id.changeGoalButton);
        editGoalTextInput = (EditText)findViewById(R.id.editGoalTextInput);
        goalValueTextView.setText(readFromFile(getApplicationContext()) + " liters per day");
        changeGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newGoal = editGoalTextInput.getText().toString();
                writeToFile(newGoal, getApplicationContext());
                goalValueTextView.setText(newGoal + " liters per day");
            }
        });

    }
}