package com.example.loginapp.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.loginapp.R;
import com.example.loginapp.database.DatabaseUsers;
import com.example.loginapp.model.RegularExpressions;
import com.example.loginapp.model.User;

import java.util.Random;

public class SingUpSystemActivity extends AppCompatActivity {

    private EditText editTextEmailSUS, editTextNameSUS, editTextNumberSUS, editTextPasswordSUS, editTextRPasswordSUS;
    private Button buttonSingUPSystem;
    private RegularExpressions regularExp;
    private String email, name, number, password, rPassword;
    private DatabaseUsers databaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up_system);
        setSupportActionBar(findViewById(R.id.toolbar3));
        getSupportActionBar().setTitle("Registrar un Usuario");

        editTextEmailSUS = findViewById(R.id.editTextEmailSUS);
        editTextNameSUS = findViewById(R.id.editTextNameSUS);
        editTextNumberSUS = findViewById(R.id.editTextNumberSUS);
        editTextPasswordSUS = findViewById(R.id.editTextPasswordSUS);
        editTextRPasswordSUS = findViewById(R.id.editTextRPasswordSUS);
        buttonSingUPSystem = findViewById(R.id.buttonSingUpSystem);
        regularExp = new RegularExpressions();
        databaseUsers = new DatabaseUsers(this);

        buttonSingUPSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = editTextEmailSUS.getText().toString().trim();
                name = editTextNameSUS.getText().toString().trim();
                number = editTextNumberSUS.getText().toString().trim();
                password = editTextPasswordSUS.getText().toString();
                rPassword = editTextRPasswordSUS.getText().toString();

                if (email.isEmpty() | name.isEmpty() | number.isEmpty() | password.isEmpty() | rPassword.isEmpty()) {
                    Toast.makeText(SingUpSystemActivity.this, "Debes de llenar toda la información", Toast.LENGTH_SHORT).show();
                } else {
                    if (regularExp.validateEmail(email) && regularExp.validateName(name) && regularExp.validatePhone(number) && regularExp.validatePassword(password) && password.equals(rPassword)) {
                        if (databaseUsers.searchEmail(email)) {
                            Toast.makeText(SingUpSystemActivity.this, "Correo no disponible", Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                Random random = new Random();
                                int randomNumber = random.nextInt(900000000) + 1000000000;
                                String nameKeyAlias = Integer.toString(randomNumber);
                                User newUser = new User(0, name, number, email, password, nameKeyAlias, null);
                                /*INSERTAMOS LA INFORMACION*/
                                if (databaseUsers.insertUser(newUser)) {
                                    Toast.makeText(SingUpSystemActivity.this, "Felicidades la cuenta fue creada", Toast.LENGTH_LONG).show();
                                    Thread.sleep(4000);
                                    Intent next = new Intent(SingUpSystemActivity.this, SystemAdminActivity.class);
                                    startActivity(next);
                                } else {
                                    Toast.makeText(SingUpSystemActivity.this, "Error al crear llave", Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                            }
                        }
                    } else {
                        if (!regularExp.validateEmail(email)) {
                            Toast.makeText(SingUpSystemActivity.this, "Email no valido", Toast.LENGTH_SHORT).show();
                        }
                        if (!regularExp.validateName(name)) {
                            Toast.makeText(SingUpSystemActivity.this, "El nombre debe de contener al menos 3 cáracteres", Toast.LENGTH_SHORT).show();
                        }
                        if (!regularExp.validatePhone(number)) {
                            Toast.makeText(SingUpSystemActivity.this, "Numero no valido", Toast.LENGTH_SHORT).show();
                        }
                        if (!regularExp.validatePassword(password)) {
                            Toast.makeText(SingUpSystemActivity.this, "Password no valido", Toast.LENGTH_SHORT).show();
                            Toast.makeText(SingUpSystemActivity.this, "Debe tener un Máyuscula, una Mínuscula, un Número y un Signo", Toast.LENGTH_SHORT).show();
                        }
                        if (!password.equals(rPassword)) {
                            Toast.makeText(SingUpSystemActivity.this, "La contraseña debe coincidir", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu miMenu) {
        getMenuInflater().inflate(R.menu.toolbar_empty, miMenu);
        return true;
    }
}