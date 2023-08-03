package com.example.loginapp.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;

import com.example.loginapp.R;

public class SingUpSystemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up_system);
        setSupportActionBar(findViewById(R.id.toolbar3));
        getSupportActionBar().setTitle("Registrar un Usuario");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu miMenu) {
        getMenuInflater().inflate(R.menu.toolbar_empty, miMenu);
        return true;
    }
}