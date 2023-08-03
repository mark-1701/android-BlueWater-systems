package com.example.loginapp.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.widget.EditText;

import com.example.loginapp.R;
import com.example.loginapp.database.DatabaseUsers;
import com.example.loginapp.model.User;

public class InformationViewActivity extends AppCompatActivity {
    EditText editTextIdVI, editTextNameVI, editTextNumberVI, editTextEmailVI, editTextPasswordVI;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_view);
        setSupportActionBar(findViewById(R.id.toolbar2));
        getSupportActionBar().setTitle("Editar Información");

        editTextIdVI = findViewById(R.id.editTextIdVI);
        editTextNameVI = findViewById(R.id.editTextNameVI);
        editTextNumberVI = findViewById(R.id.editTextNumberVI);
        editTextEmailVI = findViewById(R.id.editTextEmailVI);
        editTextPasswordVI = findViewById(R.id.editTextPasswordVI);
        user = DatabaseUsers.globalUser;

        editTextIdVI.setText(Integer.toString(user.getId()));
        editTextNameVI.setText(user.getName());
        editTextNumberVI.setText(user.getNumber());
        editTextEmailVI.setText(user.getEmail());
        editTextPasswordVI.setText(user.getPassword());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu miMenu) {
        getMenuInflater().inflate(R.menu.toolbar_empty, miMenu);
        return true;
    }
}