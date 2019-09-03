package com.marco.socketadapter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button buttonAdapters;
    private Button buttonTags;
    private Button buttonSetup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonAdapters = findViewById(R.id.button_adapters);
        buttonTags = findViewById(R.id.button_tags);
        buttonSetup = findViewById(R.id.button_setup);

        buttonAdapters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callActivityAdapters();
            }
        });

        buttonTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callActivityTags();
            }
        });

        buttonSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callActivitySetup();
            }
        });
    }

    private void callActivityAdapters(){

        Intent intent = new Intent(MainActivity.this,Adapters.class);
        startActivity(intent);
    }
    private void callActivityTags(){

        Intent intent = new Intent(MainActivity.this,Tags.class);
        startActivity(intent);
    }

    private void callActivitySetup(){

        Intent intent = new Intent(MainActivity.this,Setup.class);
        startActivity(intent);
    }
}
