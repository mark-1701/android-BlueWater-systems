package com.example.loginapp.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.loginapp.R;
import com.example.loginapp.database.DatabaseUsers;
import com.example.loginapp.model.RegularExpressions;
import com.example.loginapp.model.User;

import java.util.Random;

public class SingUpActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextName, editTextNumber, editTextPassword, editTextRPassword;
    private Button buttonSingUP;
    private RegularExpressions regularExp;
    private String email, name, number, password, rPassword;
    private DatabaseUsers databaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        editTextEmail = findViewById(R.id.editTextEmailSU);
        editTextName = findViewById(R.id.editTextNameSU);
        editTextNumber = findViewById(R.id.editTextNumberSU);
        editTextPassword = findViewById(R.id.editTextPasswordSU);
        editTextRPassword = findViewById(R.id.editTextRPasswordSU);
        buttonSingUP = findViewById(R.id.buttonSingUp);
        regularExp = new RegularExpressions();
        databaseUsers = new DatabaseUsers(this);

        buttonSingUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = editTextEmail.getText().toString().trim();
                name = editTextName.getText().toString().trim();
                number = editTextNumber.getText().toString().trim();
                password = editTextPassword.getText().toString();
                rPassword = editTextRPassword.getText().toString();;

                if (email.isEmpty() | name.isEmpty() | number.isEmpty() | password.isEmpty() | rPassword.isEmpty()) {
                    Toast.makeText(SingUpActivity.this, "Debes de llenar toda la información", Toast.LENGTH_SHORT).show();
                } else {
                    if (regularExp.validateEmail(email) && regularExp.validateName(name) && regularExp.validatePhone(number) && regularExp.validatePassword(password) && password.equals(rPassword)) {
                        if (databaseUsers.searchEmail(email)) {
                            Toast.makeText(SingUpActivity.this, "Correo no disponible", Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                Random random = new Random();
                                int randomNumber = random.nextInt(900000000) + 1000000000;
                                String nameKeyAlias = Integer.toString(randomNumber);
                                User newUser = new User(0, name, number, email, password, nameKeyAlias, null);
                                /*INSERTAMOS LA INFORMACION*/
                                if (databaseUsers.insertUser(newUser)) {
                                    Toast.makeText(SingUpActivity.this, "Felicidades tu cuenta fue creada", Toast.LENGTH_LONG).show();
                                    Thread.sleep(4000);
                                    Intent next = new Intent(SingUpActivity.this, MainActivity.class);
                                    startActivity(next);
                                } else {
                                    Toast.makeText(SingUpActivity.this, "Error al crear llave", Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                            }
                        }
                    } else {
                        if (!regularExp.validateEmail(email)) {
                            Toast.makeText(SingUpActivity.this, "Email no valido", Toast.LENGTH_SHORT).show();
                        }
                        if (!regularExp.validateName(name)) {
                            Toast.makeText(SingUpActivity.this, "El nombre debe de contener al menos 3 cáracteres", Toast.LENGTH_SHORT).show();
                        }
                        if (!regularExp.validatePhone(number)) {
                            Toast.makeText(SingUpActivity.this, "Numero no valido", Toast.LENGTH_SHORT).show();
                        }
                        if (!regularExp.validatePassword(password)) {
                            Toast.makeText(SingUpActivity.this, "Password no valido", Toast.LENGTH_SHORT).show();
                            Toast.makeText(SingUpActivity.this, "Debe tener un Máyuscula, una Mínuscula, un Número y un Signo", Toast.LENGTH_SHORT).show();
                        }
                        if (!password.equals(rPassword)) {
                            Toast.makeText(SingUpActivity.this, "La contraseña debe coincidir", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }
}