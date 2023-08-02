package com.example.loginapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import javax.crypto.SecretKey;

public class MainActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPasssword;
    private TextView textViewPassword, textViewSingUp;
    private Button buttonLogo;
    private RegularExpressions regularExp;
    private DatabaseAccess databaseAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        textViewPassword = findViewById(R.id.textViewPassword);
        textViewSingUp = findViewById(R.id.textViewSingUp);
        buttonLogo = findViewById(R.id.buttonLogin);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPasssword = findViewById(R.id.editTextPassword);
        regularExp = new RegularExpressions();
        databaseAccess = new DatabaseAccess(this);

        /*SUBRAYADO DE LOS TEXTVIEW DEL LOGIN*/
        String text1 = "¿Contraseña olvidada?";
        SpannableString content1 = new SpannableString(text1);
        content1.setSpan(new UnderlineSpan(), 0, text1.length(), 0);

        String text2 = "Crear una cuenta";
        SpannableString content2 = new SpannableString(text2);
        content2.setSpan(new UnderlineSpan(), 0, text2.length(), 0);

        textViewPassword.setText(content1);
        textViewSingUp.setText(content2);

        /*ACCESO PARA REGISTRAR CUENTA*/
        textViewSingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(MainActivity.this, SingUpActivity.class);
                startActivity(next);
            }
        });

        /*ACCESO PARA RECUPERAR CUENTA*/
        textViewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(MainActivity.this, RecoverPasswordActivity.class);
                startActivity(next);
            }
        });

        /*ACCESO AL SISTEMA*/
        buttonLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                String password = editTextPasssword.getText().toString();

                if(email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Debes de llenar toda la información", Toast.LENGTH_SHORT).show();
                } else {
                    if (regularExp.validateEmail(email) && regularExp.validatePassword(password)) {
                        try {
                            if (databaseAccess.searchInformation(email, password)) {
                                Toast.makeText(MainActivity.this, "Credenciales Aprovadas", Toast.LENGTH_LONG).show();
                                Thread.sleep(4000);
                                Intent next = new Intent(MainActivity.this, SystemAdminActivity.class);
                                startActivity(next);
                            } else {
                                //Toast.makeText(MainActivity.this, "No aprovado", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                        }

                    } else {
                        if (!regularExp.validateEmail(email)) {
                            Toast.makeText(MainActivity.this, "Email no valido", Toast.LENGTH_SHORT).show();
                        }
                        if (!regularExp.validatePassword(password)) {
                            Toast.makeText(MainActivity.this, "Password no valido", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        //ELIMINAR TODAS LAS LLAVES EXISTENTES
        /*
        EncryptDecrypt encryptDecrypt = new EncryptDecrypt();
        encryptDecrypt.setContext(this);
        encryptDecrypt.deleteKeys();*/

        //MOSTRAR ALGUN USUARIO EN ESPECIFICO
        /*
        for (int i = 0; i < 3; i++) {
            databaseAccess.loadInformation(i + 1);
        }*/
    }
}