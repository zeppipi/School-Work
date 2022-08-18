package com.example.movieapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {

    private String savedTitle = "";
    private int savedYear = 0;
    private String savedCountry = "";
    private String savedGenre = "";
    private int savedCost = 0;
    private String savedKeyword = "";

    private EditText titleText;
    private EditText yearInt;
    private EditText countryText;
    private EditText genreText;
    private EditText costInt;
    private EditText keywordsText;

    SharedPreferences persistentInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //make space for persistent information
        persistentInfo = getPreferences(Context.MODE_PRIVATE);

        //reference all text fields
        titleText = findViewById(R.id.titleInput);
        yearInt = findViewById(R.id.yearInput);
        countryText = findViewById(R.id.countryInput);
        genreText = findViewById(R.id.genreInput);
        costInt = findViewById(R.id.costInput);
        keywordsText = findViewById(R.id.keywordsInput);

        //SMS permissions
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, 0);

        //Create and instantiate the broadcast receiver and register the broadcast handler
        theBroadcastReceiver broadcastReceiver = new theBroadcastReceiver();
        Log.d("debug", "receiver made");
        registerReceiver(broadcastReceiver, new IntentFilter(SMSReceiver.SMS_FILTER));

        Log.d("debug", "onCreate");
    }

    class theBroadcastReceiver extends BroadcastReceiver{

        // This function will run everytime it receives a broadcast
        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getStringExtra(SMSReceiver.SMS_KEY);

            //tokenize the received message
            StringTokenizer token = new StringTokenizer(msg, ";");
            String msgTitle = token.nextToken();
            String msgYear = token.nextToken();
            String msgCountry = token.nextToken();
            String msgGenre = token.nextToken();
            String msgCost = token.nextToken();
            String msgKeywords = token.nextToken();

            //update UI
            titleText.setText(msgTitle);
            yearInt.setText(msgYear);
            countryText.setText(msgCountry);
            genreText.setText(msgGenre);
            costInt.setText(msgCost);
            keywordsText.setText(msgKeywords);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        EditText text = findViewById(R.id.genreInput);

        //lowercasing the genre when orientation changes
        String stringText = text.getText().toString();
        outState.putString(R.id.genreInput+"", stringText.toLowerCase());

        Log.d("debug", "onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle inState){
        super.onRestoreInstanceState(inState);
        EditText titleText = findViewById(R.id.titleInput);

        //uppercasing the title when orientation changes
        String titleStringText = titleText.getText().toString();
        inState.putString(R.id.titleInput+"", titleStringText.toUpperCase());
        titleText.setText(inState.getString(R.id.titleInput+""));

        //write the changes onto the text fields
        EditText genreText = findViewById(R.id.genreInput);
        genreText.setText(inState.getString(R.id.genreInput+""));

        Log.d("debug", "onRestoreInstanceState");
    }

    @Override
    protected void onStop() {
        super.onStop();



        Log.d("debug", "onStop");
    }

    public void addMovieCLick(View view){
        Context context = getApplicationContext();

        //receive all of the strings currently written in the text fields
        savedTitle = titleText.getText().toString();
        savedYear = Integer.parseInt(yearInt.getText().toString());
        savedCountry = countryText.getText().toString();
        savedGenre = genreText.getText().toString();
        savedCost = Integer.parseInt(costInt.getText().toString());
        savedKeyword = keywordsText.getText().toString();

        //put all of those in the persistent info
        SharedPreferences.Editor editor = persistentInfo.edit();
        editor.putString(R.id.titleInput+"",savedTitle);
        editor.putInt(R.id.yearInput+"", savedYear);
        editor.putString(R.id.countryInput+"", savedCountry);
        editor.putString(R.id.genreInput+"", savedGenre);
        editor.putInt(R.id.costInput+"", savedCost);
        editor.putString(R.id.keywordsInput+"", savedKeyword);
        editor.apply();

        //toast logic to tell if a movie was actually added or not
        String toastTitle;
        if(savedTitle.length() == 0){
            toastTitle = "No movie was added";
        } else{
            toastTitle = "The movie: " + "'" + savedTitle + "' was added";
        }

        Toast bread = Toast.makeText(context,toastTitle,Toast.LENGTH_SHORT);
        bread.show();

        Log.d("debug", "addMovieCLick");
    }

    @Override
    protected void onStart() {
        super.onStart();

        //write down all of the saved info onto the text fields
        titleText.setText(persistentInfo.getString(R.id.titleInput+"", ""));

        String yearString = persistentInfo.getInt(R.id.yearInput+"",0) + "";
        yearInt.setText(yearString);

        countryText.setText(persistentInfo.getString(R.id.countryInput+"",""));
        genreText.setText(persistentInfo.getString(R.id.genreInput+"",""));

        String costString = persistentInfo.getInt(R.id.costInput+"",0)+"";
        costInt.setText(costString);

        keywordsText.setText(persistentInfo.getString(R.id.keywordsInput+"",""));

        Log.d("debug", "onStart");
    }

    public void doubleCost(View view){
        Context context = getApplicationContext();

        EditText text = findViewById(R.id.costInput);
        String doubleCostText = (Integer.parseInt(text.getText().toString())*2)+"";

        text.setText(doubleCostText);

        Toast bread = Toast.makeText(context,"Movie cost doubled",Toast.LENGTH_SHORT);
        bread.show();

        Log.d("debug", "doubleCost");
    }

    public void clear(View view){
        Context context = getApplicationContext();
        ArrayList<EditText> textFields = new ArrayList<>();

        //the horror
        textFields.add(titleText);
        textFields.add(yearInt);
        textFields.add(countryText);
        textFields.add(genreText);
        textFields.add(costInt);
        textFields.add(keywordsText);

        //loops through all the fields and deletes them
        for(int i = 0; i < textFields.size(); i++){
            textFields.get(i).getText().clear();
        }

        Toast bread = Toast.makeText(context,"Movie info deleted",Toast.LENGTH_SHORT);
        bread.show();

        Log.d("debug", "clear");
    }

    public void removePersistentData(View view){
        Context context = getApplicationContext();

        //deletes all persistent info
        SharedPreferences.Editor editor = persistentInfo.edit();
        editor.clear();
        editor.commit();

        //tells the user about it
        Toast bread = Toast.makeText(context, "Saved info deleted", Toast.LENGTH_SHORT);
        bread.show();
    }

}