package com.example.loginapp.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.example.loginapp.model.CustomAdapter;
import com.example.loginapp.R;
import com.example.loginapp.model.User;
import com.example.loginapp.database.DatabaseUsers;

import java.util.ArrayList;
import java.util.List;

public class SystemAdminActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CustomAdapter customAdapter;
    private List<User> dataList = new ArrayList<>();
    private DatabaseUsers databaseUsers;
    private Button buttonPlus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system);
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setTitle("Blue Water System");

        buttonPlus = findViewById(R.id.buttonPlus);
        databaseUsers = new DatabaseUsers(this);

        //MAPEANDO COMPONENTE
        recyclerView = findViewById(R.id.rvDetalles);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        try {
            dataList = databaseUsers.listInformation();
        } catch (Exception e) {
        }

        if (!dataList.isEmpty()) {
            customAdapter = new CustomAdapter(dataList, this);
            recyclerView.setAdapter(customAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

        buttonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(SystemAdminActivity.this, SingUpSystemActivity.class);
                startActivity(next);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu miMenu) {
        getMenuInflater().inflate(R.menu.toolbar_main, miMenu);
        return true;
    }
}